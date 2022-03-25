package com.example.irestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.irestaurant.databinding.FragmentPictureBinding
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PICTURE = "picture"

/**
 * A simple [Fragment] subclass.
 * Use the [PictureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PictureFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var picture: String? = null
    private lateinit var binding: FragmentPictureBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            picture = it.getString(ARG_PICTURE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPictureBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.get().load(picture?.ifEmpty { null })
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.imageViewFrag)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PictureFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(picture: String) =
            PictureFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PICTURE, picture)
                }
            }
    }
}