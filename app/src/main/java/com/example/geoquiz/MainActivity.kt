package com.example.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0
class MainActivity : AppCompatActivity() {
    private val quizViewModel : QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }
    private lateinit var trueButton : Button
    private lateinit var falseButton : Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton : Button
     lateinit var questionTextView : TextView

    private var countQuestion = 0.0
     var countStep = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX,0) ?: 0
        quizViewModel.currentIndex = currentIndex
        trueButton = findViewById<Button>(R.id.true_button)
        falseButton = findViewById<Button>(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)

        trueButton.setOnClickListener {
         checkAnswer(true)
            falseButton.isEnabled = false
            trueButton.isEnabled = false
        }
        cheatButton.setOnClickListener {view ->
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // val intent = Intent(this, CheatActivity::class.java)
                val answerIsTrue = quizViewModel.currentQuestion
                val intent = CheatActivity.newIntent(this, answerIsTrue)
                val options = ActivityOptions
                        .makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            }else{
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }


            falseButton.setOnClickListener {
            checkAnswer(false)
            falseButton.isEnabled = false
            trueButton.isEnabled = false
        }
        nextButton.setOnClickListener {
            if(countStep < 5) {
               quizViewModel.moveToNext()
                falseButton.isEnabled = true
                trueButton.isEnabled = true
                countStep++
                upDateQuestion()
            }
            else{
                if(countQuestion != 0.0){
                    countQuestion += 1
                }
                questionTextView.text = "${countQuestion.toInt()}"
            }
            }
        upDateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOW, false) ?: false
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    private fun upDateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnswer : Boolean){
        val correctAnswer = quizViewModel.currentQuestion
        if(userAnswer == correctAnswer) {
            countQuestion += 16.6
        }
        val messageResI= when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast

            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResI, Toast.LENGTH_SHORT).show()

    }
}