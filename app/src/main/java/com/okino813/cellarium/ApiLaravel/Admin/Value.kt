package com.okino813.cellarium.ApiLaravel.Admin

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/* Data class des items et de ses conteneurs associé */
@Immutable
data class Item(
    val id: Int,
    val name: String,
    val total_qty: Int,
    val state: Boolean,
    val is_stock: Boolean,
    val seuil: Int,
    val containings: List<Containing>
)

@Immutable
data class Containing(
    val id: Int,
    val name: String,
    val qty_affect: Int
)

/* --------------------------------------- */
/* Data class des contenants et ses sources associés */

@Immutable
data class Contains(
    val id: Int,
    val name: String,
    val sourcing: Sourcing?
)

@Immutable
data class Sourcing(
    val id: Int,
    val name: String,
    val firestationId: Int
)

object Value {
    val items = mutableStateListOf<Item>()

    var contains = mutableStateListOf<Contains>()
    var nbr_items by mutableStateOf(0)      // 👈 réactif
    var nbr_ruptures by mutableStateOf(0)   // 👈 réactif
}