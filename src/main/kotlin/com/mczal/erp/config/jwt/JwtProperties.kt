package com.dot.service.config.model.jwt

data class JwtProperties (
  val header: String,

  val expiration: Long,

  val secret: String
)