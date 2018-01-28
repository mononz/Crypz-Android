package com.mononz.crypz.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.text.SimpleDateFormat


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
    Glide.with(context)
            .load(url)
            .apply(options)
            .into(this)
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