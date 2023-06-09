package com.dogsteven.anhcop.configurations.security.token_services

import com.dogsteven.anhcop.configurations.anhcop.AnhCopProperties
import com.dogsteven.anhcop.entities.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
@Qualifier("JWTService")
class JWTService(
    anhCopProperties: AnhCopProperties
): TokenService {
    private val audience = anhCopProperties.jwt.audience
    private val issuer = anhCopProperties.jwt.issuer
    private val secret = anhCopProperties.jwt.secretKey

    private val activeDurationInHours: Long = 6L

    override fun generateAccessToken(principal: User.Principal): String {
        val nowInstant = Instant.now()
        val issuedAt = Date.from(nowInstant)
        val expiredAt = Date.from(nowInstant.plusSeconds(activeDurationInHours * 3600L))

        return "JWT:" + Jwts.builder()
            .setAudience(audience)
            .setIssuer(issuer)
            .setIssuedAt(issuedAt)
            .setExpiration(expiredAt)
            .setSubject(principal.user.id.toString())
            .claim("role", principal.user.role)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
    }

    override fun getUserIdFromToken(token: String): Long? {
        return try {
            Jwts.parser()
                .setSigningKey(secret)
                .requireAudience(audience)
                .requireIssuer(issuer)
                .parseClaimsJws(token.substring(startIndex = 4))
                .body.subject.toLong()
        } catch (_: Throwable) {
            null
        }
    }
}