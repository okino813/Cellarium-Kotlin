package com.okino813.cellarium.page.Admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BandeauTop(){

    BandeauTopStateless()
}


@Composable
fun BandeauTopStateless(){
    return Column(

    ){
        Text("Bonjour, Axel !")
    }
}