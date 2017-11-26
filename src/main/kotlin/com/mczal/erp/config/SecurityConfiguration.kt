package com.mczal.erp.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy

@EnableWebSecurity
@Configuration
class SecurityConfiguration: WebSecurityConfigurerAdapter() {

  @Autowired
  private lateinit var userDetailsService: UserDetailsService

  @Autowired
  fun configAuhentication(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean(name = arrayOf("passwordEncoder"))
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }

  override fun configure(web: WebSecurity) {
    web.ignoring().antMatchers("/resources/**")
  }

  override fun configure(http: HttpSecurity){

    http.headers().cacheControl().disable()
    http.headers().frameOptions().disable()
    http.csrf()

      .and()
        .formLogin()
        .loginPage("/login")
        .usernameParameter("username")
        .passwordParameter("password").permitAll()

      .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll()

      .and()
        .sessionManagement()
        .sessionFixation()
        .changeSessionId()
        .maximumSessions(1)
        .maxSessionsPreventsLogin(true)
        .sessionRegistry(getSessionRegistry())

      .and()

      .and()
        .authorizeRequests()
        .antMatchers("/admin/**")
        .hasAnyRole("ADMIN")

      .and()
        .exceptionHandling()
        .accessDeniedPage("/access_denied")

  }

  @Bean
  fun getSessionAuthStrategy(sessionRegistry: SessionRegistry): SessionAuthenticationStrategy {
    return ConcurrentSessionControlAuthenticationStrategy(sessionRegistry)
  }

  @Bean
  fun getSessionRegistry(): SessionRegistry{
    return SessionRegistryImpl()
  }

}