package com.okino813.cellarium

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.okino813.cellarium.ApiLaravel.Admin.ApiAdmin
import com.okino813.cellarium.ApiLaravel.Admin.Value
import com.okino813.cellarium.ApiLaravel.TokenManager
import com.okino813.cellarium.page.Admin.AppAdmin
import com.okino813.cellarium.page.LoginStatefull
import com.okino813.cellarium.page.User.AppUser
import com.okino813.cellarium.ui.theme.CellariumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val context = LocalContext.current
            val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

            var isConnectedAdmin by remember {
                mutableStateOf(sharedPref.getBoolean("isConnectedAdmin", false))
            }
            var isConnectedUser by remember {
                mutableStateOf(sharedPref.getBoolean("isConnectedUser", false))
            }

            var mode by remember {
                mutableStateOf(sharedPref.getString("mode", "user"))
            }

            var isLoading by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf("") }

            fun LogOutAdmin(){
                sharedPref.edit().putBoolean("isConnectedAdmin", false).apply()
                TokenManager.clearAdmin(context)
                isConnectedAdmin = false
            }

            fun ChangeMode(){
                if(mode == "admin"){
                    mode = "user"
                }
                else{
                    mode = "admin"
                }
                sharedPref.edit().putString("mode", mode).apply()
            }

            // 👇 Se relance quand isConnectedAdmin ou isConnectedUser change
            LaunchedEffect(isConnectedAdmin, isConnectedUser) {
                if (isConnectedAdmin && mode == "admin") {
                    isLoading = true
                    try {
                        ApiAdmin.getInfo(context)
                        Value.nbr_ruptures = Value.items.count { it.total_qty <= it.seuil }
                    } catch (e: Exception) {
                        errorMessage = "Erreur de chargement : ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
                if (isConnectedUser && mode == "user") {
                    isLoading = true
                    try {
                        // ApiUser.getData(context)
                    } catch (e: Exception) {
                        errorMessage = "Erreur de chargement : ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            }

            CellariumTheme {
                when {
                    !isConnectedAdmin && !isConnectedUser -> {
                        LoginStatefull(
                            context = context,
                            specifiMode = false,
                            onLoginAdminSuccess = {
                                sharedPref.edit().putBoolean("isConnectedAdmin", true).apply()
                                sharedPref.edit().putString("mode", "admin").apply()
                                mode = "admin"
                                isConnectedAdmin = true
                                Log.d("Connexion", "La valeur du mode : $mode\n et la valeur du isConnectedAdmin : $isConnectedAdmin")
                            },
                            onLoginUserSuccess = {
                                sharedPref.edit().putBoolean("isConnectedUser", true).apply()
                                sharedPref.edit().putString("mode", "user").apply()
                                mode = "user"
                                isConnectedUser = true
                            },
                            onChangeLogin = {
                                if(mode == "admin"){
                                    sharedPref.edit().putString("mode", "user").apply()
                                    mode = "user"
                                }else{
                                    sharedPref.edit().putString("mode", "admin").apply()
                                    mode = "admin"
                                }
                            }
                        )
                    }
                    (mode == "user" && !isConnectedUser) || (mode == "admin" && !isConnectedAdmin) ->{
                        LoginStatefull(
                            context = context,
                            specifiMode = if (mode == "admin")true else false,
                            onLoginAdminSuccess = {
                                sharedPref.edit().putBoolean("isConnectedAdmin", true).apply()
                                sharedPref.edit().putString("mode", "admin").apply()
                                mode = "admin"
                                isConnectedAdmin = true
                            },
                            onLoginUserSuccess = {
                                sharedPref.edit().putBoolean("isConnectedUser", true).apply()
                                isConnectedUser = true
                            },
                            onChangeLogin = {
                                if(mode == "admin"){
                                    sharedPref.edit().putString("mode", "user").apply()
                                    mode = "user"
                                }else{
                                    sharedPref.edit().putString("mode", "admin").apply()
                                    mode = "admin"
                                }
                            }
                        )
                    }
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    errorMessage.isNotEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(errorMessage, color = Color.Red)
                        }
                    }
                    isConnectedAdmin && mode == "admin" -> AppAdmin(onLogOut = {LogOutAdmin()}, onChangeMode = {ChangeMode()})
                    isConnectedUser && mode == "user" -> AppUser(
                        onLogout = {},
                        onChangeMode = {}
                    )
                    else -> {}
                }
            }
        }
    }
}