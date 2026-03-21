package com.okino813.cellarium.page

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.okino813.cellarium.ApiLaravel.ApiClient
import com.okino813.cellarium.ApiLaravel.LoginAdminRequest
import com.okino813.cellarium.ApiLaravel.TokenManager
import com.okino813.cellarium.Input
import kotlinx.coroutines.launch

@Composable
fun LoginStatefull(
    context: Context,
    onLoginAdminSuccess: () -> Unit,
    onLoginUserSuccess: () -> Unit,
){

    val scope = rememberCoroutineScope()
    var code by remember { mutableStateOf("") }

    // Champs de la page login Equipier
    var firstname by remember { mutableStateOf("") }

    // Champs de la page login Admin
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }

    var isPageAdmin by remember { mutableStateOf(false) }

    var LoginSucces by remember { mutableStateOf(false) }

    if(LoginSucces){
        if(isPageAdmin){
            onLoginAdminSuccess()
        }
        else{
            onLoginUserSuccess()
        }
    }

    fun validate() {
        scope.launch {
            if (isPageAdmin) {
                // Vérification des éléments
                var response =
                    ApiClient.create(context).loginAdmin(LoginAdminRequest(code, email, password))

                if(response.isSuccessful){
                    // On sauvegarde le token
                    val body = response.body()
                    if(body != null){
                        TokenManager.save(context, body.access_token)
                        errorMessage = ""
                        // On affiche la page suivante
                        LoginSucces = true;
                    }
                }
                else{
                    // Erreur dans la réponse et afficher un dialogue
                    errorMessage = "Informations incorrecte"
                }
            } else {
                // Vérifiction des éléments
            }
        }
    }

    LoginStateless(
        code = code,
        firstname = firstname,
        onFirstnameChange = {firstname = it},
        onCodeChange = {code = it},
        validate = {validate()},
        isPageAdmin = isPageAdmin,
        pageChange = { isPageAdmin = !isPageAdmin},
        email = email,
        password = password,
        onEmailChange = { email = it},
        onPasswordChange = {password = it},
        errorMessage = errorMessage
    )
}


@Composable
fun LoginStateless(
    code : String,
    firstname : String,
    onCodeChange : (String) -> Unit,
    onFirstnameChange : (String) -> Unit,
    validate : () -> Unit,
    isPageAdmin : Boolean,
    pageChange : () -> Unit,
    email : String,
    password : String,
    onEmailChange : (String) -> Unit,
    onPasswordChange : (String) -> Unit,
    errorMessage: String
){
    return Box(
        Modifier.padding(20.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column{
            Text("Code caserne")
            Input(
                value = code,
                ValueChange = onCodeChange
            )

            Spacer(Modifier.padding(8.dp))

            if(isPageAdmin) {
                Text("Email")
                Input(
                    value = email,
                    ValueChange = onEmailChange,
                )
                Spacer(Modifier.padding(8.dp))

                Text("Mot de passe")
                Input(
                    value = password,
                    ValueChange = onPasswordChange
                )
            }else{
                Text("Firstname")
                Input(
                    value = firstname,
                    ValueChange = onFirstnameChange
                )
            }

            if(errorMessage.length > 1)
            {
                Spacer(Modifier.padding(8.dp))
                Text(
                    errorMessage,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.padding(8.dp))
            }
            else {
                Spacer(Modifier.padding(8.dp))
            }


            Button(onClick= validate){
                Text("Valider")
            }


            Spacer(Modifier.padding(16.dp))
            if(isPageAdmin) {
                Button(
                    onClick = pageChange,
                    Modifier.fillMaxWidth()
                        .background(color = Color(128, 54, 52)),
                    colors = ButtonDefaults.buttonColors(
                        Color(128, 54, 52)
                    )
                ) {
                    Text("Connection équipier")
                }
            }else{
                Button(
                    onClick = pageChange,
                    Modifier.fillMaxWidth()
                        .background(color = Color(128, 54, 52)),
                    colors = ButtonDefaults.buttonColors(
                        Color(128, 54, 52)
                    )
                ) {
                    Text("Connection administrateur")
                }
            }
        }
    }
}