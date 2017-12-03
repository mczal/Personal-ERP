package com.mczal.erp.model

import com.jawacorp.common.base.McBaseEntity
import javax.persistence.*

@Entity
@Table(
  name = "`user`",
  uniqueConstraints = arrayOf( UniqueConstraint(columnNames = arrayOf("email"))),
  indexes = arrayOf(Index(columnList = "email")
))
data class User(

  @Column
  var email: String = "",

  @Column
  var name: String = "",

  @Column
  var encryptedPassword: String = "",

  @Column
  var enabled: Boolean = false,

  @Column
  var credentialsNonExpired: Boolean = false,

  @Transient
  var password: String = "",

  @Column
  var accountNonExpired: Boolean = true,

  @Column
  var accountNonLocked: Boolean = true,

  @OneToMany(
    cascade = arrayOf(CascadeType.ALL),
    fetch = FetchType.LAZY,
    mappedBy = "user"
  )
  var roles: MutableList<UserRole> = ArrayList()

): McBaseEntity()
