package com.okino813.cellarium

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun Input(
    value : String,
    ValueChange : (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
){
    TextField(
        value = value,
        onValueChange = ValueChange,
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
//        if(isPassword){
//            visualTransformation = PasswordVisualTransformation()
//        }
    )
}