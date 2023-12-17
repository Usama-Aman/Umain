package com.sam.umain.domain.repository

import com.sam.umain.utils.NetworkResult
import com.sam.umain.data.model.FilterModel
import com.sam.umain.data.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface Repository {

    // Implementation of this is added in the Data package
    suspend fun getRestaurants(): NetworkResult<Pair<List<Restaurant?>, HashMap<String, FilterModel>>>

    suspend fun getFilterById(filterId : String): Flow<NetworkResult<FilterModel>>

    suspend fun getRestaurantStatus(restaurantId: String) : NetworkResult<Boolean>
}