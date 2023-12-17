package com.sam.umain.data.model

import com.google.gson.annotations.SerializedName

data class FilterModel(
    var id: String? = null,
    @SerializedName("image_url")
    var imageUrl: String? = null,
    var name: String? = null,
    var isSelected : Boolean = false
)