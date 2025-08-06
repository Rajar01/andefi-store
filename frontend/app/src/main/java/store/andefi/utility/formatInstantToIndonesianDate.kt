package store.andefi.utility

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.BAKLAVA)
fun formatInstantToIndonesianDate(instant: Instant): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        .withLocale(Locale.of("id", "ID"))
        .withZone(ZoneId.of("Asia/Jakarta"))

    return formatter.format(instant)
}