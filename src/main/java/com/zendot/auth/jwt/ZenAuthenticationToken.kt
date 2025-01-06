package com.zendot.auth.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails

class ZenAuthenticationToken( val userDetails: UserDetails): AbstractAuthenticationToken(userDetails?.authorities) {
    override fun getCredentials(): Any {
        return userDetails
    }

    override fun getPrincipal(): Any {
       return userDetails
    }
}