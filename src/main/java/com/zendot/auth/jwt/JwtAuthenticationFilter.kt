package com.zendot.auth.jwt


import com.zendot.auth.service.ZenAuthService
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(private val tokenProvider: JwtTokenUtils, private val userService: ZenAuthService) :
    OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwtToken = extractToken(request)
        if (jwtToken != null) {
            try {
                val claims = tokenProvider.parseToken(jwtToken).body
                val userId = claims.id.toString() // Extract user ID from token payload
                val userDetails = userService.findById(userId) as UserDetails
                val authentication = ZenAuthenticationToken(userDetails)
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