package com.example.quizapp.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentResultBinding


class ResultFragment : Fragment() {

    private var _binding : FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val args : ResultFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)

        when{
            args.score <= 1 -> {
                binding.apply {
                    resultImg.setImageResource(R.drawable.sad_icon)
                    tvGreetings.text = "Yikes man"
                    tvMessage.text = "You did poor, play again"
                    resultTv.text = "${args.score}"
                    resultTv.setTextColor(resources.getColor(R.color.red_color))
                }
            }
            args.score >= 2 -> {
                binding.apply {
                    resultImg.setImageResource(R.drawable.trophy_icon)
                    tvGreetings.text = "Congratulations"
                    tvMessage.text = "You did good, play again"
                    resultTv.text = "${args.score}"
                    resultTv.setTextColor(resources.getColor(R.color.green_color))
                }
            }
        }

        binding.btnPlay.setOnClickListener {
            navController.navigate(R.id.action_resultFragment_to_welcomeFragment)
            navController.popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}