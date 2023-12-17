package com.sam.umain.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantsModel(
    var restaurants: List<Restaurant> = ArrayList()
)

data class Restaurant(
    @SerializedName("delivery_time_minutes")
    var deliveryTimeMinutes: Int? = null,
    var filterIds: List<String?>? = null,
    var id: String? = null,
    @SerializedName("image_url")
    var imageUrl: String? = null,
    var name: String? = null,
    var rating: Double? = null
)

