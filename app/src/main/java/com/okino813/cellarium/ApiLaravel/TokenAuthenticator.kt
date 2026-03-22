package com.okino813.cellarium.ApiLaravel

import android.content.Context
import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(private val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // Évite une boucle infinie si le refresh échoue aussi
        if (response.request.url.encodedPath.contains("refresh")) return null

        // Détermine quel token utiliser (admin ou user)
        val adminToken = TokenManager.getAdmin(context)
        val userToken = TokenManager.getUser(context)

        val newToken = runBlocking {
            try {
                when {
                    adminToken != null -> {
                        // Refresh du token admin
                        val refreshResponse = ApiClient
                            .createForRefresh(context, adminToken)
                            .refreshAdmin()

                        if (refreshResponse.isSuccessful) {
                            val token = refreshResponse.body()?.access_token
                            if (token != null) {
                                TokenManager.saveAdmin(context, token)
                                Log.d("TokenAuthenticator", "Token admin rafraîchi")
                            }
                            token
                        } else {
                            Log.e("TokenAuthenticator", "Refresh admin échoué : ${refreshResponse.code()}")
                            TokenManager.clearAdmin(context)
                            null
                        }
                    }
                    userToken != null -> {
                        // Refresh du token user
                        val refreshResponse = ApiClient
                            .createForRefresh(context, userToken)
                            .refreshUser()

                        if (refreshResponse.isSuccessful) {
                            val token = refreshResponse.body()?.access_token
                            if (token != null) {
                                TokenManager.saveUser(context, token)
                                Log.d("TokenAuthenticator", "Token user rafraîchi")
                            }
                            token
                        } else {
                            Log.e("TokenAuthenticator", "Refresh user échoué : ${refreshResponse.code()}")
                            TokenManager.clearUser(context)
                            null
                        }
                    }
                    else -> null
                }
            } catch (e: Exception) {
                Log.e("TokenAuthenticator", "Erreur refresh : ${e.message}")
                null
            }
        }

        return if (newToken != null) {
            response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        } else {
            null  // Déconnexion si refresh impossible
        }
    }
}