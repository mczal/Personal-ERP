package com.dot.service.config.model.jwt

import org.hibernate.validator.constraints.NotEmpty
import java.io.Serializable

/**
 * Created by mczal on 8/15/17.
 */
data class JwtAuthenticationRequest(

    @NotEmpty
    var password: String = "",

    @NotEmpty
    var email: String = ""

) : Serializable {
}