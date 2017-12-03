package com.mczal.erp.model

import com.jawacorp.common.base.McBaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "`role`", uniqueConstraints = arrayOf(
  UniqueConstraint(columnNames = arrayOf("name"))
))
data class Role(

  @Column
  var name: String = ""

): McBaseEntity()
