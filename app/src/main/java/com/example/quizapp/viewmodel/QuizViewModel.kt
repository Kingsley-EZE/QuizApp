package com.example.quizapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapp.model.Question

class QuizViewModel : ViewModel() {

    private val questions = mutableListOf<Question>()
    private var isCorrectAnswer = false
    private var answer = 0
    private var selectedOptionPosition = 0
    private var _currentQuestionIndex = MutableLiveData<Int>()
    val currentQuestionIndex : LiveData<Int>
        get() = _currentQuestionIndex

    private var _buttonEnabled = MutableLiveData<Boolean>()
    val buttonEnabled : LiveData<Boolean>
        get() = _buttonEnabled

    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    private var _questionNumber = MutableLiveData<Int>()
    val questionNumber : LiveData<Int>
        get() = _questionNumber


    init {
        questions.add(
            Question(
            question = "When was Chelsea FC founded?",
            answers = listOf("January 4, 1902", "March 10, 1905", "October 21, 1933", "June 12, 1982"),
            correctAnswer = 2,
        ))
        questions.add(
            Question(
                question = "What is Chelsea FC home stadium?",
                answers = listOf("Santiago Bernabeu", "Emirates", "Old Trafford", "Stamford Bridge"),
                correctAnswer = 4,
            ))
        questions.add(
            Question(
                question = "Who is Chelsea FC all-time leading goal scorer?",
                answers = listOf("Didier Drogba", "John Terry", "Frank Lampard", "Romelu Lukaku"),
                correctAnswer = 3,
            ))
        _currentQuestionIndex.value = 0
        _questionNumber.value = 1
        _score.value = 0
        _buttonEnabled.value = false
    }

    fun getCurrentQuestion(): Question {
        if (_currentQuestionIndex.value == 0){
            _score.value = 0
            _questionNumber.value = 1
        }
        return questions[_currentQuestionIndex.value!!]
    }

    fun updateSelectedOptionPosition(position: Int){
        _buttonEnabled.value = position > 0
        selectedOptionPosition = position
    }

    fun setAnswer(number : Int){
        answer = number
    }

    private fun onQuestionAnswered(){
        isCorrectAnswer = answer == questions[_currentQuestionIndex.value!!].correctAnswer
        if (isCorrectAnswer){
            _score.value = (_score.value)?.plus(1)
        }
    }

    fun moveToNextQuestion(): Boolean {
        onQuestionAnswered()
        if (_currentQuestionIndex.value!! < questions.size - 1 && selectedOptionPosition != 0) {
            _currentQuestionIndex.value = (_currentQuestionIndex.value)?.plus(1)
            _questionNumber.value = (_questionNumber.value)?.plus(1)
            return true
        }
        return false
    }

}