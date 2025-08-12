package store.andefi.ui.order.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.util.DebugLogger
import com.valentinilk.shimmer.shimmer
import store.andefi.R
import store.andefi.ui.common.viewmodel.SharedViewModel
import store.andefi.ui.order.state.OrderHistoryUiState
import store.andefi.ui.order.viewmodel.OrderHistoryViewModel
import store.andefi.utility.OrderStatus
import store.andefi.utility.formatInstantToIndonesianDate
import store.andefi.utility.toRupiahFormat
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    orderHistoryViewModel: OrderHistoryViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateToProductCatalogRoute: () -> Unit = {},
    navigateToOrderDetailRoute: (String) -> Unit = {},
    bottomNavigationBar: @Composable () -> Unit = {}
) {
    val orderHistoryUiState by orderHistoryViewModel.uiState.collectAsState()
    val sharedUiState by sharedViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (sharedUiState.isAuthenticated) {
            orderHistoryViewModel.init(sharedUiState.account?.jwtToken ?: "")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pesanan") })
        },
        bottomBar = bottomNavigationBar,
        contentWindowInsets = WindowInsets.safeContent,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            when (orderHistoryUiState) {
                is OrderHistoryUiState.Empty -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            AsyncImage(
                                model = "https://placehold.co/400/png",
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(MaterialTheme.shapes.extraSmall)
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    "Belum ada pesanan",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "Kamu mau beli apa? Temukan barang yang kamu mau di Andefi",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Button(
                                onClick = { navigateToProductCatalogRoute() },
                                shape = MaterialTheme.shapes.medium,
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp, vertical = 8.dp
                                ),
                            ) { Text("Mulai belanja") }
                        }
                    }
                }

                is OrderHistoryUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is OrderHistoryUiState.Error -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
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
                                onClick = {},
                                shape = MaterialTheme.shapes.small,
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),
                            ) {
                                Text("Coba lagi")
                            }
                        }
                    }
                }

                is OrderHistoryUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(if (orderHistoryUiState is OrderHistoryUiState.Success) (orderHistoryUiState as OrderHistoryUiState.Success).orders.size else 0) {
                            val order =
                                (orderHistoryUiState as OrderHistoryUiState.Success).orders[it]

                            var totalPrice = 0L

                            OutlinedCard(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.baseline_shopping_bag_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(32.dp)
                                            )
                                            Column() {
                                                Text(
                                                    "Belanja",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Normal,
                                                )
                                                Text(
                                                    formatInstantToIndonesianDate(order.createdAt),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Normal,
                                                )
                                            }
                                        }
                                        if (order.status == OrderStatus.UNPAID.toString()) {
                                            Text(
                                                "Belum dibayar",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier
                                                    .clip(
                                                        MaterialTheme.shapes.extraSmall
                                                    )
                                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)

                                            )
                                        } else if (order.status == OrderStatus.PAID.toString()) {
                                            Text(
                                                "Dibayar",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier
                                                    .clip(
                                                        MaterialTheme.shapes.extraSmall
                                                    )
                                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)

                                            )
                                        } else if (order.status == OrderStatus.CANCELED.toString()) {
                                            Text(
                                                "Dibatalkan",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier
                                                    .clip(
                                                        MaterialTheme.shapes.extraSmall
                                                    )
                                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)

                                            )
                                        } else if (order.status == OrderStatus.COMPLETED.toString()) {
                                            Text(
                                                "Selesai",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier
                                                    .clip(
                                                        MaterialTheme.shapes.extraSmall
                                                    )
                                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)

                                            )
                                        } else if (order.status == OrderStatus.PACKED.toString()) {
                                            Text(
                                                "Dikemas",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier
                                                    .clip(
                                                        MaterialTheme.shapes.extraSmall
                                                    )
                                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)

                                            )
                                        } else if (order.status == OrderStatus.SHIPPED.toString()) {
                                            Text(
                                                "Dikirim",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier
                                                    .clip(
                                                        MaterialTheme.shapes.extraSmall
                                                    )
                                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)

                                            )
                                        }
                                    }
                                    HorizontalDivider()
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        order.orderItems.forEach { item ->
                                            val discountPrice = calculateDiscountPrice(
                                                price = item.productPrice,
                                                discountPercentage = item.productDiscountPercentage
                                            )
                                            totalPrice += (discountPrice * item.quantity)

                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                SubcomposeAsyncImage(
                                                    model = ImageRequest.Builder(
                                                        LocalContext.current
                                                    )
                                                        .data(
                                                            item.productMediaUrls["image"]
                                                                ?.split("|")?.first()
                                                        ).build(),
                                                    contentDescription = null,
                                                    imageLoader = LocalContext.current.imageLoader.newBuilder()
                                                        .logger(DebugLogger()).build(),
                                                    contentScale = ContentScale.FillBounds,
                                                    loading = {
                                                        Box(
                                                            modifier = Modifier
                                                                .shimmer()
                                                                .size(72.dp)
                                                                .background(Color.LightGray)
                                                        )
                                                    },
                                                    error = {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(72.dp)
                                                                .background(Color.LightGray)
                                                        ) {
                                                            Icon(
                                                                painterResource(R.drawable.baseline_broken_image_24),
                                                                null,
                                                                tint = Color.Gray,
                                                                modifier = Modifier
                                                                    .size(20.dp)
                                                                    .align(Alignment.Center)
                                                            )
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .size(72.dp)
                                                        .clip(MaterialTheme.shapes.extraSmall)
                                                )
                                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                    Text(
                                                        item.productName,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Normal,
                                                        maxLines = 2,
                                                        overflow = TextOverflow.Ellipsis,
                                                    )
                                                    if (item.productDiscountPercentage > 0) {
                                                        Text(
                                                            text = buildAnnotatedString {
                                                                withStyle(
                                                                    SpanStyle()
                                                                ) {
                                                                    append("${item.quantity} x ")
                                                                }
                                                                withStyle(
                                                                    SpanStyle()
                                                                ) {
                                                                    append("${discountPrice.toRupiahFormat()} ")
                                                                }
                                                                withStyle(
                                                                    SpanStyle(
                                                                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                                                        textDecoration = TextDecoration.LineThrough
                                                                    )
                                                                ) {
                                                                    append(item.productPrice.toRupiahFormat())
                                                                }
                                                            },
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.Normal,
                                                        )
                                                    } else {
                                                        Text(
                                                            "${item.quantity} x ${item.productPrice.toRupiahFormat()}",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.Normal,
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column {
                                            Text(
                                                "Total belanja",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal
                                            )
                                            Text(
                                                totalPrice.toRupiahFormat(),
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                navigateToOrderDetailRoute(order.id)
                                            },
                                            shape = MaterialTheme.shapes.medium,
                                            contentPadding = PaddingValues(
                                                horizontal = 12.dp, vertical = 4.dp
                                            ),
                                        ) { Text("Selengkapnya") }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        // OrderIntervalFilterBottomSheet()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun isProductDiscountValid(startDate: Instant, endDate: Instant): Boolean {
    val now: Instant = Instant.now()

    return !now.isBefore(startDate) && !now.isAfter(endDate)
}

private fun calculateDiscountPrice(price: Long, discountPercentage: Long): Long {
    val numerator = (100 - discountPercentage) * price
    val denominator = 100L

    return (numerator + denominator - 1) / denominator
}