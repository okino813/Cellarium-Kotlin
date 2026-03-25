package com.okino813.cellarium.page.Admin

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.okino813.cellarium.TitreH1
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.okino813.cellarium.ApiLaravel.Admin.ApiAdmin
import com.okino813.cellarium.ApiLaravel.Admin.Item
import com.okino813.cellarium.ApiLaravel.Admin.Value
import com.okino813.cellarium.ApiLaravel.ApiClient
import com.okino813.cellarium.ApiLaravel.LoginAdminRequest
import com.okino813.cellarium.ui.theme.Orange

data class Stats(
    val nbr :Int,
    val label: String

)
@Composable
fun HomeAdmin(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit
){

    // Liste des stats
    val dataList = listOf(
        Stats(Value.items.size, "Items"),
        Stats(Value.movements.size, "Mouvements"),
        Stats(Value.nbr_ruptures, "Ruptures"),
        Stats(Value.contains.size, "Contenants"),
    )

    HomeAdminStateless(
        modifier = modifier,
        listStats = dataList,
        onLogOut = onLogOut,
        onChangeMode = onChangeMode
    )
}

@Composable
fun HomeAdminStateless(
    modifier: Modifier,
    listStats: List<Stats>,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit
){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        )
        {
            // On affiche le bandeau en haut de la page
            BandeauTop(
                modifier = modifier,
                onLogOut = onLogOut,
                onChangeMode = onChangeMode
            )

            Column(
                modifier = modifier.padding(start = 16.dp, top = 10.dp),
            ){
                TitreH1(
                    "Statistiques",
                )
                LazyRow{
                    items(listStats) { item ->
                        Card(
                            modifier = Modifier.padding(8.dp)
                        ){
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    item.nbr.toString(),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    item.label,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            Column(
                modifier = modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp),
            ){
                TitreH1(
                    "Rupture de stock",
                )
                LazyColumn{
                    items(Value.items) { item ->
                        // Rajouter la condition "is stock"
                        if((item.total_qty <= item.seuil) && item.is_stock) {
                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column() {
                                        Text(
                                            item.name,
                                            style = TextStyle(
                                                fontSize = 15.sp
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            "${item.total_qty}/${item.seuil}",
                                            style = TextStyle(
                                                fontSize = 10.sp,
                                                color = if(item.total_qty == 0) Color.Red else Orange
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Text(
                                        if(item.total_qty == 0) "Rupture" else "Stock faible",
                                        style = TextStyle(
                                            color = if(item.total_qty == 0) Color.Red else Orange
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}