package store.andefi.ui.order.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.platform.LocalUriHandler
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
import coil3.compose.SubcomposeAsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.util.DebugLogger
import com.valentinilk.shimmer.shimmer
import store.andefi.R
import store.andefi.ui.common.viewmodel.SharedViewModel
import store.andefi.ui.order.state.CheckoutUiState
import store.andefi.ui.order.viewmodel.CheckoutViewModel
import store.andefi.utility.toRupiahFormat
import java.time.Instant
import kotlin.text.split

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    checkoutViewModel: CheckoutViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit = {},
    navigateToShippingAddressRoute: () -> Unit = {},
    navigateToOrderDetailRoute: (String) -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current

    val checkoutUiState by checkoutViewModel.uiState.collectAsState()
    val sharedUiState by sharedViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (sharedUiState.isAuthenticated) {
            checkoutViewModel.init(sharedUiState.account?.jwtToken ?: "")
        }
    }

    var totalAmountBeforeDiscount = 0L
    var totalAmountAfterDiscount = 0L
    var totalDiscount = 0L

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(
                                indication = null,
                                interactionSource = null,
                                onClick = {
                                    navigateBack()
                                }
                            )
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_arrow_back_24),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                title = {
                    Text("Checkout pesanan")
                }
            )
        },
        bottomBar = {
            var price = 0L

            if (checkoutUiState is CheckoutUiState.Success) {
                (checkoutUiState as CheckoutUiState.Success).orderCheckoutRequestDto.orderItem.forEach {
                    val item =
                        (checkoutUiState as CheckoutUiState.Success).products.find { productResponseDto -> productResponseDto.id == it.productId }

                    if (item != null) {
                        if (isProductDiscountValid(
                                item.discount.startDate,
                                item.discount.endDate
                            )
                        ) {
                            val discountPrice = calculateDiscountPrice(
                                price = item.price,
                                discountPercentage = item.discount.discountPercentage
                            )

                            price += (discountPrice * it.quantity)
                        } else {
                            price += item.price
                        }
                    }
                }
            }

            BottomAppBar(
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column {
                        Text(
                            "Total pembayaran", style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            price.toRupiahFormat(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Button(
                        onClick = {
                            checkoutViewModel.checkoutOrder(
                                token = sharedUiState.account?.jwtToken ?: "",
                                openUri = { url -> uriHandler.openUri(url) },
                                navigateToOrderDetailRoute = { orderId ->
                                    navigateToOrderDetailRoute(
                                        orderId
                                    )
                                }
                            )
                        },
                        shape = MaterialTheme.shapes.medium,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text("Buat pesanan")
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets.safeContent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            when (checkoutUiState) {
                is CheckoutUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is CheckoutUiState.Error -> {
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

                is CheckoutUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState(0)),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable(
                                    interactionSource = null,
                                    indication = null,
                                    onClick = {
                                        navigateToShippingAddressRoute()
                                    }
                                )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Alamat pengiriman",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_forward_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                (checkoutUiState as CheckoutUiState.Success).shippingAddressResponseDto.address,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                        HorizontalDivider()
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                "Rincian produk",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                (checkoutUiState as CheckoutUiState.Success).orderCheckoutRequestDto.orderItem.forEach {
                                    val item =
                                        (checkoutUiState as CheckoutUiState.Success).products.find { productResponseDto -> productResponseDto.id == it.productId }

                                    if (item != null) {
                                        val price: String = if (isProductDiscountValid(
                                                item.discount.startDate,
                                                item.discount.endDate
                                            )
                                        ) {
                                            val discountPrice = calculateDiscountPrice(
                                                price = item.price,
                                                discountPercentage = item.discount.discountPercentage
                                            )

                                            totalAmountAfterDiscount += (discountPrice * it.quantity)
                                            totalDiscount += ((item.price * it.quantity) - (discountPrice * it.quantity))

                                            discountPrice.toRupiahFormat()
                                        } else {
                                            totalAmountAfterDiscount += item.price

                                            item.price.toRupiahFormat()
                                        }

                                        totalAmountBeforeDiscount += (item.price * it.quantity)

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
                                                        item.media.urls["image"]
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
                                                    .clip(MaterialTheme.shapes.extraSmall)
                                                    .size(72.dp)
                                            )
                                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(
                                                    item.name,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Normal,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis,
                                                )
                                                if (isProductDiscountValid(
                                                        item.discount.startDate,
                                                        item.discount.endDate
                                                    )
                                                ) {
                                                    Text(
                                                        text = buildAnnotatedString {
                                                            withStyle(
                                                                SpanStyle()
                                                            ) {
                                                                append("${it.quantity} x ")
                                                            }
                                                            withStyle(
                                                                SpanStyle()
                                                            ) {
                                                                append("$price ")
                                                            }
                                                            withStyle(
                                                                SpanStyle(
                                                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                                                    textDecoration = TextDecoration.LineThrough
                                                                )
                                                            ) {
                                                                append(item.price.toRupiahFormat())
                                                            }
                                                        },
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Normal,
                                                    )
                                                } else {
                                                    Text(
                                                        "${it.quantity} x $price",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Normal,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        HorizontalDivider()
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                "Rincian pembayaran",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Total pesanan",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                        )
                                        Text(
                                            totalAmountBeforeDiscount.toRupiahFormat(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Diskon",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                        )
                                        Text(
                                            totalDiscount.toRupiahFormat(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    }
                                }
                                HorizontalDivider()
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Total pembayaran",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal,
                                    )
                                    Text(
                                        totalAmountAfterDiscount.toRupiahFormat(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal,
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