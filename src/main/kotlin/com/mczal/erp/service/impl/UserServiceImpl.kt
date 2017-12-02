package com.mczal.erp.service.impl

import com.jawacorp.common.base.exception.ApplicationException
import com.jawacorp.common.base.exception.HttpCode
import com.mczal.erp.dao.UserDaoJPA
import com.mczal.erp.model.User
import com.mczal.erp.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserServiceImpl: UserService {

  @Autowired
  private lateinit var userDaoJPA: UserDaoJPA

  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder

  override fun save(user: User): User {
    user.encryptedPassword = passwordEncoder.encode(user.password)
    return userDaoJPA.save(user)
  }

  override fun update(user: User): User {
    return userDaoJPA.save(user)
  }

  override fun findByEmail(email: String): User {
    return userDaoJPA.findByEmail(email) ?: throw ApplicationException(HttpCode.NOT_FOUND)
  }

  override fun count(): Long {
    return userDaoJPA.count()
  }

}