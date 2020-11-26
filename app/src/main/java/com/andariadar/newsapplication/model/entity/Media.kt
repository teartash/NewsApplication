package com.andariadar.newsapplication.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Media(
    @field:SerializedName("media-metadata") val mediaMetadata: List<MediaMetadata>? = listOf()
) : Parcelable