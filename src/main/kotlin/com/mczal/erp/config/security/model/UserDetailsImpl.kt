package com.mczal.erp.config.security.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(

  private val authorities: Collection<GrantedAuthority> = ArrayList(),

  private val enabled: Boolean,

  private val email: String,

  private val credentialsNonExpired: Boolean,

  private val password: String,

  private val accountNonExpired: Boolean,

  private val accountNonLocked: Boolean

) : UserDetails {
  override fun getAuthorities(): Collection<GrantedAuthority> {
    return authorities
  }

  override fun isEnabled(): Boolean {
    return enabled
  }

  override fun getUsername(): String {
    return email
  }

  override fun isCredentialsNonExpired(): Boolean {
    return credentialsNonExpired
  }

  override fun getPassword(): String {
    return password
  }

  override fun isAccountNonExpired(): Boolean {
    return accountNonExpired
  }

  override fun isAccountNonLocked(): Boolean {
    return accountNonLocked
  }
}