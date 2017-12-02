package com.mczal.erp.model

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "userRole")
data class UserRole(

  @ManyToOne
  @JoinColumn(name = "userId")
  var user: User? = null,

  @ManyToOne
  @JoinColumn(name = "roleId")
  var role: Role? = null

)