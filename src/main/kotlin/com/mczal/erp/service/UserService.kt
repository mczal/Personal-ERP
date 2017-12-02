package com.mczal.erp.service

import com.mczal.erp.model.User

interface UserService {

  fun save(user: User): User

  fun update(user: User): User

  fun findByEmail(email: String): User

  fun count(): Long

}