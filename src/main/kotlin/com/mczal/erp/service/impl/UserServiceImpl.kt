package com.mczal.erp.service.impl

import com.jawacorp.common.base.exception.ApplicationException
import com.jawacorp.common.base.exception.HttpCode
import com.mczal.erp.dao.UserDaoJPA
import com.mczal.erp.model.User
import com.mczal.erp.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserServiceImpl: UserService {

  @Autowired
  private lateinit var userDaoJPA: UserDaoJPA

  override fun findByEmail(email: String): User {
    return userDaoJPA.findByEmail(email) ?: throw ApplicationException(HttpCode.NOT_FOUND)
  }

}