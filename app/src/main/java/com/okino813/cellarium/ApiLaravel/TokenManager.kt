package com.okino813.cellarium.ApiLaravel

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenManager {
    private const val PREFS_NAME = "secure_prefs"
    private const val KEY_ADMIN_TOKEN = "jwt_admin_token"
    private const val KEY_USER_TOKEN = "jwt_user_token"

    private fun getPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context, PREFS_NAME, masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveAdmin(context: Context, token: String) =
        getPrefs(context).edit().putString(KEY_ADMIN_TOKEN, token).apply()

    fun getAdmin(context: Context): String? =
        getPrefs(context).getString(KEY_ADMIN_TOKEN, null)

    fun saveUser(context: Context, token: String) =
        getPrefs(context).edit().putString(KEY_USER_TOKEN, token).apply()

    fun getUser(context: Context): String? =
        getPrefs(context).getString(KEY_USER_TOKEN, null)

    fun clearAdmin(context: Context) =
        getPrefs(context).edit().remove(KEY_ADMIN_TOKEN).apply()

    fun clearUser(context: Context) =
        getPrefs(context).edit().remove(KEY_USER_TOKEN).apply()
}