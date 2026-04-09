package com.okino813.cellarium.page.Admin

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.okino813.cellarium.ApiLaravel.Admin.Item
import com.okino813.cellarium.ApiLaravel.Admin.Value
import com.okino813.cellarium.TitreH1
import com.okino813.cellarium.ui.theme.Orange
import com.okino813.cellarium.ui.theme.Vert
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.okino813.cellarium.*
import com.okino813.cellarium.ApiLaravel.Admin.ApiAdmin
import com.okino813.cellarium.ApiLaravel.Admin.UpdateItemRequest
import com.okino813.cellarium.ui.theme.Rouge
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon

@Composable
fun ItemAdmin(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit,
    refreshTrigger: () -> Unit,
    context: Context
){
    var selectedItem by remember { mutableStateOf<Item?>(null) }

    if (selectedItem != null) {
        // Affiche la page de modification
        EditItemAdmin(
            item = selectedItem!!,
            onBack = { selectedItem = null },
            modifier = modifier,
            onLogOut = onLogOut,
            onChangeMode = onChangeMode,
            context = context,
            onRefresh = refreshTrigger

        )
    } else {

        ItemAdminStateless(
            modifier = modifier,
            onLogOut = onLogOut,
            onChangeMode = onChangeMode,
            onSelectItem = { selectedItem = it}
        )
    }
}

@Composable
fun ItemAdminStateless(
    modifier: Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit,
    onSelectItem: (Item) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            BandeauTop(
                modifier = modifier,
                onLogOut = onLogOut,
                onChangeMode = onChangeMode
            )
            Column(modifier = modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp)) {
                TitreH1("Items")
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(Value.items) { item ->
                        val is_zero = item.total_qty == 0
                        val is_inf = item.total_qty < item.seuil

                        var statusColor: Color
                        var statusLabel: String

                        if(is_inf and !is_zero) {
                            statusLabel = "Bas"
                            statusColor = Orange
                        }
                        else if(is_zero){
                            statusLabel = "Rupture"
                            statusColor = Color.Red
                        }
                        else{
                            statusLabel = "OK"
                            statusColor = Vert
                        }

                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            onClick = { onSelectItem(item) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Nom + seuil
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        item.name,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 15.sp
                                    )
                                    if(item.is_stock) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            "Seuil : ${item.seuil}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                // Stock + badge
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    if(item.is_stock) {
                                        Text(
                                            "${item.total_qty}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 22.sp,
                                            color = statusColor
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = statusColor.copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(99.dp)
                                                )
                                                .padding(horizontal = 10.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                statusLabel,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = statusColor
                                            )
                                        }
                                    }else{
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = Color.Black.copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(99.dp)
                                                )
                                                .padding(horizontal = 10.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                "Non stocké",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
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
    }
}
@Composable
fun EditItemAdmin(
    context: Context,
    item: Item,
    onBack: () -> Unit,
    modifier: Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit,
    onRefresh: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf(item.name) }
    var is_stock by remember { mutableStateOf(item.is_stock) }
    var total_qty by remember { mutableStateOf(item.total_qty) }
    var state by remember { mutableStateOf(item.state) }
    var seuil by remember { mutableStateOf(item.seuil) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    fun updateItem() {
        scope.launch {
            isLoading = true
            try {
                val response = ApiAdmin.updateItem(
                    context,
                    UpdateItemRequest(
                        id = item.id,
                        name = name,
                        is_stock = is_stock,
                        total_qty = total_qty,
                        state = state,
                        seuil = seuil
                    )
                )
                if (response.isSuccessful) {
                    onRefresh()
                    onBack()
                } else {
                    errorMessage = "Erreur de mise à jour"
                }
            } catch (e: Exception) {
                errorMessage = "Erreur réseau : ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            BandeauTop(
                modifier = modifier,
                onLogOut = onLogOut,
                onChangeMode = onChangeMode
            )

            // Header avec bouton retour
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            HorizontalDivider(thickness = 0.5.dp)

            // Formulaire scrollable
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Nom
                    SectionCard {
                        Text(
                            "Nom de l'item",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Input(value = name, ValueChange = { name = it })
                    }
                }

                item {
                    // Toggles
                    SectionCard {
                        SwitchRow(
                            label = "Est stocké",
                            checked = is_stock,
                            onCheckedChange = { is_stock = it }
                        )

                        HorizontalDivider(
                            thickness = 0.5.dp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                        SwitchRow(
                            label = "Est actif",
                            checked = state,
                            onCheckedChange = { state = it }
                        )

                    }
                }

                if (is_stock) {
                    item {
                        SectionCard {
                            Text(
                                "Stock actuel",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Input(value = total_qty, ValueChange = { total_qty = it })

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                "Seuil d'alerte",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Input(value = seuil, ValueChange = { seuil = it })
                        }
                    }
                }

                // Erreur
                if (errorMessage.isNotEmpty()) {
                    item {
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Bouton sauvegarder fixe en bas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { updateItem() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Sauvegarder", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

// Composables utilitaires
@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 15.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}