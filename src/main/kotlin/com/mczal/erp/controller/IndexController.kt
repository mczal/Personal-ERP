package com.mczal.erp.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class IndexController {

  @RequestMapping(value = "", method = arrayOf(RequestMethod.GET))
  fun index(): String{
    return "index"
  }

  @RequestMapping(value = "/login", method = arrayOf(RequestMethod.GET))
  fun login(): String {
    return "login"
  }

  @RequestMapping(value = "/access_denied", method = arrayOf(RequestMethod.GET))
  fun accessDenied(): String {
    return "access_denied"
  }

}