package com.zendot.auth.service

import com.zendot.auth.model.ZenUser

interface TokenProvider {
    fun generateToken(user: ZenUser): String
}