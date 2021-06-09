package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel



private const val TAG = "QuizViewModel"
class QuizViewModel : ViewModel() {
    var mainActivity = MainActivity()

   var currentIndex = 0
    var isCheater = false
    private val questionBank = mutableListOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    val currentQuestion: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        if(currentIndex < 5) {
            currentIndex = (currentIndex + 1) % questionBank.size
        }else{
            mainActivity.questionTextView.text = "${mainActivity.countStep}"

        }
    }

}

