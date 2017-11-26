package com.dot.service.config.model.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mobile.device.Device
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*

/**
 * Created by mczal on 8/15/17.
 */
@Component
class JwtTokenUtil : Serializable {

  private val AUDIENCE_MOBILE = "mobile"

  private val AUDIENCE_TABLET = "tablet"

  private val AUDIENCE_UNKNOWN = "unknown"

  private val AUDIENCE_WEB = "web"

  private val CLAIM_KEY_AUDIENCE = "audience"

  private val CLAIM_KEY_CREATED = "created"

  private val CLAIM_KEY_USERNAME = "sub"

  private val serialVersionUID = -3301605591108950415L

  @Autowired
  private lateinit var jwtProperties: JwtProperties

  private fun generateAudience(device: Device): String {
    var audience = AUDIENCE_UNKNOWN
    if (device.isNormal) {
      audience = AUDIENCE_WEB
    } else if (device.isTablet) {
      audience = AUDIENCE_TABLET
    } else if (device.isMobile) {
      audience = AUDIENCE_MOBILE
    }
    return audience
  }

  private fun generateExpirationDate(): Date {
    return Date(System.currentTimeMillis() + jwtProperties.expiration * 1000)
  }

  private fun getClaimsFromToken(token: String): Claims? {
    var claims: Claims?
    try {
      claims = Jwts.parser().setSigningKey(jwtProperties.secret).parseClaimsJws(token).body
    } catch (e: Exception) {
      claims = null
    }

    return claims
  }

  private fun ignoreTokenExpiration(token: String): Boolean? {
    val audience = getAudienceFromToken(token)
    return AUDIENCE_TABLET == audience || AUDIENCE_MOBILE == audience
  }

  private fun isCreatedBeforeLastPasswordReset(created: Date, lastPasswordReset: Date?): Boolean {
    return lastPasswordReset != null && created.before(lastPasswordReset)
  }

  private fun isTokenExpired(token: String): Boolean {
    val expiration = getExpirationDateFromToken(token)!!
    return expiration.before(Date())
  }

  fun canTokenBeRefreshed(token: String, lastPasswordReset: Date): Boolean? {
    val created = getCreatedDateFromToken(token)
    return (!isCreatedBeforeLastPasswordReset(created!!, lastPasswordReset)) && (!isTokenExpired(
        token) || ignoreTokenExpiration(token)!!)
  }

  fun generateToken(userDetails: UserDetails, device: Device): String {
    val claims = HashMap<String, Any>()
    claims.put(CLAIM_KEY_USERNAME, userDetails.username)
    claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device))
    claims.put(CLAIM_KEY_CREATED, Date())
    return generateToken(claims)
  }

  fun getAudienceFromToken(token: String): String? {
    var audience: String?
    try {
      val claims = getClaimsFromToken(token)!!
      audience = claims[CLAIM_KEY_AUDIENCE] as String
    } catch (e: Exception) {
      audience = null
    }

    return audience
  }

  fun getCreatedDateFromToken(token: String): Date? {
    var created: Date?
    try {
      val claims = getClaimsFromToken(token)!!
      created = Date(claims[CLAIM_KEY_CREATED] as Long)
    } catch (e: Exception) {
      created = null
    }

    return created
  }

  fun getExpirationDateFromToken(token: String): Date? {
    var expiration: Date?
    try {
      val claims = getClaimsFromToken(token)!!
      expiration = claims.expiration
    } catch (e: Exception) {
      expiration = null
    }

    return expiration
  }

  fun getUsernameFromToken(token: String): String? {
    var username: String?
    try {
      val claims = getClaimsFromToken(token)!!
      username = claims.subject
    } catch (e: Exception) {
      username = null
    }

    return username
  }

  fun refreshToken(token: String): String? {
    var refreshedToken: String?
    try {
      val claims = getClaimsFromToken(token)!!
      claims.put(CLAIM_KEY_CREATED, Date())
      refreshedToken = generateToken(claims)
    } catch (e: Exception) {
      refreshedToken = null
    }

    return refreshedToken
  }

  fun validateToken(token: String, userDetails: UserDetails): Boolean {
    val user = userDetails as JwtUser
    val username = getUsernameFromToken(token)
    val created = getCreatedDateFromToken(token)
    //final Date expiration = getExpirationDateFromToken(token);
    return username == user.getUsername() && !isTokenExpired(token)
  }

  fun validateTokenExpirationOnly(token: String): Boolean {
    getUsernameFromToken(token)!!
    return !isTokenExpired(token)
  }

  internal fun generateToken(claims: Map<String, Any>): String {
    return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
        .signWith(SignatureAlgorithm.HS512, jwtProperties.secret).compact()
  }

}