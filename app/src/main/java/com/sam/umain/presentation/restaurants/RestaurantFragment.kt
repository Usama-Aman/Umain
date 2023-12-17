package com.sam.umain.presentation.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sam.umain.R
import com.sam.umain.databinding.FragmentRestaurantsBinding
import com.sam.umain.presentation.restaurant_detail.RestaurantDetailFragment
import com.sam.umain.utils.ConnectionLiveData
import com.sam.umain.utils.Utility
import com.sam.umain.utils.viewGone
import com.sam.umain.utils.viewVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestaurantFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantsBinding
    private val viewModel: RestaurantsViewModel by viewModels()

    // Live Data to observe internet change (additional)
    private val connectionLiveData by lazy {
        ConnectionLiveData(requireContext())
    }

    private val restaurantsAdapter by lazy {
        RestaurantsAdapter { position, tags , view->

            ViewCompat.setTransitionName(view, "restaurantImage")
            Utility.replaceFragment(
                activity = requireActivity() as AppCompatActivity,
                fragmentContainer = R.id.fragmentContainer,
                fragment = RestaurantDetailFragment.newInstance(
                    restaurantId = viewModel.filteredRestaurants[position].id!!,
                    restaurantName = viewModel.filteredRestaurants[position].name!!,
                    restaurantImage = viewModel.filteredRestaurants[position].imageUrl!!,
                    restaurantTags = tags
                ),
                isBackStack = true,
                sharedView = view,
                elementName = "restaurantImage"
            )
        }
    }
    private val filterAdapter by lazy {
        FiltersAdapter { filterId ->
            viewModel.filterRestaurants(
                filterId = filterId,
                isSelected = !viewModel.filtersHashMap[filterId]?.isSelected!!
            )
        }
    }

    companion object {
        fun newInstance(): RestaurantFragment {
            return RestaurantFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRestaurantsBinding.inflate(layoutInflater, container, false)

        initAdapter()
        initObservers()

        if (viewModel.filteredRestaurants.isEmpty())
            viewModel.getRestaurants()

        return binding.root
    }

    /*
    * Setting up the adapters
    * */
    private fun initAdapter() {
        binding.rvRestaurants.adapter = restaurantsAdapter
        binding.rvFilters.adapter = filterAdapter
    }

    /*
    * Handling the observers from the view model
    * */
    private fun initObservers() {
        // Observing the live data in case to show Internet error
        // Here, I am just disabling the button
        // It is an inherited class to Live Data, you can find it in Utils
        connectionLiveData.observe(viewLifecycleOwner) {
            if (it && viewModel.errorMessage.value?.peekContent() == "No Internet Connection")
                if (viewModel.filteredRestaurants.isEmpty())
                    viewModel.getRestaurants()
        }
        viewModel.restaurantFetched.observe(viewLifecycleOwner) {
            if (it) {
                restaurantsAdapter.setData(viewModel.filteredRestaurants, viewModel.filtersHashMap)
                filterAdapter.setData(viewModel.filtersHashMap)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loading.collect {
                    if (it)
                        binding.progressBar.viewVisible()
                    else
                        binding.progressBar.viewGone()
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled().let {
                if (!it.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

}