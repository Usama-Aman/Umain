package com.sam.umain.presentation.restaurant_detail

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.umain.domain.repository.Repository
import com.sam.umain.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _errorMessage: MutableLiveData<String> = MutableLiveData("")
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _statusReceived: MutableLiveData<Boolean> = MutableLiveData()
    val statusReceived: LiveData<Boolean>
        get() = _statusReceived

    var restaurantId = ObservableField("")
    var restaurantName = ObservableField("")
    var restaurantTags = ObservableField("")
    var restaurantImage = ""

    /*
    * This method will return the status of the given restaurant
    * */
    fun getRestaurantStatus() {

            viewModelScope.launch {
                when (val response = repository.getRestaurantStatus(restaurantId.get() ?: "")) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            _statusReceived.value = it
                        }
                    }

                    is NetworkResult.Error -> {
                        _errorMessage.value = response.message ?: ""
                    }
                }
            }

    }

}