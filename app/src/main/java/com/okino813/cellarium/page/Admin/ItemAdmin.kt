package com.okino813.cellarium.page.Admin

import android.R.attr.name
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.setValue
import com.okino813.cellarium.ApiLaravel.Admin.DeleteItemRequest
import com.okino813.cellarium.ApiLaravel.Admin.StoreItemRequest

@Composable
fun ItemAdmin(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit,
    refreshTrigger: () -> Unit,
    context: Context
){
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    var createMode by remember { mutableStateOf<Boolean>(false) }

    val scope = rememberCoroutineScope()

    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    fun deleteItem(item: Item){
        scope.launch {
            isLoading = true
            try {
                val response = ApiAdmin.deleteItem(
                    context,
                    DeleteItemRequest(
                        id = item.id,
                    )
                )
                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()

                if (response.isSuccessful) {
                    refreshTrigger()
                    selectedItem = null
                } else {
                    errorMessage = "Erreur lors de la supression"
                }
            } catch (e: Exception) {
                errorMessage = "Erreur réseau : ${e.message}"
            } finally {
                isLoading = false
            }

        }


    }

    if (selectedItem != null) {
        // Affiche la page de modification
        EditItemAdmin(
            item = selectedItem!!,
            onBack = { selectedItem = null },
            modifier = modifier,
            onLogOut = onLogOut,
            onChangeMode = onChangeMode,
            context = context,
            onRefresh = refreshTrigger,
            deleteItem = {deleteItem(selectedItem!!)}

        )
    } else if(createMode){
        AddItemAdmin(
            onBack = {
                selectedItem = null
                createMode = false
            },
            modifier = modifier,
            onLogOut = onLogOut,
            onChangeMode = onChangeMode,
            context = context,
            onRefresh = refreshTrigger

        )
    }
    else {
        ItemAdminStateless(
            modifier = modifier,
            onLogOut = onLogOut,
            onChangeMode = onChangeMode,
            onSelectItem = { selectedItem = it},
            onCreateModeChange =  {createMode = it},
            onDelete = {
                deleteItem(it)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemAdminStateless(
    modifier: Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit,
    onSelectItem: (Item) -> Unit,
    onCreateModeChange: (Boolean) -> Unit,
    onDelete: (Item) -> Unit
) {

    var selectedContain by remember { mutableStateOf<Item?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // BottomSheet options
    if (showBottomSheet && selectedContain != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    selectedContain!!.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Option modifier
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showBottomSheet = false
                            onSelectItem(selectedContain!!)
                        }
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Modifier", fontSize = 15.sp)
                }

                HorizontalDivider(thickness = 0.5.dp)

                // Option supprimer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showBottomSheet = false
                            onDelete(selectedContain!!)
                        }
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Supprimer",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onCreateModeChange(true)
                },
            ) {
                Icon(Icons.Filled.Add, "Ajouter un item")
            }
        }
    ) { innerPadding ->
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = { onSelectItem(item) },
                                    onLongClick = {
                                        selectedContain = item
                                        showBottomSheet = true
                                    }
                                ),
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
    onRefresh: () -> Unit,
    deleteItem: () -> Unit
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
                item {
                    Button(
                        onClick = { deleteItem() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                            disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.4f),
                            disabledContentColor = MaterialTheme.colorScheme.onError.copy(alpha = 0.4f)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onError
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Supprimer", fontWeight = FontWeight.Medium)
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

@Composable
fun AddItemAdmin(
    context: Context,
    onBack: () -> Unit,
    modifier: Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit,
    onRefresh: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var is_stock by remember { mutableStateOf(false) }
    var total_qty by remember { mutableStateOf(0 )}
    var state by remember { mutableStateOf(true) }
    var seuil by remember { mutableStateOf(10) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    fun storeItem() {
        scope.launch {
            isLoading = true
            try {
                val response = ApiAdmin.storeItem(
                    context,
                    StoreItemRequest(
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
                    Toast.makeText(context, "Item ajouter avec succès !", Toast.LENGTH_LONG).show()
                } else {
                    errorMessage = "Erreur de l'ajout"
                    Log.e("ITEMADD", response.message())
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                errorMessage = "Erreur réseau : ${e.message}"
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
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
                    name,
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
                    onClick = { storeItem() },
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