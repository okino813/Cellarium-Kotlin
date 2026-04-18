package com.okino813.cellarium.ApiLaravel

import com.okino813.cellarium.ApiLaravel.Admin.AdminAllInfoResponse
import com.okino813.cellarium.ApiLaravel.Admin.ContainResponse
import com.okino813.cellarium.ApiLaravel.Admin.DeleteItemRequest
import com.okino813.cellarium.ApiLaravel.Admin.ItemResponse
import com.okino813.cellarium.ApiLaravel.Admin.MovementResponse
import com.okino813.cellarium.ApiLaravel.Admin.SourceResponse
import com.okino813.cellarium.ApiLaravel.Admin.StatsResponse
import com.okino813.cellarium.ApiLaravel.Admin.StoreItemRequest
import com.okino813.cellarium.ApiLaravel.Admin.UpdateContainQtyRequest
import com.okino813.cellarium.ApiLaravel.Admin.UpdateContainRequest
import com.okino813.cellarium.ApiLaravel.Admin.UpdateItemRequest
import com.okino813.cellarium.ApiLaravel.Admin.UpdateResponse
import com.okino813.cellarium.ApiLaravel.Admin.addItemToContainRequest
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

    @GET("admin/sources")
    suspend fun getSources(): Response<List<SourceResponse>>

    @GET("admin/contains")
    suspend fun getContains(): Response<List<ContainResponse>>

    @GET("admin/movements")
    suspend fun getMovements(): Response<List<MovementResponse>>

    @GET("admin/stats")
    suspend fun getStats(): Response<StatsResponse>

    @POST("admin/item/update")
    suspend fun updateItem(@Body body: UpdateItemRequest): Response<UpdateResponse>

    @POST("admin/item/store")
    suspend fun storeItem(@Body body: StoreItemRequest): Response<UpdateResponse>

    @POST("admin/item/destroy")
    suspend fun deleteItem(@Body body: DeleteItemRequest): Response<UpdateResponse>

    @POST("admin/contain/update")
    suspend fun updateContain(@Body body: UpdateContainRequest): Response<UpdateResponse>

    @POST("admin/contain/qty/update")
    suspend fun updateContainQty(@Body body: UpdateContainQtyRequest): Response<UpdateResponse>

    @POST("admin/contain/add/item")
    suspend fun addItemToContain(@Body body: addItemToContainRequest): Response<UpdateResponse>



    // User
    @GET("user/info")
    suspend fun getUserInfo(): Response<UserInfoResponse>
}