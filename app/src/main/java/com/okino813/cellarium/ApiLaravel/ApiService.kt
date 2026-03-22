package com.okino813.cellarium.ApiLaravel

import com.okino813.cellarium.ApiLaravel.Admin.AdminAllInfoResponse
import com.okino813.cellarium.ApiLaravel.Admin.ContainResponse
import com.okino813.cellarium.ApiLaravel.Admin.ItemResponse
import com.okino813.cellarium.ApiLaravel.Admin.StatsResponse
import com.okino813.cellarium.ApiLaravel.User.UserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // Auth Admin
    @POST("auth/admin/login")
    suspend fun loginAdmin(@Body body: LoginAdminRequest): Response<LoginAdminResponse>

    @POST("auth/admin/refresh")
    suspend fun refreshAdmin(): Response<LoginAdminResponse>

    // Auth User
    @POST("auth/user/login")
    suspend fun loginUser(@Body body: LoginUserRequest): Response<LoginUserResponse>

    @POST("auth/user/refresh")          // 👈 à créer côté Laravel aussi
    suspend fun refreshUser(): Response<LoginUserResponse>
    // Admin
    @GET("admin/info")
    suspend fun getAdminInfo(): Response<AdminAllInfoResponse>

    @GET("admin/items")
    suspend fun getItems(): Response<List<ItemResponse>>

    @GET("admin/contains")
    suspend fun getContains(): Response<List<ContainResponse>>

    @GET("admin/stats")
    suspend fun getStats(): Response<StatsResponse>

    // User
    @GET("user/info")
    suspend fun getUserInfo(): Response<UserInfoResponse>
}