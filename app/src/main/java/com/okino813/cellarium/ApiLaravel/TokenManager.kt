package com.okino813.cellarium.ApiLaravel

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenManager {
    private const val PREFS_NAME = "secure_prefs"
    private const val KEY_TOKEN = "jwt_token"

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

    fun save(context: Context, token: String) =
        getPrefs(context).edit().putString(KEY_TOKEN, token).apply()

    fun get(context: Context): String? =
        getPrefs(context).getString(KEY_TOKEN, null)

    fun clear(context: Context) =
        getPrefs(context).edit().remove(KEY_TOKEN).apply()
}