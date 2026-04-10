package com.okino813.cellarium.ApiLaravel.Admin

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Date

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

/* --------------------------------------- */
/* Data class des movements et ses items associés */

@Immutable
data class Movements(
    val id: Int,
    val firstname: String,
    val comment: String?,
    val date: String,
    val items: List<Items>
)

@Immutable
data class Items(
    val id: Int,
    val name: String,
    val total_qty: Int,
    val state: Boolean,
    val is_stock: Boolean,
    val operation : Int,
)

/* --------------------------------------- */
/* Data class des sources */
@Immutable
data class Sources(
    val id: Int,
    val name: String,
    val firestationId: Int
)

object Value {
    val items = mutableStateListOf<Item>()

    var contains = mutableStateListOf<Contains>()

    var movements = mutableStateListOf<Movements>()

    var sources = mutableStateListOf<Sources>()
    var nbr_items by mutableStateOf(0)
    var nbr_ruptures by mutableStateOf(0)
}