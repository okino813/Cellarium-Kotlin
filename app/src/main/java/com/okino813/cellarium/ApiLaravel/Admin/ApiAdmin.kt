package com.okino813.cellarium.ApiLaravel.Admin

import android.content.Context
import android.util.Log
import com.okino813.cellarium.ApiLaravel.ApiClient
import retrofit2.Response

object ApiAdmin {
    suspend fun getInfo(context: Context) {
        getItems(context)
        getContains(context)
        getMovements(context)
    }

    suspend fun getStats(context: Context): Response<StatsResponse> {
        return ApiClient.create(context).getStats()
    }

    suspend fun getItems(context: Context) {
        val response = ApiClient.create(context).getItems()
        if (response.isSuccessful) {
            response.body()?.let { items ->
                Log.d("ApiAdmin", "Items reçus : ${items.size}")
                Value.items.clear()
                Value.items.addAll(
                    items.map { item ->
                        Item(
                            id = item.id,
                            name = item.name,
                            total_qty = item.total_qty,
                            state = item.state == 1,
                            is_stock = item.is_stock == 1,
                            seuil = item.seuil,
                            containings = item.containings.map { c ->
                                Containing(
                                    id = c.id,
                                    name = c.name,
                                    qty_affect = c.pivot.qty_affect  // 👈 on aplatit le pivot
                                )
                            }
                        )
                    }
                )
                Value.nbr_items = items.size
                Log.d("ApiAdmin", "Value.items.size après : ${Value.items.size}")
            }
        }else{
            Log.e("Items", response.body().toString())
        }
    }

    suspend fun getContains(context: Context) {
        val response = ApiClient.create(context).getContains()
        if (response.isSuccessful) {
            response.body()?.let { contains ->
                Log.d("Contains", contains.toString())
                Value.contains.clear()
                Value.contains.addAll(
                    contains.map { contain ->
                        Contains(
                            id = contain.id,
                            name = contain.name,
                            sourcing = contain.source?.let {
                                Sourcing(
                                    id = it.id,
                                    name = it.name,
                                    firestationId = it.firestation_id
                                )
                            }
                        )
                    }
                )
            }
        }else{
            Log.e("Contains", response.body().toString())
        }
    }

    suspend fun getMovements(context: Context) {
        val response = ApiClient.create(context).getMovements()
        if (response.isSuccessful) {
            response.body()?.let { movements ->
                Log.d("Movements", movements.toString())
                Value.movements.clear()
                Value.movements.addAll(
                    movements.map { movement ->
                        Movements(
                            id = movement.id,
                            firstname = movement.firstname,
                            comment = movement.comment,
                            items = movement.items.map {
                                Items(
                                    id = it.id,
                                    name = it.name,
                                    total_qty = it.total_qty,
                                    state = it.state == 1,
                                    is_stock = it.is_stock == 1,
                                    operation = it.operation,
                                )
                            }
                        )
                    }
                )
            }
        }else{
            Log.e("Movements", response.body().toString())
        }
    }


}