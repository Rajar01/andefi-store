package store.andefi.ui.product.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import store.andefi.R

@Composable
fun BottomNavigationBar(
    navigateToProductCatalogRoute: () -> Unit = {},
    navigateToProductCategoriesRoute: () -> Unit = {},
    navigateToCartRoute: () -> Unit = {},
    navigateToOrderHistoryRoute: () -> Unit = {},
    isProductCatalogRouteSelected: Boolean = false,
    isProductCategoriesRouteSelected: Boolean = false,
    isCartRouteSelected: Boolean = false,
    isOrderHistoryRouteSelected: Boolean = false
) {
    NavigationBar {
        NavigationBarItem(
            selected = isProductCatalogRouteSelected, label = { Text("Beranda") },
            onClick = { navigateToProductCatalogRoute() },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_home_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
        )
        NavigationBarItem(
            selected = isProductCategoriesRouteSelected, label = { Text("Kategori") },
            onClick = { navigateToProductCategoriesRoute() },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_category_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
        )
        NavigationBarItem(
            selected = isCartRouteSelected, label = { Text("Keranjang") },
            onClick = { navigateToCartRoute() },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_shopping_cart_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
        )
        NavigationBarItem(
            selected = isOrderHistoryRouteSelected, label = { Text("Pesanan") },
            onClick = { navigateToOrderHistoryRoute() },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_discount_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
        )
    }
}