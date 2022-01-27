package com.project.tvshows_mvvm.models

import com.google.gson.annotations.SerializedName

class TVShowDetails {

    @SerializedName("url")
    val url: String? = null

    @SerializedName("description")
    val description: String? = null

    @SerializedName("runtime")
    val runtime: String? = null

    @SerializedName("image_path")
    val imagePath: String? = null

    @SerializedName("rating")
    val rating: String? = null

    @SerializedName("genres")
    val genres: Array<String>? = null

    @SerializedName("pictures")
    val pictures: Array<String>? = null

    @SerializedName("episodes")
    val episodes: ArrayList<Episode>? = null
}