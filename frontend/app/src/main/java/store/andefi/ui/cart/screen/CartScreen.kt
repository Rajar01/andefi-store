package store.andefi.ui.cart.screen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.SubcomposeAsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.util.DebugLogger
import com.valentinilk.shimmer.shimmer
import store.andefi.R
import store.andefi.data.remote.dto.OrderCheckoutRequestDto
import store.andefi.data.remote.dto.OrderItemCheckoutRequestDto
import store.andefi.ui.cart.state.CartUiState
import store.andefi.ui.cart.viewmodel.CartViewModel
import store.andefi.ui.common.viewmodel.SharedViewModel
import store.andefi.utility.toRupiahFormat
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit = {},
    navigateToCheckoutRoute: (OrderCheckoutRequestDto) -> Unit = {},
) {
    val cartUiState by cartViewModel.uiState.collectAsState()
    val sharedUiState by sharedViewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (sharedUiState.isAuthenticated) {
            cartViewModel.getCart(sharedUiState.account?.jwtToken ?: "")
        }

        // Collect Snackbar events from the ViewModel's Flow
        // TODO: Handle if multiple snackbar show up
        cartViewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(
                                interactionSource = null,
                                indication = null,
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
                    Text("Keranjang")
                }
            )
        },
        bottomBar = {
            var totalAmount = 0L
            var totalDiscount = 0L

            if (cartUiState is CartUiState.Success) {
                (cartUiState as CartUiState.Success).cartItemsChecked.forEach {
                    if (isProductDiscountValid(
                            it.product.discount.startDate,
                            it.product.discount.endDate
                        )
                    ) {
                        val discountPrice = calculateDiscountPrice(
                            price = it.product.price,
                            discountPercentage = it.product.discount.discountPercentage
                        )

                        totalAmount += (discountPrice * it.quantity)
                        totalDiscount += ((it.product.price * it.quantity) - (discountPrice * it.quantity))
                    } else {
                        totalAmount += (it.product.price * it.quantity)
                    }
                }
            }


            BottomAppBar(contentPadding = PaddingValues(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Checkbox(
                            checked = if (cartUiState is CartUiState.Success) {
                                val cartItemIds =
                                    (cartUiState as CartUiState.Success).cart.cartItems.map { it.id }
                                val cartItemCheckedIds =
                                    (cartUiState as CartUiState.Success).cartItemsChecked.map { it.id }

                                cartItemCheckedIds.containsAll(cartItemIds) && (cartUiState as CartUiState.Success).cart.cartItems.isNotEmpty()
                            } else {
                                false
                            },
                            onCheckedChange = { if (it) cartViewModel.onCartItemCheckedAllChange() else cartViewModel.onCartItemUnCheckedAllChange() },
                            modifier = Modifier
                                .size(18.dp)
                                .padding(0.dp)
                        )
                        Text(
                            "Semua",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End,
                        ) {
                            Text(
                                if (cartUiState is CartUiState.Success) totalAmount.toRupiahFormat() else 0.toRupiahFormat(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    "Total diskon",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                )
                                Text(
                                    if (cartUiState is CartUiState.Success) totalDiscount.toRupiahFormat() else 0.toRupiahFormat(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                )
                            }
                        }
                        Button(
                            onClick = {
                                
                            },
                            shape = MaterialTheme.shapes.medium,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        ) { Text("Checkout") }
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
            when (cartUiState) {
                is CartUiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is CartUiState.Error -> {
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
                                onClick = {
                                    cartViewModel.getCart(sharedUiState.account?.jwtToken ?: "")
                                },
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

                is CartUiState.Empty -> {
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
                            Image(
                                painter = painterResource(R.drawable.empty_cart_illustration),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.extraSmall)
                                    .size(100.dp)
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    "Belum ada produk di keranjang",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    "Kamu mau beli apa? Temukan barang yang kamu mau di Andefi Store",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                is CartUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(if (cartUiState is CartUiState.Success) (cartUiState as CartUiState.Success).cart.cartItems.size else 0) {
                            val price: String = if (isProductDiscountValid(
                                    (cartUiState as CartUiState.Success).cart.cartItems[it].product.discount.startDate,
                                    (cartUiState as CartUiState.Success).cart.cartItems[it].product.discount.endDate
                                )
                            ) {
                                calculateDiscountPrice(
                                    price = (cartUiState as CartUiState.Success).cart.cartItems[it].product.price,
                                    discountPercentage = (cartUiState as CartUiState.Success).cart.cartItems[it].product.discount.discountPercentage
                                ).toRupiahFormat()
                            } else {
                                (cartUiState as CartUiState.Success).cart.cartItems[it].product.price.toRupiahFormat()
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = (cartUiState as CartUiState.Success).cartItemsChecked.any { cartItemChecked ->
                                        cartItemChecked.id == (cartUiState as CartUiState.Success).cart.cartItems[it].id
                                    },
                                    onCheckedChange = { _ ->
                                        cartViewModel.onCartItemCheckedChange((cartUiState as CartUiState.Success).cart.cartItems[it])
                                    },
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(0.dp)
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
                                                (cartUiState as CartUiState.Success)
                                                    .cart.cartItems[it].product.media.urls["image"]
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
                                                    .size(96.dp)
                                                    .background(Color.LightGray)
                                            )
                                        },
                                        error = {
                                            Box(
                                                modifier = Modifier
                                                    .size(96.dp)
                                                    .background(Color.LightGray)
                                            ) {
                                                Icon(
                                                    painterResource(R.drawable.baseline_broken_image_24),
                                                    null,
                                                    tint = Color.Gray,
                                                    modifier = Modifier
                                                        .size(24.dp)
                                                        .align(Alignment.Center)
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .clip(MaterialTheme.shapes.extraSmall)
                                            .size(96.dp)
                                    )
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Text(
                                            (cartUiState as CartUiState.Success).cart.cartItems[it].product.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Bottom,
                                            modifier = Modifier.fillMaxSize()

                                        ) {
                                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(
                                                    price,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                )
                                                if (isProductDiscountValid(
                                                        (cartUiState as CartUiState.Success).cart.cartItems[it].product.discount.startDate,
                                                        (cartUiState as CartUiState.Success).cart.cartItems[it].product.discount.endDate
                                                    )
                                                ) {
                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                            4.dp
                                                        )
                                                    ) {
                                                        Text(
                                                            (cartUiState as CartUiState.Success).cart.cartItems[it].product.price.toRupiahFormat(),
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.Normal,
                                                            textDecoration = TextDecoration.LineThrough
                                                        )
                                                        Text(
                                                            "${(cartUiState as CartUiState.Success).cart.cartItems[it].product.discount.discountPercentage}%",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.Bold,
                                                        )
                                                    }
                                                }
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),

                                                ) {
                                                IconButton(
                                                    modifier = Modifier.size(28.dp),
                                                    onClick = {
                                                        if ((cartUiState as CartUiState.Success).cart.cartItems[it].quantity <= 1L) {
                                                            cartViewModel.onRemoveCartItemFromCartConfirmationDialogShowRequest(
                                                                (cartUiState as CartUiState.Success).cart.cartItems[it]
                                                            )
                                                        } else {
                                                            cartViewModel.decreaseCartItemQuantity(
                                                                sharedUiState.account?.jwtToken
                                                                    ?: "",
                                                                (cartUiState as CartUiState.Success).cart.cartItems[it]
                                                            )
                                                        }
                                                    },
                                                ) {
                                                    Box(
                                                        contentAlignment = Alignment.Center,
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .border(
                                                                width = 1.dp,
                                                                brush = SolidColor(MaterialTheme.colorScheme.outlineVariant),
                                                                shape = CircleShape
                                                            )
                                                    ) {
                                                        Icon(
                                                            imageVector = ImageVector.vectorResource(
                                                                R.drawable.baseline_remove_24
                                                            ),
                                                            contentDescription = null,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }

                                                }
                                                Text(
                                                    (cartUiState as CartUiState.Success).cart.cartItems[it].quantity.toString(),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Normal,
                                                )
                                                IconButton(
                                                    modifier = Modifier.size(28.dp),
                                                    onClick = {
                                                        cartViewModel.increaseCartItemQuantity(
                                                            sharedUiState.account?.jwtToken ?: "",
                                                            (cartUiState as CartUiState.Success).cart.cartItems[it]
                                                        )
                                                    },
                                                ) {
                                                    Box(
                                                        contentAlignment = Alignment.Center,
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .border(
                                                                width = 1.dp,
                                                                brush = SolidColor(MaterialTheme.colorScheme.outlineVariant),
                                                                shape = CircleShape
                                                            )
                                                    ) {
                                                        Icon(
                                                            imageVector = ImageVector.vectorResource(
                                                                R.drawable.baseline_add_24
                                                            ),
                                                            contentDescription = null,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (it < (cartUiState as CartUiState.Success).cart.cartItems.size - 1) HorizontalDivider()
                        }
                    }

                    if ((cartUiState as CartUiState.Success).showRemoveCartItemFromCartConfirmationDialog) {
                        BasicAlertDialog(
                            onDismissRequest = { cartViewModel.onRemoveCartItemFromCartConfirmationDialogDismissRequest() },
                            properties = DialogProperties(
                                dismissOnBackPress = true,
                                dismissOnClickOutside = true
                            )
                        ) {
                            Surface(
                                shape = MaterialTheme.shapes.large,
                                tonalElevation = AlertDialogDefaults.TonalElevation,
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight()
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.wrapContentSize()
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(
                                                R.drawable.outline_error_24
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier.size(72.dp)
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            "Yakin ingin menghapus produk ini?",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            "Produk ini akan dihapus dari keranjang belanja Anda. Anda dapat menambahkannya kembali nanti.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                cartViewModel.onRemoveCartItemFromCartConfirmationDialogDismissRequest()
                                            },
                                            shape = MaterialTheme.shapes.small,
                                            contentPadding = PaddingValues(
                                                horizontal = 16.dp,
                                                vertical = 8.dp
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            Text("Tidak")
                                        }
                                        Button(
                                            onClick = {
                                                (cartUiState as CartUiState.Success).cartItemToBeDeleted?.let {
                                                    cartViewModel.removeProductFromCart(
                                                        sharedUiState.account?.jwtToken ?: "",
                                                        it
                                                    )
                                                }
                                            },
                                            shape = MaterialTheme.shapes.small,
                                            contentPadding = PaddingValues(
                                                horizontal = 16.dp,
                                                vertical = 8.dp
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                        ) {
                                            Text("Ya")
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
}

@RequiresApi(Build.VERSION_CODES.O)
private fun isProductDiscountValid(startDate: Instant, endDate: Instant): Boolean {
    val now: Instant = Instant.now()

    return !now.isBefore(startDate) && !now.isAfter(endDate)
}

private fun calculateDiscountPrice(price: Long, discountPercentage: Long): Long {
    return (100 - discountPercentage) * price / 100
}