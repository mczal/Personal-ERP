package com.dot.service.config.model.jwt

import com.jawacorp.common.base.rest.McBaseRestResponse
import com.jawacorp.common.base.rest.error.HttpCode

/**
 * Created by mczal on 8/15/17.
 */
data class JwtAuthenticationResponse(

    private val requestIdz: String,
    private val httpCode: HttpCode,
    val token: String

) : McBaseRestResponse(requestIdz, httpCode)