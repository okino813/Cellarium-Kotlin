package com.okino813.cellarium.page.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.okino813.cellarium.ApiLaravel.Admin.Value
import com.okino813.cellarium.TitreH1
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.okino813.cellarium.ui.theme.DeleteRouge
import com.okino813.cellarium.ui.theme.Rouge
import com.okino813.cellarium.ui.theme.RougeColor
import com.okino813.cellarium.ui.theme.Vert
import com.okino813.cellarium.ui.theme.VertColor

@Composable
fun MovementAdmin(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
    onChangeMode: () -> Unit
){


    MovementAdminStateless(
        modifier = modifier,
        onLogOut = onLogOut,
        onChangeMode = onChangeMode
    )
}

@Composable
fun MovementAdminStateless(
    modifier: Modifier,
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
                modifier = modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp),
            ) {
                TitreH1("Historique des mouvements")
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(Value.movements) { movement ->

                        ElevatedCard(
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Header — prénom + date
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        movement.firstname,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        movement.date,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(thickness = 0.5.dp)
                                Spacer(modifier = Modifier.height(10.dp))

                                // Liste des items
                                movement.items.forEach { item ->

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    ) {
                                        // Badge opération
                                        val isPositive = item.operation > 0

                                        val badgeBackground = if (isPositive)
                                            MaterialTheme.colorScheme.tertiaryContainer  // vert adaptatif
                                        else
                                            MaterialTheme.colorScheme.errorContainer     // rouge adaptatif

                                        val badgeText = if (isPositive)
                                            MaterialTheme.colorScheme.onTertiaryContainer
                                        else
                                            MaterialTheme.colorScheme.onErrorContainer

                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .background(
                                                    color = badgeBackground,
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (isPositive) "+${item.operation}" else "${item.operation}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = badgeText
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            item.name,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                // Commentaire
                                if (!movement.comment.isNullOrEmpty()) {
                                    HorizontalDivider(thickness = 0.5.dp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp).padding(top = 2.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = movement.comment,
                                            fontSize = 13.sp,
                                            fontStyle = FontStyle.Italic,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
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