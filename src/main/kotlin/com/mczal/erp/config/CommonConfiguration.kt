package com.mczal.erp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableJpaRepositories("com.mczal.erp.dao")
@EnableJpaAuditing
class CommonConfiguration {

  class SecurityAuditor : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String>? {
      val auth = SecurityContextHolder.getContext().authentication
      if (auth != null) {
        return Optional.of(auth.name)
        //        return auth.name
      } else {
        return Optional.of("bootstrapUsername")
        //        return "bootstrapUsername"
      }
    }
  }

  @Bean
  fun createAuditingListener(): AuditingEntityListener {
    return AuditingEntityListener()
  }

  @Bean
  fun createAuditorProvider(): AuditorAware<String> {
    return SecurityAuditor()
  }

}