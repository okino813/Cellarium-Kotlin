package com.okino813.cellarium.page.Admin

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.okino813.cellarium.R
import com.okino813.cellarium.ui.theme.CellariumTheme
import kotlin.math.exp

@Composable
fun BandeauTop(
    modifier: Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit
){


    BandeauTopStateless(
        modifier = modifier,
        onLogOut = onLogOut,
        onChangeMode = onChangeMode
    )
}


@Composable
fun BandeauTopStateless(
    modifier: Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    return Column(
        modifier = modifier.background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(16.dp),
    ){
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Cellarium Admin")

            Box{
                IconButton(
                    onClick = { expanded = true }
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = "Profile"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Changer de mode") },
                        onClick = {
                            expanded = false
                            onChangeMode()
                            // action
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Paramètres") },
                        onClick = {
                            expanded = false
                            // action
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Déconnexion") },
                        onClick = {
                            expanded = false
                            onLogOut()
                        }
                    )
                }
            }
        }
    }
}