package br.com.pedrotlf.desafioshippmobile.utils

import android.content.Context
import android.util.TypedValue

fun Context.dipFromPixels(dip: Float) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        resources.displayMetrics
    ).toInt()

val <T> T.exhaustive: T
    get() = this