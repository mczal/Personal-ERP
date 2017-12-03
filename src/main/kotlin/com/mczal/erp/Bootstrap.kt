package com.mczal.erp

import com.mczal.erp.dao.RoleDaoJPA
import com.mczal.erp.model.Role
import com.mczal.erp.model.User
import com.mczal.erp.model.UserRole
import com.mczal.erp.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.PropertySource
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:bootstrap-config.properties")
class Bootstrap: ApplicationListener<ContextRefreshedEvent> {

  private val logger = LoggerFactory.getLogger(this::class.java)

  @Value("\${bootstrap.password}")
  private lateinit var defaultPassword: String

  @Value("\${bootstrap.email}")
  private lateinit var defaultEmail: String

  @Value("\${bootstrap.name}")
  private lateinit var defaultName: String

  @Autowired
  private lateinit var roleDaoJPA: RoleDaoJPA

  @Autowired
  private lateinit var userService: UserService

  private fun loadRole(){
    if(roleDaoJPA.count() > 0){
      return
    }
    logger.debug("Creating default role...")

    val roleAdmin = Role(name = "ROLE_ADMIN")
    roleDaoJPA.save(roleAdmin)
  }

  private fun loadAdmin(){
    logger.debug("UserCount: ${userService.count()}")
    if(userService.count() > 0){
      return
    }
    logger.debug("Creating default user admin...")

    logger.debug("Searching role admin...")
    val adminRole = roleDaoJPA.findByName("ROLE_ADMIN")!!
    logger.debug("Found role admin")

    val user = User(
      email = defaultEmail,
      name = defaultName,
      password = defaultPassword,
      credentialsNonExpired = true,
      accountNonLocked = true,
      accountNonExpired = true,
      enabled = true
    )
    logger.debug("Saving user...")
    val savedUser = userService.save(user)
    logger.debug("User saved")

    logger.debug("Creating admin role...")
    val userRole = UserRole(
      user = savedUser,
      role = adminRole
    )

    logger.debug("Associates user with role...")
    savedUser.roles.add(userRole)
    userService.update(savedUser)

    logger.debug("Success create user with admin role!")
  }

  override fun onApplicationEvent(contextRefreshedEvent: ContextRefreshedEvent) {
    loadRole()
    loadAdmin()
  }

}