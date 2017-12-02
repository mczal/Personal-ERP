package com.mczal.erp.config.security.service

import com.mczal.erp.config.security.model.UserDetailsImpl
import com.mczal.erp.model.UserRole
import com.mczal.erp.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by mczal on 8/15/17.
 */
@Service("userDetailsService")
@Transactional(readOnly = true)
class UserDetailsServiceImpl : UserDetailsService {

  private val logger = LoggerFactory.getLogger(this.javaClass)

  @Autowired
  lateinit var userService: UserService

  override fun loadUserByUsername(email: String): UserDetails {
    logger.debug("loadUserByUsername '$email' in userDetailsService")
    val user = userService.findByEmail(email)
    return UserDetailsImpl(
      authorities = mapToGrantedAuthorities(user.roles),
      email = user.email,
      enabled = user.enabled,
      accountNonExpired = user.accountNonExpired,
      accountNonLocked = user.accountNonLocked,
      credentialsNonExpired = user.credentialsNonExpired,
      password = user.encryptedPassword
    )
  }

  private fun mapToGrantedAuthorities(userRoles: MutableList<UserRole>): List<GrantedAuthority>{
    return userRoles.map { userRole ->
      SimpleGrantedAuthority(userRole.role!!.name)
    }
  }

}