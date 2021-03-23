package br.com.pedrotlf.desafioshippmobile.utils

import android.app.Activity
import android.content.Context
import android.util.TypedValue

fun Context.dipFromPixels(dip: Float) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        resources.displayMetrics
    ).toInt()

/**
 * Should use on "when" statements. It prevents compilation to succeed when your statement is not
 * covering all conditions.
 */
val <T> T.exhaustive: T
    get() = this

const val ADD_CARD_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_CARD_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val SELECT_CARD_RESULT_OK = Activity.RESULT_FIRST_USER + 2