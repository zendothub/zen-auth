package com.zendot.auth.service

import com.zendot.auth.model.ZenUser


interface ZenAuthService {
    /**
     * find user by id
     * it is used in token generation
     */
    fun findById(id: String): ZenUser
}