package com.andariadar.newsapplication.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.andariadar.newsapplication.db.converters.Converters
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@TypeConverters(Converters::class)
@Parcelize
@Entity(tableName = "news")
data class News(

    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val title: String = "",
    val published_date: String = "",
    val section: String = "",
    @SerializedName("abstract") val shortNews: String = "",
    val byline: String = "",
    var media: List<Media>? = listOf(),
    val url: String = ""
): Parcelable {
    val mediaUrl get() = media!![0].mediaMetadata!![2].url!!
}