package com.dot.service.config.model.jwt

import com.dot.service.model.Merchant
import org.springframework.beans.BeanUtils

/**
 * Created by mczal on 8/15/17.
 */
class JwtUserFactory {

  companion object {
    //    fun mapToGrantedAuthorities(userRoles: List<UserRole>): List<SimpleGrantedAuthority> {
//      return userRoles.stream().map { userRole -> SimpleGrantedAuthority(userRole.role!!.role) }
//          .toList()
//    }

    fun create(merchant: Merchant): JwtUser {
      val jwtUser = JwtUser(
          name = merchant.name,
          password = merchant.encryptedPassword,
          isEnabled = merchant.enabled,
          authorities = emptyList())
      BeanUtils.copyProperties(merchant, jwtUser)

      return jwtUser
    }
  }

}