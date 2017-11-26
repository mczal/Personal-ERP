package com.dot.service.config.model.jwt

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by mczal on 8/15/17.
 */
class JwtAuthenticationTokenFilter : OncePerRequestFilter() {

  @Autowired
  lateinit var jwtTokenUtil: JwtTokenUtil

  @Autowired
  lateinit var jwtUserDetailsService: UserDetailsService

  @Autowired
  lateinit var jwtProperties: JwtProperties

  private val cust_logger: Logger = LoggerFactory.getLogger(this::class.java)

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse,
      chain: FilterChain) {
    val header = request.getHeader(jwtProperties.header)

    cust_logger.debug("running doFilterInternal for header -> " + header + " in " + this.javaClass)

    if (header != null && header.startsWith("Bearer ") && header.substring(7).isNotEmpty()) {
      val authToken = header.substring(7)
      val username = jwtTokenUtil.getUsernameFromToken(authToken)

      if (SecurityContextHolder.getContext().authentication == null) {

        /**
         * It is not compelling necessary to load the merchant details from the database. You could also store the information
         * in the token and read it from it. It's up to you ;)
         *
         * TODO : Consider using Redis to store username as a key and userDetails as a value for more fast processing.
         * TODO : If already in redis, should not retreive merchant from DB to check existency of merchant
         * TODO : Should reconsider if already in redis, does validate token should always be call ? or not neccessary.
         * */
        val userDetails = this.jwtUserDetailsService.loadUserByUsername(username)

        /**
         * For simple validation it is completely sufficient to just check the token integrity. You don't have to call
         * the database compellingly. Again it's up to you ;)
         * TODO : Should consider to only check token integrity by calling jwtTokenUtil.validateTokenExpirationOnly
         * TODO : pros -> retreive merchant from DB (line 51) are not neccessary again, it could be banished !
         * TODO : For more fast performance
         * */
        if (jwtTokenUtil.validateToken(authToken, userDetails)) {
          val authentication = UsernamePasswordAuthenticationToken(userDetails, null,
              userDetails.authorities)
          authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
          SecurityContextHolder.getContext().authentication = authentication
        }
      }
    }

    chain.doFilter(request, response)
  }

}