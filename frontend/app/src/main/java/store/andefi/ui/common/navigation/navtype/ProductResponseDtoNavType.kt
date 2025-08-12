package store.andefi.ui.common.navigation.navtype

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.json.Json
import store.andefi.data.remote.dto.ProductResponseDto
import java.net.URLDecoder
import java.net.URLEncoder

object ProductResponseDtoNavType : NavType<ProductResponseDto>(isNullableAllowed = true) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: ProductResponseDto
    ) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): ProductResponseDto? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): ProductResponseDto {
        val decodedValue = URLDecoder.decode(value, "UTF-8")
        return Json.decodeFromString<ProductResponseDto>(decodedValue)
    }

    override fun serializeAsValue(value: ProductResponseDto): String {
        val jsonString = Json.encodeToString(value)
        return URLEncoder.encode(jsonString, "UTF-8")
    }
}