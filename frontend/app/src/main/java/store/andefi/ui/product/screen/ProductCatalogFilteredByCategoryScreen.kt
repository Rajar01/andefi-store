package store.andefi.ui.product.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import store.andefi.R
import store.andefi.ui.product.component.ProductCard
import store.andefi.ui.product.viewmodel.ProductCatalogFilteredByCategoryViewModel

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCatalogFilteredByCategoryScreen(
    productCatalogFilteredByCategoryViewModel: ProductCatalogFilteredByCategoryViewModel = hiltViewModel(),
    navigateToProductCategoriesRoute: () -> Unit,
    navigateToProductDetailsRoute: (String) -> Unit,
    category: String
) {
    val products =
        productCatalogFilteredByCategoryViewModel.getProducts(category).collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }

    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    // Collect Snackbar events from the ViewModel's Flow
    LaunchedEffect(key1 = Unit) {
        productCatalogFilteredByCategoryViewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(navigationIcon = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { navigateToProductCategoriesRoute() }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_arrow_back_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }, title = { Text(category) })
        },
        contentWindowInsets = WindowInsets.safeContent,
    ) { paddingValues ->
        PullToRefreshBox(
            state = refreshState,
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                products.refresh()
                isRefreshing = false
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                    )
                    .fillMaxSize()
            ) {
                when (products.loadState.refresh) {
                    is LoadState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is LoadState.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.error_ilustration),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(100.dp)
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Terjadi kesalahan",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        "Kami sedang mencoba untuk memperbaikinya. Silahkan coba lagi dalam beberapa menit.",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Button(
                                    onClick = { products.retry() },
                                    shape = MaterialTheme.shapes.small,
                                    contentPadding = PaddingValues(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    ),
                                ) { Text("Coba lagi") }
                            }
                        }
                    }

                    is LoadState.NotLoading -> {
                        LazyVerticalGrid(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(vertical = 20.dp, horizontal = 16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(
                                count = products.itemCount,
                                key = products.itemKey { it.id }
                            ) {
                                val item = products[it]
                                if (item != null) {
                                    Box(
                                        modifier = Modifier.clickable(
                                            interactionSource = null,
                                            indication = null,
                                            onClick = {
                                                navigateToProductDetailsRoute(item.id)
                                            }
                                        )) {
                                        ProductCard(item)
                                    }
                                }
                            }

                            when (products.loadState.append) {
                                is LoadState.Loading -> {
                                    item(span = { GridItemSpan(maxLineSpan) }) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }

                                is LoadState.Error -> productCatalogFilteredByCategoryViewModel.onLoadMoreProductError()

                                is LoadState.NotLoading -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}