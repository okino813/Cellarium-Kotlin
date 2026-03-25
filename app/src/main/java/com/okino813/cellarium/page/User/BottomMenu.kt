package com.okino813.cellarium.page.User

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.okino813.cellarium.ApiLaravel.Admin.Item
import com.okino813.cellarium.R
import com.okino813.cellarium.page.Admin.ContenantAdmin
import com.okino813.cellarium.page.Admin.HomeAdmin
import com.okino813.cellarium.page.Admin.ItemAdmin
import com.okino813.cellarium.page.Admin.MovementAdmin
import com.okino813.cellarium.page.Admin.SourceAdmin
import com.okino813.cellarium.ui.theme.CellariumTheme

@Composable
fun AppUser(
    onLogout : () -> Unit,
    onChangeMode: () -> Unit
) {
    var currentDestinationAdmin by rememberSaveable { mutableStateOf(AppDestinationsAdmin.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinationsAdmin.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label,
                            modifier = Modifier.size(30.dp),
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestinationAdmin,
                    onClick = { currentDestinationAdmin = it }
                )
            }
        }
    ) {
        when(currentDestinationAdmin){
            AppDestinationsAdmin.HOME -> HomeAdmin(
                onLogOut = onLogout,
                onChangeMode = onChangeMode
            )
            AppDestinationsAdmin.MOUVEMENT -> MovementAdmin()
            AppDestinationsAdmin.ITEM -> ItemAdmin()
            AppDestinationsAdmin.CONTENANT -> ContenantAdmin()
            AppDestinationsAdmin.SOURCE -> SourceAdmin()
            else -> {
                HomeAdmin(
                    onLogOut = onLogout,
                    onChangeMode = onChangeMode
                )
            }
        }
    }
}

enum class AppDestinationsAdmin(
    val label: String,
    val icon: Int,
) {
    HOME("Home", R.drawable.ic_home),
    MOUVEMENT("Mouvements", R.drawable.ic_transfer),
    ITEM("Items", R.drawable.ic_item),
    CONTENANT("Contenants", R.drawable.ic_contenant),
    SOURCE("Sources", R.drawable.ic_source),
//    FAVORITES("Favorites", R.drawable.ic_favorite),
//    PROFILE("Profile", R.drawable.ic_account_box),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CellariumTheme {
        Greeting("Android")
    }
}