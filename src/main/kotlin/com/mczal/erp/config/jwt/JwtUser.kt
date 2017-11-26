package com.dot.service.config.model.jwt

import com.fasterxml.jackson.annotation.JsonIgnore
import com.jawacorp.common.base.McBaseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

/**
 * Created by mczal on 8/15/17.
 */
data class JwtUser(

    var lastPasswordResetDate: Date? = null,

    private val authorities: Collection<SimpleGrantedAuthority> = ArrayList<SimpleGrantedAuthority>(),

    var encryptedPassword: String = "",

    private var name: String = "",

    var email: String = "",

    private var isEnabled: Boolean = false,

    private var password: String = ""

) : UserDetails, McBaseEntity() {

  override fun getName(): String {
    return this.name
  }

  override fun getAuthorities(): Collection<GrantedAuthority> {
    return authorities
  }

  override fun isEnabled(): Boolean {
    return isEnabled
  }

  override fun getUsername(): String {
    return email
  }

  @JsonIgnore
  override fun isCredentialsNonExpired(): Boolean {
    return true
  }

  override fun getPassword(): String {
    return password
  }

  @JsonIgnore
  override fun isAccountNonExpired(): Boolean {
    return true
  }

  @JsonIgnore
  override fun isAccountNonLocked(): Boolean {
    return true
  }

  override fun toString(): String {
    return "JwtUser{ email='$email', enabled=$isEnabled, id='$id', password='$password' , username='$username'}'"
  }
}