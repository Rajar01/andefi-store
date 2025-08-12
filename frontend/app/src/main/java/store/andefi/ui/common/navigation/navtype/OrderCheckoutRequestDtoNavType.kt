package store.andefi.ui.common.navigation.navtype

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.json.Json
import store.andefi.data.remote.dto.OrderCheckoutRequestDto
import store.andefi.data.remote.dto.OrderCheckoutResponseDto
import java.net.URLDecoder
import java.net.URLEncoder

object OrderCheckoutRequestDtoNavType : NavType<OrderCheckoutRequestDto>(isNullableAllowed = true) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: OrderCheckoutRequestDto
    ) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): OrderCheckoutRequestDto? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): OrderCheckoutRequestDto {
        val decodedValue = URLDecoder.decode(value, "UTF-8")
        return Json.decodeFromString<OrderCheckoutRequestDto>(decodedValue)
    }

    override fun serializeAsValue(value: OrderCheckoutRequestDto): String {
        val jsonString = Json.encodeToString(value)
        return URLEncoder.encode(jsonString, "UTF-8")
    }
}