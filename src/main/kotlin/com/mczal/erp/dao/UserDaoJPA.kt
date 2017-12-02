package com.mczal.erp.dao

import com.mczal.erp.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserDaoJPA: JpaRepository<User, String> {

  fun findByEmail(email: String): User?

}