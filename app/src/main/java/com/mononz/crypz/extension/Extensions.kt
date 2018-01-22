package com.mononz.crypz.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat
import java.text.NumberFormat
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

fun ImageView.loadUrlEmpty(url: String) {
    val options = RequestOptions()
            .placeholder(null)
            .error(null)
    Glide.with(context)
            .load(url)
            .apply(options)
            .into(this)
}