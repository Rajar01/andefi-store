package store.andefi.utility


import android.os.Build
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
fun Number.toRupiahFormat(): String {
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale.of("in", "ID"))
    formatRupiah.maximumFractionDigits = 0
    return formatRupiah.format(this)
}