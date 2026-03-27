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
        Log.d("TokenAuthenticator", "401 reçu sur : ${response.request.url}")
        Log.d("TokenAuthenticator", "adminToken présent : ${TokenManager.getAdmin(context) != null}")

        if (response.request.url.encodedPath.contains("refresh")) {
            Log.e("TokenAuthenticator", "Refresh lui-même a échoué — abandon")
            return null
        }

        if (response.responseCount() >= 2) {
            Log.e("TokenAuthenticator", "Trop de tentatives — abandon")
            return null
        }

        // Détermine quel token utiliser (admin ou user)
        val adminToken = TokenManager.getAdmin(context)
        val userToken = TokenManager.getUser(context)

        val newToken = runBlocking {
            try {
                when {
                    adminToken != null -> {
                        Log.d("TokenAuthenticator", "Tentative refresh admin avec token : ${adminToken.take(20)}...")
                        // Refresh du token admin
                        val refreshResponse = ApiClient
                            .createForRefresh(context, adminToken)
                            .refreshAdmin()

                        Log.d("TokenAuthenticator", "Refresh response code : ${refreshResponse.code()}")
                        Log.d("TokenAuthenticator", "Refresh response body : ${refreshResponse.errorBody()?.string()}")

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

    private fun Response.responseCount(): Int {
        var count = 1
        var prior = this.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}