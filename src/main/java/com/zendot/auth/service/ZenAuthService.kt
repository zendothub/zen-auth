package com.zendot.auth.service

import com.zendot.auth.model.ZenUser


interface ZenAuthService {
    fun findById(id: String): ZenUser
}