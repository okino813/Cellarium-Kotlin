package com.okino813.cellarium.ApiLaravel.User

data class UserInfoResponse(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val email: String,
    val firestation: String
)