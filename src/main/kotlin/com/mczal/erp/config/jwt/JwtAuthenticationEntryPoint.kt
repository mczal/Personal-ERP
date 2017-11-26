package com.dot.service.config.model.jwt

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by mczal on 8/15/17.
 */
@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

  override fun commence(p0: HttpServletRequest?, p1: HttpServletResponse?, p2: AuthenticationException?) {
    // This is invoked when merchant tries to access a secured REST resource without supplying any credentials
    // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
    p1!!.sendError(HttpServletResponse.SC_UNAUTHORIZED)
//    return McBaseRestResponse(1, HttpCode.UNAUTHORIZED)
  }

}