package edu.uw.ischool.xyou.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible

class QuizActivity :
    AppCompatActivity(),
    QuestionOverviewFragment.OnBeginButtonClickedListener,
    QuestionFragment.OnSubmitButtonClickedListener,
    AnswerFragment.OnNextButtonClickedListener
{
    private lateinit var topic: Topic
    private var currentQuestionIndex = 0
    private var correctAnswerCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val topicName = intent.getStringExtra("topicName").toString()

        // customize toolbar
        // setup toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.findViewById<TextView>(R.id.title).text = topicName
        // hide menu button
        toolbar.findViewById<TextView>(R.id.preferences).isVisible = false

        // grab topic data
        val app = application as QuizApp
        topic = app.topicRepository.getTopicByName(topicName)

        // create a fragment
        val fragment = QuestionOverviewFragment.newInstance(topicName)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, "QuestionOverviewFragment")
            .commit()
    }

    override fun onBeginButtonClicked() {
        val fragment = QuestionFragment.newInstance(topic.name, currentQuestionIndex)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, "QuestionFragment")
            .addToBackStack(null)
            .commit()
    }

    override fun onSubmitButtonClicked(selectedAnswer: Int) {
        if (selectedAnswer + 1 == topic.questions[currentQuestionIndex].correctAnswerIndex) {
            correctAnswerCount++
        }

        val fragment = AnswerFragment.newInstance(topic.name, correctAnswerCount, currentQuestionIndex, selectedAnswer)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, "AnswerFragment")
            .addToBackStack(null)
            .commit()

        currentQuestionIndex++
    }

    override fun onNextButtonClicked() {
        val fragment = QuestionFragment.newInstance(topic.name, currentQuestionIndex)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, "QuestionFragment")
            .addToBackStack(null)
            .commit()
    }
}