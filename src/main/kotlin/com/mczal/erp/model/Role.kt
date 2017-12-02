package com.mczal.erp.model

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "`role`", uniqueConstraints = arrayOf(
  UniqueConstraint(columnNames = arrayOf("name"))
))
data class Role(

  var name: String = ""

)
