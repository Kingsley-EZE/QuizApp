package com.example.quizapp.model

data class Question (
    val question: String,
    val answers: List<String>,
    val correctAnswer: Int
    )