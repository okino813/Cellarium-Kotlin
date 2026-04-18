package com.okino813.cellarium.page.Admin

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okino813.cellarium.ApiLaravel.Admin.ApiAdmin
import com.okino813.cellarium.ApiLaravel.Admin.Contains
import com.okino813.cellarium.ApiLaravel.Admin.Sourcing
import com.okino813.cellarium.ApiLaravel.Admin.UpdateContainRequest
import com.okino813.cellarium.ApiLaravel.Admin.Value
import com.okino813.cellarium.Input
import com.okino813.cellarium.SectionCard
import com.okino813.cellarium.TitreH1
import kotlinx.coroutines.launch
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.DialogProperties
import com.okino813.cellarium.ApiLaravel.Admin.Containing
import com.okino813.cellarium.ApiLaravel.Admin.Item
import com.okino813.cellarium.ApiLaravel.Admin.UpdateContainQtyRequest
import com.okino813.cellarium.ApiLaravel.Admin.addItemToContainRequest
import com.okino813.cellarium.ButtonQty
import com.okino813.cellarium.InputNumber
import com.okino813.cellarium.R

@Composable
fun ContenantAdmin(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit,
    refreshTrigger: () -> Unit,
    context: Context
){
    var selectedContain by remember { mutableStateOf<Contains?>(null) }

    if (selectedContain != null) {
        // Affiche la page de modification
        EditContenantAdmin(
            contain = selectedContain!!,
            onBack = { selectedContain = null },
            modifier = modifier,
            onLogOut = onLogOut,
            onChangeMode = onChangeMode,
            context = context,
            onRefresh = refreshTrigger

        )
    } else {
        ContenantAdminStateless(
            modifier = modifier,
            onLogOut = onLogOut,
            onChangeMode = onChangeMode,
            onSelectContain = { selectedContain = it}
        )
    }
}

@Composable
fun ContenantAdminStateless(
    modifier: Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit,
    onSelectContain: (Contains) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            BandeauTop(
                modifier = modifier,
                onLogOut = onLogOut,
                onChangeMode = onChangeMode
            )
            Column(modifier = modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp)) {
                TitreH1("Contenants")
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(Value.contains) { contain ->
                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            onClick = { onSelectContain(contain) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                // Nom
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        contain.name,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        if (contain.sourcing != null) contain.sourcing.name else "Aucune source",
                                        fontSize = 12.sp,
                                        color = if (contain.sourcing != null)
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        else
                                            MaterialTheme.colorScheme.error
                                    )
                                }

                                // Flèche
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContenantAdmin(
    context: Context,
    contain: Contains,
    onBack: () -> Unit,
    modifier: Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit,
    onRefresh: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var id by remember { mutableStateOf(contain.id) }
    var name by remember { mutableStateOf(contain.name) }
    var source by remember { mutableStateOf(contain.sourcing) }

    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var expandedAddAsso by remember { mutableStateOf(false) }
    val options = Value.sources

    var qtyToAdd by remember { mutableStateOf(3) }

    var listItemOutContain = ArrayList<Item>()

    Value.items.forEach { item ->
        var isInContain = false

        item.containings.forEach { contain ->
            if (contain.id == id) {
                isInContain = true
            }
        }

        if(!isInContain){
            listItemOutContain.add(item)
        }
    }

    var selectedItemToAdd by remember {
        mutableStateOf<Item?>(listItemOutContain.firstOrNull())
    }

    var selectedOption by remember {
        mutableStateOf(options.find { it.id == source?.id } ?: options[0])
    }

    var showDialog by remember { mutableStateOf(false) }

    fun updateContain() {
        scope.launch {
            isLoading = true
            try {
                val response = ApiAdmin.updateContain(
                    context,
                    UpdateContainRequest(
                        id = contain.id,
                        name = name,
                        source_id = source?.id
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

    fun updateQtyAffect(
        idContain: Int,
        idItem: Int,
        qty: Int
    ){
        scope.launch {
            isLoading = true
            try {
                val response = ApiAdmin.updateContainQty(
                    context,
                    UpdateContainQtyRequest(
                        idContain = idContain,
                        idItem = idItem,
                        qty = qty
                    )
                )
                if (response.isSuccessful) {
                    val itemIndex = Value.items.indexOfFirst { it.id == idItem }
                    if (itemIndex != -1) {
                        val item = Value.items[itemIndex]
                        val updatedContainings = item.containings.map { containing ->
                            if (containing.id == idContain) {
                                containing.copy(qty_affect = qty)
                            } else {
                                containing
                            }
                        }
                        Value.items[itemIndex] = item.copy(containings = updatedContainings)
                    }
                } else {
                    errorMessage = "Erreur de mise à jour"
                }
            } catch (e: Exception) {
                errorMessage = "Erreur réseau : ${e.message}"
            } finally {
                isLoading = false
            }
        }
        Log.i("UPDATEQTY", "Je change les valeurs suivante :\n Le contain : ${idContain} \n L'item : ${idItem} \n La quantité affecté : ${qty}")
    }

    fun storeItemContaining(
        contain: Contains,
        idItem: Int,
        qty: Int
    ){
        scope.launch {
            isLoading = true
            try {
                // Rajouter une vérification que l'item n'est pas déjà ajouter.
                val response = ApiAdmin.addItemToContain(
                    context,
                    addItemToContainRequest(
                        idContain = contain.id,
                        idItem = idItem,
                        qty = qty
                    )
                )
                if (response.isSuccessful) {
                    val itemIndex = Value.items.indexOfFirst { it.id == idItem }
                    if (itemIndex != -1) {
                        val item = Value.items[itemIndex]

                        val NewContainings = item.containings + Containing(
                            id = contain.id,
                            name = contain.name,
                            qty_affect = qty
                        )
                        Value.items[itemIndex] = item.copy(containings = NewContainings)
                    }
                } else {
                    errorMessage = "Erreur de mise à jour"
                }
            } catch (e: Exception) {
                errorMessage = "Erreur réseau : ${e.message}"
            } finally {
                isLoading = false
            }
        }
        Log.i("UPDATEQTY", "Je change les valeurs suivante :\n Le contain : ${contain.id} \n L'item : ${idItem} \n La quantité affecté : ${qty}")
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            if(showDialog){
                AlertDialog(
                    // set dismiss request
                    onDismissRequest = { showDialog = false },
                    // configure confirm button
                    confirmButton = {
                        Button(onClick = {
                            if(qtyToAdd > 0)
                            {
                                showDialog = false
                                storeItemContaining(
                                    contain = contain,
                                    idItem = selectedItemToAdd?.id ?: -1,
                                    qty = qtyToAdd
                                )
                            }else{
                                Toast.makeText(context, "Qantité minimum : 1", Toast.LENGTH_SHORT).show()
                            }

                        }) {
                            // set button text
                            Text("Confirm")
                        }
                    },
                    // configure dismiss button
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            // set button text
                            Text("Dismiss")
                        }
                    },
                    // set title text
                    title = {
                        Text(text = "Association d'item", color = Color.Black)
                    },
                    // set description text
                    text = {
                        Column() {
                            ExposedDropdownMenuBox(
                                expanded = expandedAddAsso,
                                onExpandedChange = { expandedAddAsso = !expandedAddAsso }
                            ) {
                                TextField(
                                    value = selectedItemToAdd?.name ?: "",
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAddAsso)
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedAddAsso,
                                    onDismissRequest = { expandedAddAsso = false }
                                ) {
                                    listItemOutContain.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item.name) },
                                            onClick = {
                                                selectedItemToAdd = item
                                                expandedAddAsso = false
                                            }
                                        )
                                    }
                                }
                            }
                            Text(
                                "Quantité affecté",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Input(
                                value = qtyToAdd.toString(),
                                ValueChange = {
                                    qtyToAdd = it.toIntOrNull() ?: 0
                                },
                                isNumber = true
                            )
                        }
                    },
                    // set padding for contents inside the box
                    modifier = Modifier.padding(16.dp),
                    // define box shape
                    shape = RoundedCornerShape(16.dp),
                    // set box background color
                    containerColor = Color.White,
                    // set icon color
                    iconContentColor = Color.Red,
                    // set title text color
                    titleContentColor = Color.Black,
                    // set text color
                    textContentColor = Color.DarkGray,
                    // set elevation
                    tonalElevation = 8.dp,
                    // set properties
                    properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
                )
            }
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
                    contain.name,
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
                            "Nom du contenant",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Input(value = name, ValueChange = { name = it })

                        Spacer(modifier = Modifier.height(12.dp))


                        // Ici c'est une liste déroulante pour la source
                        Text(
                            "Source associé",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = selectedOption.name,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                options.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption.name) },
                                        onClick = {
                                            selectedOption = selectionOption
                                            source = Sourcing(
                                                id = selectionOption.id,
                                                name = selectionOption.name,
                                                firestationId = selectionOption.firestationId
                                            )
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = { updateContain() },
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

                item {
                    SectionCard {
                        Text(
                            "Items associé",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Column{
                            Value.items.forEach { item ->
                                item.containings.forEach { contain ->
                                    if(contain.id == id ){

                                        var qty by remember { mutableStateOf(contain.qty_affect.toString()) }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = item.name,
                                                fontSize = 10.sp
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceAround,
                                                modifier = Modifier
                                                    .clip(CircleShape)
                                                    .border(2.dp, Color.Black, CircleShape)
                                                    .height(36.dp)
                                            ) {
                                                ButtonQty(
                                                    onClick = {
                                                        var qtyInt = qty.toIntOrNull() ?: 0 // Evite le crash la valeur est null
                                                        qtyInt = qtyInt + 1
                                                        qty = qtyInt.toString()
                                                        Log.d("updateQty", "Envoi idContain=${contain.id}, idItem=${item.id}, qty=$qtyInt")
                                                        updateQtyAffect(
                                                            idContain = contain.id,
                                                            idItem = item.id,
                                                            qty = qtyInt
                                                        )
                                                    },
                                                    text = "+"
                                                )
                                                Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Gray))

                                                InputNumber(
                                                    value = qty,
                                                    ValueChange = { newValue ->
                                                        qty = newValue
                                                    }
                                                )

                                                Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(Color.Gray))
                                                ButtonQty(
                                                    onClick = {
                                                        var qtyInt = qty.toIntOrNull() ?: 0 // Evite le crash la valeur est null
                                                        if(qtyInt > 0) {
                                                            qtyInt = qtyInt - 1
                                                            qty = qtyInt.toString()
                                                            Log.d("updateQty", "Envoi idContain=${contain.id}, idItem=${item.id}, qty=$qtyInt")
                                                            updateQtyAffect(
                                                                idContain = contain.id,
                                                                idItem = item.id,
                                                                qty = qtyInt
                                                            )
                                                        }
                                                    },
                                                    text = "-"
                                                )
                                            }
                                        }
                                    }

                                    listItemOutContain.add(item)
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Button(
                                    content = {Text("Associé un item")},
                                    onClick = {
                                        showDialog = true
                                    }
                                )
                            }
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

            }
        }
    }
}