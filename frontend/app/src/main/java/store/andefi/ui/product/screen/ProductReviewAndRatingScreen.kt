package store.andefi.ui.product.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.SubcomposeAsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.util.DebugLogger
import com.valentinilk.shimmer.shimmer
import store.andefi.R
import store.andefi.ui.product.viewmodel.ProductReviewAndRatingViewModel
import store.andefi.utility.SortBy
import store.andefi.utility.formatInstantToIndonesianDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = {},
    checked: String?,
    onCheckedChange: (String?) -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
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
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = { onDismissRequest() })
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_close_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    "Rating",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Semua",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f),
                )
                Checkbox(
                    checked = checked.isNullOrBlank(),
                    onCheckedChange = { if (it) onCheckedChange(null) },
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    "Satu",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = checked == "1",
                    onCheckedChange = { if (it) onCheckedChange("1") },
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    "Dua",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = checked == "2",
                    onCheckedChange = { if (it) onCheckedChange("2") },
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    "Tiga",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = checked == "3",
                    onCheckedChange = { if (it) onCheckedChange("3") },
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    "Empat",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = checked == "4",
                    onCheckedChange = { if (it) onCheckedChange("4") },
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_star_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    "Lima",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = checked == "5",
                    onCheckedChange = { if (it) onCheckedChange("5") },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = {},
    checked: String,
    onCheckedChange: (String) -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
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
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = { onDismissRequest() })
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_close_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    "Urutkan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Terbaru",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f),
                )
                Checkbox(
                    checked = checked == SortBy.LATEST.toString(),
                    onCheckedChange = {
                        if (it) onCheckedChange(SortBy.LATEST.toString())
                    },
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Rating tertinggi",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f),
                )
                Checkbox(
                    checked = checked == SortBy.RATING_DESC.toString(),
                    onCheckedChange = {
                        if (it) onCheckedChange(SortBy.RATING_DESC.toString())
                    },
                )
            }
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Rating terendah",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f),
                )
                Checkbox(
                    checked = checked == SortBy.RATING_ASC.toString(),
                    onCheckedChange = {
                        if (it) onCheckedChange(SortBy.RATING_ASC.toString())
                    },
                )
            }
            HorizontalDivider()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductReviewAndRatingScreen(
    productReviewAndRatingViewModel: ProductReviewAndRatingViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {},
    productId: String
) {
    val reviews =
        productReviewAndRatingViewModel.getProductReviews(productId).collectAsLazyPagingItems()

    val productReviewAndRatingUiState by productReviewAndRatingViewModel.uiState.collectAsState()

    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    val ratingBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sortBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val snackbarHostState = remember { SnackbarHostState() }

    // Collect Snackbar events from the ViewModel's Flow
    LaunchedEffect(key1 = Unit) {
        productReviewAndRatingViewModel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
            TopAppBar(navigationIcon = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
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
            }, title = { Text("Rating & ulasan") })
        }, contentWindowInsets = WindowInsets.safeContent
    ) { paddingValues ->
        PullToRefreshBox(
            state = refreshState, isRefreshing = isRefreshing, onRefresh = {
                isRefreshing = true
                reviews.refresh()
                isRefreshing = false
            }) {
            Box(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                    )
                    .fillMaxSize()
            ) {
                when (reviews.loadState.refresh) {
                    is LoadState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is LoadState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
                                    onClick = { reviews.retry() },
                                    shape = MaterialTheme.shapes.small,
                                    contentPadding = PaddingValues(
                                        horizontal = 16.dp, vertical = 8.dp
                                    ),
                                ) { Text("Coba lagi") }
                            }
                        }
                    }

                    is LoadState.NotLoading -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    FilterChip(
                                        selected = productReviewAndRatingUiState.showRatingBottomSheet,
                                        onClick = { productReviewAndRatingViewModel.onRatingBottomSheetShowRequest() },
                                        label = { Text("Rating") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.baseline_star_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        },
                                        trailingIcon = {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_drop_down_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                    )
                                    FilterChip(
                                        selected = productReviewAndRatingUiState.showSortBottomSheet,
                                        onClick = { productReviewAndRatingViewModel.onSortBottomSheetShowRequest() },
                                        label = { Text("Urutkan") },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.outline_swap_vert_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        },
                                        trailingIcon = {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_drop_down_24),
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                    )
                                }
                            }
                            items(
                                count = reviews.itemCount, key = reviews.itemKey { it.id }) {
                                val item = reviews[it]
                                if (item != null) {
                                    val reviewMedia = item.media?.urls["image"]?.split("|")
                                    var isReviewContentExceedMaxLines by remember {
                                        mutableStateOf(
                                            false
                                        )
                                    }
                                    var isReviewContentExpanded by remember {
                                        mutableStateOf(
                                            false
                                        )
                                    }

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                item.account.username,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                    for (i in 0 until item.rating.toInt()) {
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
                                                        item.createdAt
                                                    ),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Normal
                                                )
                                            }
                                            Column(
                                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                                modifier = Modifier.animateContentSize()
                                            ) {
                                                Text(
                                                    item.content,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Normal,
                                                    maxLines = if (isReviewContentExpanded) Int.MAX_VALUE else 5,
                                                    overflow = TextOverflow.Ellipsis,
                                                    onTextLayout = {
                                                        isReviewContentExceedMaxLines =
                                                            it.multiParagraph.didExceedMaxLines
                                                    })

                                                if (isReviewContentExpanded || isReviewContentExceedMaxLines) {
                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                            4.dp
                                                        ),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.clickable(
                                                            interactionSource = null,
                                                            indication = null,
                                                            onClick = {
                                                                isReviewContentExpanded =
                                                                    !isReviewContentExpanded
                                                            })
                                                    ) {
                                                        Text(
                                                            if (isReviewContentExpanded) "Sembunyikan" else "Baca selengkapnya",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.Normal,
                                                        )
                                                        Box(
                                                            contentAlignment = Alignment.Center,
                                                            modifier = Modifier.size(16.dp)
                                                        ) {
                                                            if (isReviewContentExpanded)
                                                                Icon(
                                                                    imageVector = ImageVector.vectorResource(
                                                                        R.drawable.baseline_keyboard_arrow_up_24
                                                                    ),
                                                                    contentDescription = null,
                                                                    modifier = Modifier.size(16.dp)
                                                                ) else Icon(
                                                                imageVector = ImageVector.vectorResource(
                                                                    R.drawable.baseline_keyboard_arrow_down_24
                                                                ),
                                                                contentDescription = null,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        HorizontalUncontainedCarousel(
                                            state = rememberCarouselState {
                                                reviewMedia?.size ?: 0
                                            },
                                            itemWidth = 72.dp,
                                            itemSpacing = 12.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) { index ->
                                            SubcomposeAsyncImage(
                                                model = ImageRequest.Builder(
                                                    LocalContext.current
                                                ).data(reviewMedia?.get(index) ?: "").build(),
                                                contentDescription = null,
                                                imageLoader = LocalContext.current.imageLoader.newBuilder()
                                                    .logger(
                                                        DebugLogger()
                                                    ).build(),
                                                contentScale = ContentScale.FillBounds,
                                                modifier = Modifier
                                                    .maskClip(
                                                        MaterialTheme.shapes.extraSmall
                                                    )
                                                    .size(84.dp),
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
                                            )
                                        }
                                    }
                                }
                                if (it < reviews.itemCount - 1) HorizontalDivider()
                            }

                            when (reviews.loadState.append) {
                                is LoadState.Loading -> {
                                    item {
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

                                is LoadState.Error -> productReviewAndRatingViewModel.onLoadMoreProductReviewError()

                                is LoadState.NotLoading -> {}
                            }
                        }

                        if (productReviewAndRatingUiState.showRatingBottomSheet) RatingBottomSheet(
                            sheetState = ratingBottomSheetState,
                            onDismissRequest = { productReviewAndRatingViewModel.onRatingBottomSheetDismissRequest() },
                            checked = productReviewAndRatingUiState.ratingFilter,
                            onCheckedChange = {
                                productReviewAndRatingViewModel.onRatingBottomSheetCheckedChange(
                                    it
                                )
                                productReviewAndRatingViewModel.onRatingBottomSheetDismissRequest()
                            })

                        if (productReviewAndRatingUiState.showSortBottomSheet) SortBottomSheet(
                            sheetState = sortBottomSheetState,
                            onDismissRequest = { productReviewAndRatingViewModel.onSortBottomSheetDismissRequest() },
                            checked = productReviewAndRatingUiState.sortFilter,
                            onCheckedChange = {
                                productReviewAndRatingViewModel.onSortBottomSheetCheckedChange(
                                    it
                                )
                                productReviewAndRatingViewModel.onSortBottomSheetDismissRequest()
                            })
                    }
                }
            }
        }
    }
}