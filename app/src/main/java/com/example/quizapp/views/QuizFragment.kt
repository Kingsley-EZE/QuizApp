package com.example.quizapp.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentQuizBinding
import com.example.quizapp.databinding.OptionItemBinding
import com.example.quizapp.viewmodel.QuizViewModel

class QuizFragment : Fragment() , OnClickListener{

    private var _binding : FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModel : QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)
        viewModel = ViewModelProvider(this)[QuizViewModel::class.java]

        setUpIndicators()
        setQuestion()

        binding.apply {
            optionOne.optionBox.setOnClickListener(this@QuizFragment)
            optionTwo.optionBox.setOnClickListener(this@QuizFragment)
            optionThree.optionBox.setOnClickListener(this@QuizFragment)
            optionFour.optionBox.setOnClickListener(this@QuizFragment)
            btnNext.setOnClickListener(this@QuizFragment)
            quitTv.setOnClickListener(this@QuizFragment)
        }

        viewModel.questionNumber.observe(viewLifecycleOwner) {
            binding.tvQuestionCount.text = getString(R.string.question_count, it.toString())
        }

        viewModel.buttonEnabled.observe(viewLifecycleOwner) {
            if (it){
                binding.btnNext.isEnabled = it
                binding.btnNext.setBackgroundResource(R.drawable.brand_button_bg)
            }else{
                binding.btnNext.isEnabled = it
                binding.btnNext.setBackgroundResource(R.drawable.disabled_button_bg)
            }

        }

        return binding.root
    }

    private fun setQuestion(){
        defaultOptionsView()
        val question = viewModel.getCurrentQuestion()
        binding.apply {
            tvQuestion.text = question.question
            optionOne.option.text = question.answers[0]
            optionTwo.option.text = question.answers[1]
            optionThree.option.text = question.answers[2]
            optionFour.option.text = question.answers[3]
        }
    }

    private fun defaultOptionsView(){
        val options = ArrayList<OptionItemBinding>()
        options.add(0, binding.optionOne)
        options.add(1, binding.optionTwo)
        options.add(2, binding.optionThree)
        options.add(3, binding.optionFour)
        for (option in options){
            option.optionBox.setBackgroundResource(R.drawable.option_box_bg)
            option.imgCheckIcon.setImageResource(R.drawable.uncheck_icon)
            option.option.setTextColor(resources.getColor(R.color.white))
        }
    }

    private fun selectedOptionsView(box: OptionItemBinding,selectedOption: Int){
        defaultOptionsView()
        viewModel.updateSelectedOptionPosition(selectedOption)
        box.optionBox.setBackgroundResource(R.drawable.selected_option_box)
        box.imgCheckIcon.setImageResource(R.drawable.check_icon)
        box.option.setTextColor(resources.getColor(R.color.green_color))
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.option_one ->{
                selectedOptionsView(binding.optionOne, 1)
                viewModel.setAnswer(1)
            }
            R.id.option_two ->{
                selectedOptionsView(binding.optionTwo, 2)
                viewModel.setAnswer(2)
            }
            R.id.option_three ->{
                selectedOptionsView(binding.optionThree, 3)
                viewModel.setAnswer(3)
            }
            R.id.option_four ->{
                selectedOptionsView(binding.optionFour, 4)
                viewModel.setAnswer(4)
            }
            R.id.btn_next ->{
               nextQuestion()
            }
            R.id.quit_tv -> {
                navController.navigate(R.id.action_quizFragment_to_welcomeFragment)
                navController.popBackStack()
            }
        }
    }

    private fun nextQuestion(){
        if (viewModel.moveToNextQuestion()){
            viewModel.updateSelectedOptionPosition(0)
            setQuestion()
        }else{
            val action = QuizFragmentDirections.actionQuizFragmentToResultFragment(viewModel.score.value!!)
            navController.navigate(action)
        }
    }

    private fun setUpIndicators(){
        viewModel.currentQuestionIndex.observe(viewLifecycleOwner){
            when(it){
                0 -> {
                    binding.level1.indicator.setBackgroundResource(R.drawable.progress_indicator_active)
                    binding.level2.indicator.setBackgroundResource(R.drawable.progress_indicator_inactive)
                    binding.level3.indicator.setBackgroundResource(R.drawable.progress_indicator_inactive)
                }
                1 -> {
                    binding.level1.indicator.setBackgroundResource(R.drawable.progress_indicator_active)
                    binding.level2.indicator.setBackgroundResource(R.drawable.progress_indicator_active)
                    binding.level3.indicator.setBackgroundResource(R.drawable.progress_indicator_inactive)
                }
                2 -> {
                    binding.level1.indicator.setBackgroundResource(R.drawable.progress_indicator_active)
                    binding.level2.indicator.setBackgroundResource(R.drawable.progress_indicator_active)
                    binding.level3.indicator.setBackgroundResource(R.drawable.progress_indicator_active)
                    binding.btnNext.text = "Submit"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}