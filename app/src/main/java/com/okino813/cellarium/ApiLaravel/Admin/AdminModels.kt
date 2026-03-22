package com.okino813.cellarium.ApiLaravel.Admin

data class AdminAllInfoResponse(
    val items: List<String>
)

data class StatsResponse(
    val items: Int,
    val ruptures: Int,
    val mouvements: Int
)


/* Les class du getItems */
data class ItemResponse(
    val id: Int,
    val name: String,
    val total_qty: Int,
    val state: Int,
    val is_stock: Int,
    val seuil: Int,
    val containings: List<ContainingResponse>  // 👈 relation with('containings')
)

// Contient la liste des conteneurs d'item.
data class ContainingResponse(
    val id: Int,
    val name: String,
    val pivot: PivotContaining  // 👈 withPivot('qty_affect')
)

// Contient la table pivot item containing
data class PivotContaining(
    val qty_affect: Int
)


/* La classe du getContain */
// Contient simplement la liste des contenants
data class ContainResponse(
    val id: Int,
    val name: String,
    val source: SourcingResponse?
)

data class SourcingResponse(
    val id: Int,
    val name: String,
    val firestation_id: Int
)