package com.dicoding.myrecyclerview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hero(
    val name: String,
    val description: String,
    val photo: Int,
    val role: String,
    val origin: String,
    val bg: Int,
    val tamnel: String,
    val videoResId: Int,
    val deskripsividio: String,
) : Parcelable