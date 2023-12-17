package com.sam.umain.data.repository

import android.util.Log
import com.google.gson.Gson
import com.sam.umain.data.model.FilterModel
import com.sam.umain.data.model.Restaurant
import com.sam.umain.data.model.RestaurantsModel
import com.sam.umain.data.network.ApiService
import com.sam.umain.domain.repository.Repository
import com.sam.umain.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import java.net.UnknownHostException

class RepositoryImpl(
    private val apiService: ApiService,
) : Repository, BaseApiResponse() {

    override suspend fun getRestaurants(): NetworkResult<Pair<List<Restaurant?>, HashMap<String, FilterModel>>> {

        val response = safeApiCall {
            apiService.getRestaurants()
        }

        return if (response.data != null) {
            val data = Gson().fromJson(response.data.toString(), RestaurantsModel::class.java)

            // Created the hashmap to separate the filter IDs
            // Used the linked hashmap to keep the item sorted as they were entered
            val hashMap: HashMap<String, FilterModel> = LinkedHashMap<String, FilterModel>()
            data.restaurants.forEach { restaurant ->
                restaurant.filterIds?.forEach { filter ->
                    hashMap[filter.toString()] = FilterModel()
                }
            }

            hashMap.forEach { (key, _) ->
                getFilterById(key).collect {
                    when (it) {
                        is NetworkResult.Success -> {
                            val filter = it.data!!
                            hashMap[filter.id.toString()] = filter
                        }

                        is NetworkResult.Error -> {
                            Log.d("Filter ID Error", it.message.toString())
                        }
                    }
                }
            }

            NetworkResult.Success(Pair(data.restaurants, hashMap))
        } else {
            NetworkResult.Error(response.message)
        }
    }

    override suspend fun getFilterById(filterId: String): Flow<NetworkResult<FilterModel>> = flow {

        val response = safeApiCall {
            apiService.getFilterById(filterId)
        }

        if (response.data != null) {
            val data = Gson().fromJson(response.data.toString(), FilterModel::class.java)
            emit(NetworkResult.Success(data))
        } else {
            emit(NetworkResult.Error(response.message))
        }

    }

    override suspend fun getRestaurantStatus(restaurantId: String): NetworkResult<Boolean> {
        val response = safeApiCall {
            apiService.getRestaurantStatus(restaurantId)
        }

        return if (response.data != null) {

            val json = JSONObject(response.data.toString())
            val status = if (json.has("is_currently_open"))
                json.getBoolean("is_currently_open")
            else
                false
            NetworkResult.Success(status)
        } else {
            NetworkResult.Error(response.message)
        }
    }
}

// Abstract Safe api is added in case there is an error, such that time out or No Internet
abstract class BaseApiResponse {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return if (e is UnknownHostException)
                error("No Internet Connection")
            else
                error(e.message ?: e.toString())
        }
    }

    private fun <T> error(errorMessage: String): NetworkResult<T> =
        NetworkResult.Error(errorMessage)
}