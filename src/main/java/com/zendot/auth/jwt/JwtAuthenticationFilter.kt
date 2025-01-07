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

/**
*Custom filter class that performs JWT token validation and user authentication
 */
class JwtAuthenticationFilter(private val tokenProvider: JwtTokenUtils, private val userService: ZenAuthService) :
    OncePerRequestFilter() {
    // The core method of the filter, which checks and processes the token
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        // Extract the JWT token from the request
        val jwtToken = extractToken(request)
        if (jwtToken != null) {
            try {
                val claims = tokenProvider.parseToken(jwtToken).payload
                val userId = claims["id"] // Extract user ID from token payload
                // Fetch the user details using the extracted user ID from the database
                val userDetails = userService.findById(userId.toString()) as UserDetails
                val authentication = ZenAuthenticationToken(userDetails)
                // Create an authentication object with the fetched user details
                val context: SecurityContext = SecurityContextHolder.createEmptyContext()
                context.authentication = authentication
                // Set the authentication object in the security context
                SecurityContextHolder.setContext(context)
            } catch (e: JwtException) {
                // In case of JWT token verification failure, log the error
                println("JWT verification failed: ${e.message}")
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
        // Only apply the filter to paths that start with "/api/"
        val path = request.servletPath
        return !path.startsWith("/api/")
    }

}