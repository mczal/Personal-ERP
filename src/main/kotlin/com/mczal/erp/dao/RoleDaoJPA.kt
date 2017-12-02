package com.mczal.erp.dao

import com.mczal.erp.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleDaoJPA: JpaRepository<Role, String> {

  fun findByName(name: String): Role?

}