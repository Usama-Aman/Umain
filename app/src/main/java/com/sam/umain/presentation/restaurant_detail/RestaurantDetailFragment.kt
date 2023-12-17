package com.sam.umain.presentation.restaurant_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.sam.umain.R
import com.sam.umain.databinding.FragmentRestaurantDetailBinding
import com.sam.umain.utils.viewGone
import com.sam.umain.utils.viewVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantDetailFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantDetailBinding
    private val viewModel: RestaurantDetailViewModel by viewModels()

    companion object {

        private const val RESTAURANT_ID = "restaurantId"
        private const val RESTAURANT_NAME = "restaurantName"
        private const val RESTAURANT_IMAGE = "restaurantImage"
        private const val RESTAURANT_TAGS = "restaurantTags"

        fun newInstance(
            restaurantId: String,
            restaurantName: String,
            restaurantImage: String,
            restaurantTags: String,
        ): RestaurantDetailFragment {
            return RestaurantDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(RESTAURANT_ID, restaurantId)
                    putString(RESTAURANT_NAME, restaurantName)
                    putString(RESTAURANT_IMAGE, restaurantImage)
                    putString(RESTAURANT_TAGS, restaurantTags)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRestaurantDetailBinding.inflate(layoutInflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.restaurantId.set(arguments?.getString(RESTAURANT_ID) ?: "")
        viewModel.restaurantName.set(arguments?.getString(RESTAURANT_NAME) ?: "")
        viewModel.restaurantTags.set(arguments?.getString(RESTAURANT_TAGS) ?: "")
        viewModel.restaurantImage = arguments?.getString(RESTAURANT_IMAGE) ?: ""

        initViews()
        initListeners()
        initObservers()

        viewModel.getRestaurantStatus()

        return binding.root
    }

    private fun initViews() {
        binding.ivRestaurant.load(viewModel.restaurantImage) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
        }

        if (viewModel.restaurantTags.get().toString().isEmpty())
            binding.tvSubtitle.viewGone()
        else
            binding.tvSubtitle.viewVisible()

    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initObservers() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                Toast.makeText(
                    requireContext(),
                    it.toString(),
                    Toast.LENGTH_SHORT
                ).show()
        }

        viewModel.statusReceived.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvStatus.text = requireContext().getText(R.string.restaurantStatusOpen)
                binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.positiveColor
                    )
                )
            } else {
                binding.tvStatus.text = requireContext().getText(R.string.restaurantStatusClose)
                binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.negativeColor
                    )
                )
            }
        }
    }

}