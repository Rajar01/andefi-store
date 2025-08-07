package store.andefi.ui.product.screen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.SubcomposeAsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.util.DebugLogger
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.flow.flowOf
import store.andefi.R
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.ui.common.viewmodel.SharedViewModel
import store.andefi.ui.product.viewmodel.ProductDetailsViewModel
import store.andefi.utility.formatInstantToIndonesianDate
import store.andefi.utility.toRupiahFormat
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productDetailsViewModel: ProductDetailsViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit = {},
    navigateToProductSpecificationsAndDescriptionRoute: (ProductResponseDto?) -> Unit = {},
    navigateToProductReviewAndRatingRoute: (String) -> Unit = {}
) {
    val productDetailsUiState by productDetailsViewModel.uiState.collectAsState()
    val sharedUiState by sharedViewModel.uiState.collectAsState()

    val reviewMedia =
        if (!productDetailsUiState.isLoading) productDetailsViewModel.getProductReviewMedia(
            productDetailsUiState.product!!.id
        )
            .collectAsLazyPagingItems() else flowOf(PagingData.empty<String>()).collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }

    // Collect Snackbar events from the ViewModel's Flow
    LaunchedEffect(key1 = Unit) {
        productDetailsViewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(navigationIcon = {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .size(48.dp)
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = { navigateBack() })
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.outline_arrow_back_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }, title = { })
        },
        bottomBar = {
            BottomAppBar(contentPadding = PaddingValues(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { },
                        shape = MaterialTheme.shapes.small,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    ) { Text("Beli sekarang") }
                    Button(
                        onClick = {
                            productDetailsViewModel.addProductIntoCart(
                                sharedUiState.account?.jwtToken ?: ""
                            )
                        },
                        shape = MaterialTheme.shapes.small,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        enabled = !productDetailsUiState.isAddProductIntoCartLoading,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    ) { Text("Masukkan keranjang") }
                }

            }
        },
        contentWindowInsets = WindowInsets.safeContent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}, modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_camera_alt_24),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        }) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                )
                .fillMaxSize()
        ) {
            // TODO: Handle if product detail failed to load
            when (productDetailsUiState.isLoading) {
                true -> Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                false -> {
                    val now: Instant = Instant.now()
                    val isProductDiscountValid =
                        !now.isBefore(productDetailsUiState.product!!.discount.startDate) && !now.isAfter(
                            productDetailsUiState.product!!.discount.endDate
                        )
                    val discountPrice =
                        (100 - productDetailsUiState.product!!.discount.discountPercentage) * productDetailsUiState.product!!.price / 100

                    val productMediaUrls =
                        productDetailsUiState.product?.media?.urls["image"]?.split("|")
                            ?.map { it.trim() }
                    val productMediaUrlsSize = productMediaUrls?.size ?: 0

                    val productMediaCarouselState = rememberCarouselState {
                        productMediaUrlsSize
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState(0))
                    ) {
                        HorizontalUncontainedCarousel(
                            state = productMediaCarouselState,
                            itemWidth = 500.dp,
                            flingBehavior = CarouselDefaults.singleAdvanceFlingBehavior(
                                productMediaCarouselState
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(productMediaUrls?.get(it)).build(),
                                contentDescription = null,
                                imageLoader = LocalContext.current.imageLoader.newBuilder().logger(
                                    DebugLogger()
                                ).build(),
                                contentScale = ContentScale.FillBounds,
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .shimmer()
                                            .fillMaxWidth()
                                            .height(360.dp)
                                            .background(Color.LightGray)
                                    )
                                },
                                error = {
                                    Box(
                                        modifier = Modifier
                                            .background(Color.LightGray)
                                            .fillMaxWidth()
                                            .height(360.dp),
                                    ) {
                                        Icon(
                                            painterResource(R.drawable.baseline_broken_image_24),
                                            null,
                                            tint = Color.Gray,
                                            modifier = Modifier
                                                .size(48.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(360.dp)
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    if (isProductDiscountValid) discountPrice.toRupiahFormat() else productDetailsUiState.product!!.price.toRupiahFormat(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                )
                                if (isProductDiscountValid) {
                                    Text(
                                        productDetailsUiState.product!!.price.toRupiahFormat(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Normal,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                    Text(
                                        "${productDetailsUiState.product!!.discount.discountPercentage}%",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                            Text(
                                productDetailsUiState.product?.name ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(16.dp)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_24),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    "%.1f (%d)".format(
                                        productDetailsUiState.reviewSummary?.averageRatings ?: 0f,
                                        productDetailsUiState.reviewSummary?.totalRatings ?: 0
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                )
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            shape = CircleShape
                                        ),
                                )
                                Text(
                                    "${productDetailsUiState.product!!.stock.soldQuantity} terjual",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                )
                            }
                        }
                        HorizontalDivider()
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = null,
                                        indication = null,
                                        onClick = {
                                            navigateToProductSpecificationsAndDescriptionRoute(
                                                productDetailsUiState.product
                                            )
                                        })
                            ) {
                                Text(
                                    "Spesifikasi & deskripsi produk",
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
                            if (productDetailsUiState.product!!.attributes.isNotEmpty()) {
                                val lastAttribute =
                                    productDetailsUiState.product!!.attributes.keys.lastOrNull()

                                Column {
                                    for ((key, value) in productDetailsUiState.product!!.attributes) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                        ) {
                                            Text(
                                                key, modifier = Modifier.width(200.dp),
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal,
                                            )
                                            Text(
                                                value,
                                                modifier = Modifier.fillMaxWidth(),
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Normal,
                                            )
                                        }
                                        if (key != lastAttribute) HorizontalDivider()
                                    }
                                }
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    productDetailsUiState.product?.description ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 5,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable(
                                        interactionSource = null,
                                        indication = null,
                                        onClick = {
                                            navigateToProductSpecificationsAndDescriptionRoute(
                                                productDetailsUiState.product
                                            )
                                        })
                                ) {
                                    Text(
                                        "Baca selengkapnya",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal,
                                    )
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.size(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_forward_24),
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                        if (productDetailsUiState.review != null) {
                            HorizontalDivider()
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = null, indication = null, onClick = {
                                                navigateToProductReviewAndRatingRoute(
                                                    productDetailsUiState.product!!.id
                                                )
                                            })
                                ) {
                                    Text(
                                        "Rating & ulasan",
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
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.size(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_star_24),
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Text(
                                        "%.1f".format(
                                            productDetailsUiState.reviewSummary!!.averageRatings
                                        ),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.onSurface,
                                                shape = CircleShape
                                            ),
                                    )
                                    Text(
                                        "${productDetailsUiState.reviewSummary!!.totalRatings} rating",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal,
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.onSurface,
                                                shape = CircleShape
                                            ),
                                    )
                                    Text(
                                        "${productDetailsUiState.reviewSummary!!.totalReviews} ulasan",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal,
                                    )
                                }
                                when (reviewMedia.loadState.refresh) {
                                    is LoadState.Loading -> {
                                        HorizontalUncontainedCarousel(
                                            state = rememberCarouselState { 5 },
                                            itemWidth = 72.dp,
                                            itemSpacing = 12.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .maskClip(
                                                        MaterialTheme.shapes.extraSmall
                                                    )
                                                    .shimmer()
                                                    .size(84.dp)
                                                    .background(Color.LightGray)
                                            )
                                        }
                                    }

                                    is LoadState.Error -> {
                                        HorizontalUncontainedCarousel(
                                            state = rememberCarouselState { 5 },
                                            itemWidth = 72.dp,
                                            itemSpacing = 12.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .maskClip(
                                                        MaterialTheme.shapes.extraSmall
                                                    )
                                                    .size(84.dp)
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
                                        }
                                    }

                                    is LoadState.NotLoading -> {
                                        val reviewMediaCarouselState = rememberCarouselState {
                                            reviewMedia.itemCount
                                        }

                                        HorizontalUncontainedCarousel(
                                            state = reviewMediaCarouselState,
                                            itemWidth = 72.dp,
                                            itemSpacing = 12.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {
                                            when (reviewMedia.loadState.append) {
                                                is LoadState.Loading -> {
                                                    Box(
                                                        modifier = Modifier
                                                            .maskClip(
                                                                MaterialTheme.shapes.extraSmall
                                                            )
                                                            .shimmer()
                                                            .size(84.dp)
                                                            .background(Color.LightGray)
                                                    )
                                                }

                                                is LoadState.Error -> {
                                                    Box(
                                                        modifier = Modifier
                                                            .maskClip(
                                                                MaterialTheme.shapes.extraSmall
                                                            )
                                                            .size(84.dp)
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
                                                }

                                                is LoadState.NotLoading -> {
                                                    val item = reviewMedia[it]

                                                    if (item != null) {
                                                        SubcomposeAsyncImage(
                                                            model = ImageRequest.Builder(
                                                                LocalContext.current
                                                            )
                                                                .data(item).build(),
                                                            contentDescription = null,
                                                            imageLoader = LocalContext.current.imageLoader.newBuilder()
                                                                .logger(
                                                                    DebugLogger()
                                                                ).build(),
                                                            contentScale = ContentScale.FillBounds,
                                                            loading = {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .shimmer()
                                                                        .size(84.dp)
                                                                        .background(Color.LightGray)
                                                                )
                                                            },
                                                            error = {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .size(84.dp)
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
                                                                .maskClip(
                                                                    MaterialTheme.shapes.extraSmall
                                                                )
                                                                .size(84.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        productDetailsUiState.review!!.account.username,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            for (i in 0 until productDetailsUiState.review!!.rating.toInt()) {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier.size(16.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = ImageVector.vectorResource(
                                                            R.drawable.baseline_star_24
                                                        ),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            formatInstantToIndonesianDate(
                                                productDetailsUiState.review!!.createdAt
                                            ),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                    Text(
                                        productDetailsUiState.review!!.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Normal
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