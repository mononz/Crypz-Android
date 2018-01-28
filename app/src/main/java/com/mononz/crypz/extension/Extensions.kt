package com.mononz.crypz.extension

import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mononz.crypz.library.svg.SvgSoftwareLayerSetter
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Double.pricify() : String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(this)
}

fun Double.pricify2() : String {
    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    val symbols = formatter.decimalFormatSymbols
    symbols.groupingSeparator = ','
    formatter.decimalFormatSymbols = symbols
    return "$" + formatter.format(this)
}

fun Double.thousands() : String {
    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    val symbols = formatter.decimalFormatSymbols
    symbols.groupingSeparator = ','
    formatter.decimalFormatSymbols = symbols
    return formatter.format(this)
}

fun ImageView.loadUrl(url: String?, placeholder : Int) {
    val options = RequestOptions()
            .placeholder(placeholder)
            .error(null)

    if (isSvgImage(url)) {
        Glide.with(context)
                .`as`(PictureDrawable::class.java)
                .apply(options)
                .listener(SvgSoftwareLayerSetter())
                .load(url).into(this)
    } else {
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(this)
    }
}

private fun isSvgImage(url: String?): Boolean {
    return url != null && url.endsWith(".svg")
}

fun ImageView.loadUrl(url: String?) {
    val options = RequestOptions()
            .placeholder(null)
            .error(null)
    Glide.with(context)
            .load(url)
            .apply(options)
            .into(this)
}

fun Date.newUtc() : String {
    val calendar = Calendar.getInstance()
    calendar.timeZone = TimeZone.getTimeZone("UTC")
    calendar.time = this

    val time = calendar.time
    val outputFmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'", Locale.US)
    return outputFmt.format(time)
}