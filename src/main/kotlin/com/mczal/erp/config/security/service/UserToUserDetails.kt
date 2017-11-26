package com.dot.service.config.security.service

import com.dot.service.config.model.jwt.JwtUser
import com.mczal.erp.model.User
import org.springframework.beans.BeanUtils
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

/**
 * Created by mczal on 8/15/17.
 */
@Component
class UserToUserDetails : Converter<User, JwtUser> {

  override fun convert(user: User): JwtUser {
    val userDetails = JwtUser(
        name = user.name,
        password = user.encryptedPassword,
        isEnabled = user.enabled,
        authorities = emptyList())
    BeanUtils.copyProperties(user, userDetails)

    return userDetails
  }

}