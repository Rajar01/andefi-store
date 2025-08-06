package store.andefi.ui.product.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import store.andefi.R
import store.andefi.ui.product.viewmodel.ProductCategoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCategoriesScreen(
    productCategoriesViewModel: ProductCategoriesViewModel = hiltViewModel(),
    navigateToProductCatalogFilteredByCategoryRoute: (category: String) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {}
) {
    val productCategoriesUiState by productCategoriesViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Kategori") })
        },
        bottomBar = bottomNavigationBar,
        contentWindowInsets = WindowInsets.safeContent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                )
                .fillMaxSize()
        ) {
            when (Pair(productCategoriesUiState.isLoading, productCategoriesUiState.isError)) {
                Pair(true, false) -> Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                Pair(false, true) -> Box(
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
                            onClick = { productCategoriesViewModel.getCategories() },
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                        ) { Text("Coba lagi") }
                    }
                }

                Pair(false, false) -> LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(count = productCategoriesUiState.categories.count()) { index ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    navigateToProductCatalogFilteredByCategoryRoute(
                                        productCategoriesUiState.categories[index].name
                                    )
                                }
                                .padding(16.dp)
                        ) {
                            Text(
                                productCategoriesUiState.categories[index].name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_forward_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}