package com.okino813.cellarium.ApiLaravel.User

import android.content.Context
import com.okino813.cellarium.ApiLaravel.ApiClient
import retrofit2.Response

object ApiUser {
    suspend fun getInfo(context: Context): Response<UserInfoResponse> {
        return ApiClient.create(context).getUserInfo()
    }
}