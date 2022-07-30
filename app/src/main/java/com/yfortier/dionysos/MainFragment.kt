package com.yfortier.dionysos

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.yfortier.dionysos.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

	private var _binding: FragmentMainBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

		_binding = FragmentMainBinding.inflate(inflater, container, false)
		return binding.root

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Picasso.get().load("https://images.unsplash.com/photo-1511795409834-ef04bbd61622").fit().into(binding.imageViewEvenement);


	}

    override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}