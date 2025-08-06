package store.andefi.ui.product.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.SubcomposeAsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.util.DebugLogger
import com.valentinilk.shimmer.shimmer
import store.andefi.R
import store.andefi.ui.product.viewmodel.ProductSpecificationsAndDescriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSpecificationsAndDescriptionScreen(
    productSpecificationsAndDescriptionViewModel: ProductSpecificationsAndDescriptionViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {}
) {
    val productSpecificationsAndDescriptionUiState by productSpecificationsAndDescriptionViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
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
            }, title = { Text("Spesifikasi & deskripsi produk") })
        },
        contentWindowInsets = WindowInsets.safeContent
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState(0))
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(
                            productSpecificationsAndDescriptionUiState.product?.media?.urls["image"]?.split(
                                "|"
                            )?.first()
                        ).build(),
                    imageLoader = LocalContext.current.imageLoader.newBuilder().logger(
                        DebugLogger()
                    ).build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(MaterialTheme.shapes.extraSmall),
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
                                .background(Color.LightGray)
                                .size(72.dp),
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
                )
                Text(
                    productSpecificationsAndDescriptionUiState.product?.name ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    "Spesifikasi produk",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
                if (productSpecificationsAndDescriptionUiState.product!!.attributes.isNotEmpty()) {
                    val lastAttribute =
                        productSpecificationsAndDescriptionUiState.product!!.attributes.keys.lastOrNull()

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for ((key, value) in productSpecificationsAndDescriptionUiState.product!!.attributes) {
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
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    "Deskripsi produk", style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    productSpecificationsAndDescriptionUiState.product?.description ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}