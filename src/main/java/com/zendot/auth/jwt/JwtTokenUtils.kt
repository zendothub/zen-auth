package com.zendot.auth.jwt

import com.zendot.auth.model.ZenUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtTokenUtils(
    @Value("\${interkashi.jwt.lifetime}") val jwtLifeDuration: Long,
    @Value("\${interkashi.refreshToken.lifetime}") val refreshTokenLifeDuration: Long,
    @Value("\${interkashi.jwt.secret}") val secretKey: String,
    @Value("\${interkashi.refreshToken.secret}") val refreshTokenSecretKey: String

) {
    fun generateOtpToken(user: ZenUser): String = Jwts.builder()
        .claim("id",user.id)
        .setId(UUID.randomUUID().toString())
        .setIssuedAt(Date())
        .setExpiration(Date.from(Instant.now().plusSeconds(jwtLifeDuration)))      .signWith(getOtpKey())
        .compact()

    fun generateRefreshOtpToken(user: ZenUser): String = Jwts.builder()
        .claim("type", "Refresh Token")
        .subject(user.id)
        .id(UUID.randomUUID().toString())
        .issuedAt(Date())
        .expiration(Date.from(Instant.now().plusSeconds(refreshTokenLifeDuration)))
        .signWith(getRefreshOtpTokenKey())
        .compact()

    fun getOtpKey() = SecretKeySpec(
        Base64.getDecoder().decode(secretKey),
        "HmacSHA256"
    )

    fun parseToken(jwtString: String): Jws<Claims> {
        return Jwts.parser()
            .verifyWith(getOtpKey())
            .build()
            .parseSignedClaims(jwtString)
    }

    fun parseRefreshOtpToken(jwtString: String): Jws<Claims> {
        return Jwts.parser()
            .verifyWith(getRefreshOtpTokenKey())
            .build()
            .parseSignedClaims(jwtString)
    }

    private fun getRefreshOtpTokenKey() = SecretKeySpec(
        Base64.getDecoder().decode(refreshTokenSecretKey),
        "HmacSHA256"
    )
}