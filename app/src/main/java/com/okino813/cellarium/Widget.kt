package com.okino813.cellarium

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import com.okino813.cellarium.ui.theme.PoppinsFontFamily

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

@Composable
fun TitreH1(
    value: String
){
    Text(
        value,
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    )
}