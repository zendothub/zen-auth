package football.interkashi.dashboard.config.auth.jwt

import football.interkashi.dashboard.config.auth.phoneNumber.FirebaseAuthenticationToken
import football.interkashi.dashboard.users.service.UserService
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(private val tokenProvider: JwtTokenUtils, private val userService: UserService) :
    OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwtToken = extractToken(request)
        if (jwtToken != null) {
            try {
                val claims = tokenProvider.parse(jwtToken).body
                val userId = claims.subject.toString() // Extract user ID from token payload
                val userDetails = userService.getUserById(userId)
                val authentication = FirebaseAuthenticationToken(userDetails)
                val context: SecurityContext = SecurityContextHolder.createEmptyContext()
                context.authentication = authentication
                SecurityContextHolder.setContext(context)
            } catch (e: JwtException) {
                logger.error("JWT verification failed: ${e.message}")
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization")
        return if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader.substring(7)
        } else null
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath
        return !path.startsWith("/api/")
    }


}