package store.andefi.ui.product.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.util.DebugLogger
import com.valentinilk.shimmer.shimmer
import store.andefi.R
import store.andefi.data.remote.dto.ProductResponseDto
import store.andefi.utility.toRupiahFormat
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@Composable
fun ProductCard(
    productResponseDto: ProductResponseDto
) {
    val now: Instant = Instant.now()
    val isProductDiscountValid =
        !now.isBefore(productResponseDto.discount.startDate)
                && !now.isAfter(productResponseDto.discount.endDate)
    val discountPrice =
        (100 - productResponseDto.discount.discountPercentage) * productResponseDto.price / 100

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(productResponseDto.media.urls["image"]?.split("|")?.first()).build(),
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
                        .height(186.dp)
                        .background(Color.LightGray)
                )
            },
            error = {
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .fillMaxWidth()
                        .height(186.dp),
                ) {
                    Icon(
                        painterResource(R.drawable.baseline_broken_image_24),
                        null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.Center)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(186.dp)
                .clip(MaterialTheme.shapes.medium)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                productResponseDto.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isProductDiscountValid) discountPrice.toRupiahFormat() else productResponseDto.price.toRupiahFormat(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
                if (isProductDiscountValid) {
                    Text(
                        productResponseDto.price.toRupiahFormat(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Normal,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        }
    }
}