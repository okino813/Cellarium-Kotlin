package com.okino813.cellarium

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.okino813.cellarium.ui.theme.PoppinsFontFamily

@Composable
fun Input(
    value: String,
    ValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = ValueChange,
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun Input(
    value: Int,
    ValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value.toString(),
        onValueChange = { ValueChange(it.toIntOrNull() ?: 0) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),  // 👈 clavier numérique
        modifier = modifier.fillMaxWidth()
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