package edu.uw.ischool.xyou.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import modal.Question
import modal.topics

class QuestionActivity : AppCompatActivity() {
    private var currentQuestionIndex = 0
    private lateinit var questions: List<Question>
    private var selectedAnswerPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val topicName = intent.getStringExtra("TOPIC_NAME")

        // retrieve passed values from AnswerActivity
        currentQuestionIndex = intent.getIntExtra("QUESTION_INDEX", 0)


        questions = topics.find { it.name == topicName }?.questions ?: listOf()

        if (questions.isNotEmpty()) {
            displayQuestion(currentQuestionIndex)
        }

        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.visibility = View.INVISIBLE // Initially invisible

        val answersGroup = findViewById<RadioGroup>(R.id.answers_group)
        answersGroup.setOnCheckedChangeListener { group, checkedId ->
            submitButton.visibility = View.VISIBLE // Show the button when an option is selected
            selectedAnswerPosition = group.indexOfChild(findViewById(checkedId))
        }

        submitButton.setOnClickListener {
            // Proceed to display answer and handle next steps
            val intent = Intent(this, AnswerActivity::class.java).apply {
                putExtra("TOPIC_NAME", topicName)
                putExtra("SELECTED_ANSWER", selectedAnswerPosition)
                putExtra("CORRECT_ANSWER", questions[currentQuestionIndex].correctAnswerIndex)
                putExtra("CURRENT_SCORE", intent.getIntExtra("CURRENT_SCORE", 0))
                putExtra("QUESTION_INDEX", currentQuestionIndex)
                putExtra("TOTAL_QUESTIONS", questions.size)
            }
            startActivity(intent)
        }
    }

    private fun displayQuestion(index: Int) {
        val question = questions[index]
        findViewById<TextView>(R.id.question_text).text = question.questionText
        val answersGroup = findViewById<RadioGroup>(R.id.answers_group)
        answersGroup.clearCheck() // Clear previous selections
        findViewById<RadioButton>(R.id.answer_1).text = question.options[0]
        findViewById<RadioButton>(R.id.answer_2).text = question.options[1]
        findViewById<RadioButton>(R.id.answer_3).text = question.options[2]
        findViewById<RadioButton>(R.id.answer_4).text = question.options[3]
    }
}