package com.project.tvshows_mvvm.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "tvShows")
class TVShow : Serializable {

    @PrimaryKey
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("start_date")
    var startDate: String? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("network")
    var network: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("image_thumbnail_path")
    var thumbnail: String? = null

}