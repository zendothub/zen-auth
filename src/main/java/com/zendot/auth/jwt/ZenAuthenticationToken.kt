package com.zendot.auth.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails

/**
 *ZenAuthenticationToken is a custom Spring Security token that holds the authenticated user's details (as UserDetails) and their authorities.
 */
class ZenAuthenticationToken( val userDetails: UserDetails): AbstractAuthenticationToken(userDetails.authorities) {
    override fun getCredentials(): Any {
        return userDetails
    }

    override fun getPrincipal(): Any {
       return userDetails
    }
}