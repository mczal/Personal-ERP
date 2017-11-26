package com.dot.service.config.security.service

import com.dot.service.config.model.jwt.JwtUser
import com.dot.service.model.Merchant
import com.dot.service.service.MerchantService
import com.jawacorp.common.base.rest.error.HttpCode
import com.jawacorp.common.base.rest.error.RestException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by mczal on 8/15/17.
 */
@Service("jwtUserDetailsService")
@Transactional(readOnly = true)
class JwtUserDetailsServiceImpl : UserDetailsService {

  private val logger = LoggerFactory.getLogger(this.javaClass)

  @Autowired
  lateinit var merchantService: MerchantService

  @Autowired
  lateinit var userToMerchantDetails: Converter<Merchant, JwtUser>

  override fun loadUserByUsername(s: String): UserDetails {
    logger.debug("loadUserByUsername '$s' in userDetailsService")
    try {
      val userDetails = userToMerchantDetails
          .convert(merchantService.findByEmail("jwtUserDetailsService", s))
      return userDetails
    } catch (e: Exception) {
      throw RestException("", HttpCode.NOT_FOUND, "Email not found !")
    }

  }

}