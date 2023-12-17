package com.sam.umain.presentation.restaurants

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.umain.utils.NetworkResult
import com.sam.umain.data.model.FilterModel
import com.sam.umain.data.model.Restaurant
import com.sam.umain.domain.repository.Repository
import com.sam.umain.utils.SingleTimeObserverEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean>
        get() = _loading

    private val _restaurantFetched: MutableLiveData<Boolean> = MutableLiveData(false)
    val restaurantFetched: LiveData<Boolean>
        get() = _restaurantFetched

    private val _errorMessage = MutableLiveData(SingleTimeObserverEvent(""))
    val errorMessage: LiveData<SingleTimeObserverEvent<String>>
        get() = _errorMessage

    var filtersHashMap : HashMap<String,FilterModel> = HashMap()
    private val restaurants: MutableList<Restaurant> = ArrayList()
    val filteredRestaurants: MutableList<Restaurant> = ArrayList()

    /*
    * This method will return List of restaurants and a hashmap of filters
    * That hashmap will be used to find the filters name from the ids in each restaurant
    * */
    fun getRestaurants() {
        _loading.value = true
        restaurants.clear()
        viewModelScope.launch {
            when (val response = repository.getRestaurants()) {
                is NetworkResult.Success -> {
                    _loading.emit(false)

                    response.data?.first?.forEach {
                        if (it != null) {
                            restaurants.add(it)
                            filteredRestaurants.add(it)
                        }
                    }

                    response.data?.second?.let { filtersHashMap = it }

                    _restaurantFetched.value = true
                }

                is NetworkResult.Error -> {
                    _loading.emit(false)
                    _errorMessage.value = SingleTimeObserverEvent(response.message.toString())
                }
            }
        }
    }

    /*
    * Filtering the list on basis of filter ID
    * Setting the selected filter to true so it can be shown in the UI
    * */
    fun filterRestaurants(filterId: String, isSelected: Boolean = false) {
        filtersHashMap[filterId]?.isSelected = isSelected

        filteredRestaurants.clear()
        for (i in restaurants.indices) {
            var isContain = false
            filtersHashMap.forEach { (s, filterModel) ->
                if (filtersHashMap[s]?.isSelected == true)
                    if (restaurants[i].filterIds?.contains(filterModel.id) == true)
                        isContain = true
            }
            if (isContain)
                filteredRestaurants.add(restaurants[i])
        }

        if (filteredRestaurants.isEmpty()) { // It means nothing found, so returning full list
            filteredRestaurants.addAll(restaurants)
        }

        _restaurantFetched.value = true
    }

}

