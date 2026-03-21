package com.okino813.cellarium

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.okino813.cellarium.page.Admin.AppAdmin
import com.okino813.cellarium.ui.theme.CellariumTheme
import com.okino813.cellarium.page.LoginStatefull

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var context = LocalContext.current;

            // On récupère le fichier de préférences "app_prefs"
            val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

            // On récupère le boolean "isConnected", false par défaut
            var isConnectedAdmin by remember {
                mutableStateOf(
                    sharedPref.getBoolean(
                        "isConnectedAdmin",
                        false
                    )
                )
            }
            var isConnectedUser by remember {
                mutableStateOf(
                    sharedPref.getBoolean(
                        "isConnectedUser",
                        false
                    )
                )
            }


            CellariumTheme {
                if (isConnectedUser || isConnectedAdmin) {
                    AppAdmin()
                } else {
                    LoginStatefull(
                        context,
                        onLoginAdminSuccess = {
                            sharedPref.edit().putBoolean("isConnectedAdmin", true).apply()
                            isConnectedAdmin = true  // 👈 déclenche la recomposition
                        },
                        onLoginUserSuccess = {
                            sharedPref.edit().putBoolean("isConnectedUser", true).apply()
                            isConnectedUser = true
                        }
                    )
                }
            }
        }
    }
}

