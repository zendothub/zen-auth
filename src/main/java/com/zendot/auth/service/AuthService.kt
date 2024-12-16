package com.zendot.auth.service

import com.zendot.auth.model.ZenUser


interface AuthService {
    fun findUserOrCreate(credentials: Map<String, Any>): ZenUser
}