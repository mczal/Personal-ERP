package com.dot.service.config.security

import com.dot.service.config.model.jwt.JwtAuthenticationEntryPoint
import com.dot.service.config.model.jwt.JwtAuthenticationTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Created by mczal on 8/17/17.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

  @Autowired
  lateinit var jwtUserDetailsService: UserDetailsService

  @Autowired
  lateinit var unauthorizedHandler: JwtAuthenticationEntryPoint

  @Bean
  @Throws(Exception::class)
  fun authenticationTokenFilterBean(): JwtAuthenticationTokenFilter {
    return JwtAuthenticationTokenFilter()
  }

  @Autowired
  @Throws(Exception::class)
  fun configAuthentication(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder())
  }

  @Bean(name = arrayOf("passwordEncoder"))
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {

    http.csrf().disable().antMatcher("/api/**")
        .authorizeRequests()
        .antMatchers("/api/*/login").permitAll()
        .antMatchers("/api/*/register").permitAll()
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/api/*/auth/**").authenticated()
        .antMatchers(HttpMethod.POST, "/api/*/auth/**").authenticated()
        .antMatchers(HttpMethod.PUT, "/api/*/auth/**").authenticated()
        .antMatchers(HttpMethod.DELETE, "/api/*/auth/**").authenticated()
        .antMatchers(HttpMethod.OPTIONS, "/api/*/auth/**").permitAll()
        .and()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
        .and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

    http.addFilterBefore(authenticationTokenFilterBean(),
        UsernamePasswordAuthenticationFilter::class.java)

  }

}