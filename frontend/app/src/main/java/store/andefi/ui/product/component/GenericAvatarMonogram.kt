package store.andefi.ui.product.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import store.andefi.utility.toHslColor

@Composable
fun GenericAvatarMonogram(
    id: String,
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier.Companion,
    size: Dp = 48.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    Box(modifier.size(size), contentAlignment = Alignment.Companion.Center) {
        val color = remember(id, firstName, lastName) {
            val name = listOf(firstName, lastName).joinToString(separator = "").uppercase()
            Color("$id / $name".toHslColor())
        }
        val initials = (firstName.take(1) + lastName.take(1)).uppercase()
        Canvas(modifier = Modifier.Companion.size(30.dp)) {
            drawCircle(SolidColor(color))
        }
        Text(
            text = initials,
            style = textStyle,
            fontWeight = FontWeight.Companion.Bold,
            color = Color.Companion.White
        )
    }
}