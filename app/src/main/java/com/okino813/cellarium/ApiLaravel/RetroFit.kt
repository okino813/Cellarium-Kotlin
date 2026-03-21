package com.okino813.cellarium.ApiLaravel

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:80/api/"

    fun create(context: Context): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}


interface ApiService {
    @POST("auth/admin/login")
    suspend fun loginAdmin(@Body body: LoginAdminRequest): Response<LoginAdminResponse>

    @POST("auth/admin/refresh")
    suspend fun refresh(): Response<LoginAdminResponse>

    @GET("user")
    suspend fun getUser(): Response<UserResponse>
}

data class LoginAdminRequest(val code: String, val email: String, val password: String)
data class LoginAdminResponse(val access_token: String, val expires_in: Int)

data class UserResponse(val access_token: String, val expires_in: Int)