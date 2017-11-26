package com.mczal.erp.model

import com.jawacorp.common.base.McBaseEntity

data class User(

  var email: String = "",

  var name: String = "",

  var enabled: Boolean = false,

  var encryptedPassword: String = ""

): McBaseEntity()
