package com.okino813.cellarium.ApiLaravel

data class LoginAdminRequest(val code: String, val email: String, val password: String)
data class LoginAdminResponse(val access_token: String, val expires_in: Int)

data class LoginUserRequest(val code: String, val firstname: String)
data class LoginUserResponse(val access_token: String, val expires_in: Int)