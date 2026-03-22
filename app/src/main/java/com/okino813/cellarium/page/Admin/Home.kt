package com.okino813.cellarium.page.Admin

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.okino813.cellarium.ApiLaravel.Admin.ApiAdmin
import com.okino813.cellarium.ApiLaravel.Admin.Item
import com.okino813.cellarium.ApiLaravel.Admin.Value
import com.okino813.cellarium.ApiLaravel.ApiClient
import com.okino813.cellarium.ApiLaravel.LoginAdminRequest

data class Stats(
    val nbr :Int,
    val label: String

)
@Composable
fun HomeAdmin(
    modifier: Modifier = Modifier
){

    // Liste des stats
    val dataList = listOf(
        Stats(Value.items.size, "Items"),
        Stats(Value.contains.size, "Contenants"),
        Stats(Value.nbr_ruptures, "Ruptures"),
        Stats(5, "Mouvements"),
    )

    HomeAdminStateless(
        modifier = modifier,
        listStats = dataList
    )
}

@Composable
fun HomeAdminStateless(
    modifier: Modifier,
    listStats: List<Stats>
){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = modifier.padding(innerPadding)
        )
        {
//            BandeauTop()
            Column(
                modifier = modifier.padding(start = 16.dp),
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

        }

    }
}