package store.andefi.ui.order.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import store.andefi.ui.order.state.OrderDetailUiState
import store.andefi.ui.order.viewmodel.OrderDetailViewModel
import store.andefi.ui.order.viewmodel.OrderHistoryViewModel
import store.andefi.utility.OrderStatus
import store.andefi.utility.formatInstantToIndonesianDate
import store.andefi.utility.toRupiahFormat
import kotlin.text.split

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewAndRatingBottomSheet() {
    ModalBottomSheet(
        onDismissRequest = { },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_close_24),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        "Beri rating & tulis ulasan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                HorizontalDivider()
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_outline_24),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_outline_24),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_outline_24),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_outline_24),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_outline_24),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = "",
                    label = { Text("Ulasan") },
                    placeholder = { Text("Bagikan pendapat Anda tentang produk ini") },
                    onValueChange = { },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(2) {
                        AsyncImage(
                            model = "https://placehold.co/400/png",
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.extraSmall)
                                .size(72.dp)
                        )
                    }
                    item {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.extraSmall)
                                .border(
                                    width = 1.dp,
                                    brush = SolidColor(MaterialTheme.colorScheme.outlineVariant),
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .size(72.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_add_24),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
                Button(
                    onClick = { },
                    shape = MaterialTheme.shapes.large,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Kirim") }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderDetailViewModel: OrderDetailViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateToOrderHistoryRoute: () -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current

    val orderDetailUiState by orderDetailViewModel.uiState.collectAsState()
    val sharedUiState by sharedViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (sharedUiState.isAuthenticated) {
            orderDetailViewModel.init(sharedUiState.account?.jwtToken ?: "")
        }
    }

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
                                onClick = { navigateToOrderHistoryRoute() })
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_arrow_back_24),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                title = { Text("Detail pesanan") },
            )
        },
        bottomBar = {
            if (orderDetailUiState is OrderDetailUiState.Success) {
                if ((orderDetailUiState as OrderDetailUiState.Success).order.status == OrderStatus.UNPAID.toString()) {
                    BottomAppBar(contentPadding = PaddingValues(16.dp)) {
                        Button(
                            onClick = {
                                orderDetailViewModel.pay(
                                    (orderDetailUiState as OrderDetailUiState.Success).order.id,
                                    { url -> uriHandler.openUri(url) })
                            },
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier.fillMaxSize()
                        ) { Text("Bayar") }

                    }
                } else if ((orderDetailUiState as OrderDetailUiState.Success).order.status == OrderStatus.COMPLETED.toString()) {
                    BottomAppBar(contentPadding = PaddingValues(16.dp)) {
                        OutlinedButton(
                            onClick = { },
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier.fillMaxSize()
                        ) { Text("Beri rating dan ulasan") }
                    }
                }
            }
        },
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
            when (orderDetailUiState) {
                is OrderDetailUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is OrderDetailUiState.Error -> {
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

                is OrderDetailUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState(0))
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            if ((orderDetailUiState as OrderDetailUiState.Success).order.status == OrderStatus.UNPAID.toString()) {
                                Text(
                                    "Pesanan belum dibayar",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            } else if ((orderDetailUiState as OrderDetailUiState.Success).order.status == OrderStatus.PAID.toString()) {
                                Text(
                                    "Pesanan dibayar",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            } else if ((orderDetailUiState as OrderDetailUiState.Success).order.status == OrderStatus.CANCELED.toString()) {
                                Text(
                                    "Pesanan dibatalkan",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            } else if ((orderDetailUiState as OrderDetailUiState.Success).order.status == OrderStatus.COMPLETED.toString()) {
                                Text(
                                    "Pesanan selesai",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            } else if ((orderDetailUiState as OrderDetailUiState.Success).order.status == OrderStatus.PACKED.toString()) {
                                Text(
                                    "Pesanan dikemas",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            } else if ((orderDetailUiState as OrderDetailUiState.Success).order.status == OrderStatus.SHIPPED.toString()) {
                                Text(
                                    "Pesanan dikirim",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            HorizontalDivider()
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Nomor pesanan",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal,
                                    )
                                    Text(
                                        (orderDetailUiState as OrderDetailUiState.Success).order.id,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal,
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Tanggal pembelian",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal,
                                    )
                                    Text(
                                        formatInstantToIndonesianDate((orderDetailUiState as OrderDetailUiState.Success).order.createdAt),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal,
                                    )
                                }
                                (orderDetailUiState as OrderDetailUiState.Success).order.paidAt?.let { paidAt ->
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Tanggal pembayaran",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                        )
                                        Text(
                                            formatInstantToIndonesianDate(paidAt),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    }
                                }
                                (orderDetailUiState as OrderDetailUiState.Success).order.shippingAt?.let { shippingAt ->
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Tanggal pengiriman",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                        )
                                        Text(
                                            formatInstantToIndonesianDate(shippingAt),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                        )
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
                                "Rincian produk",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                (orderDetailUiState as OrderDetailUiState.Success).order.orderItems.forEach { item ->
                                    val discountPrice = calculateDiscountPrice(
                                        price = item.productPrice,
                                        discountPercentage = item.productDiscountPercentage
                                    )

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
                        }
                        HorizontalDivider()
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                "Alamat pengiriman",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                (orderDetailUiState as OrderDetailUiState.Success).order.shippingAddress.address,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                        HorizontalDivider()
                        if ((orderDetailUiState as OrderDetailUiState.Success).order.payment.method != null && (orderDetailUiState as OrderDetailUiState.Success).order.payment.amount > 0L) {
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
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Metode pembayaran",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                        )
                                        Text(
                                            (orderDetailUiState as OrderDetailUiState.Success).order.payment.method!!,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    }
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
                                            (orderDetailUiState as OrderDetailUiState.Success).order.payment.amount.toRupiahFormat(),
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
//        ReviewAndRatingBottomSheet()
    }
}

private fun calculateDiscountPrice(price: Long, discountPercentage: Long): Long {
    val numerator = (100 - discountPercentage) * price
    val denominator = 100L

    return (numerator + denominator - 1) / denominator
}