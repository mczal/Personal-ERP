package com.mczal.erp

import com.mczal.erp.dao.RoleDaoJPA
import com.mczal.erp.model.Role
import com.mczal.erp.model.User
import com.mczal.erp.model.UserRole
import com.mczal.erp.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.PropertySource
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:bootstrap.config.properties")
class Bootstrap: ApplicationListener<ContextRefreshedEvent> {

  @Value("\${bootstrap.password}")
  private lateinit var defaultPassword: String

  @Autowired
  private lateinit var roleDaoJPA: RoleDaoJPA

  @Autowired
  private lateinit var userService: UserService

  private fun loadRole(){
    if(roleDaoJPA.count() > 0){
      return
    }
    val roleAdmin = Role(name = "ROLE_ADMIN")
    roleDaoJPA.save(roleAdmin)
  }

  private fun loadAdmin(){
    if(userService.count() > 0){
      return
    }
    val adminRole = roleDaoJPA.findByName("ROLE_ADMIN")!!

    val mczal = User(
      email = "mseptrianto@gmail.com",
      name = "Septri",
      password = defaultPassword,
      credentialsNonExpired = true,
      accountNonLocked = true,
      accountNonExpired = true,
      enabled = true
    )
    val savedMczal = userService.save(mczal)

    val mczalRole = UserRole(
      user = savedMczal,
      role = adminRole
    )
    savedMczal.roles.add(mczalRole)
    userService.update(savedMczal)

  }

  override fun onApplicationEvent(contextRefreshedEvent: ContextRefreshedEvent) {
    loadRole()
    loadAdmin()
  }

}