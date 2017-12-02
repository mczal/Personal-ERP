package com.mczal.erp.service

import com.mczal.erp.model.User

interface UserService {

  fun findByEmail(email: String): User

}