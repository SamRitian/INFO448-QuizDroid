package edu.uw.ischool.xyou.quizdroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

private const val ARG_TOPIC_NAME = "topicName"
private const val ARG_CURRENT_QUESTION_INDEX = "currentQuestionIndex"

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionFragment : Fragment() {
    private lateinit var rootView: View
    private var selectedOption: Int? = null
    private var topicName: String? = null
    private var currentQuestionIndex: Int? = null

    interface OnSubmitButtonClickedListener {
        fun onSubmitButtonClicked(selectedAnswer: Int)
    }
    companion object {
        @JvmStatic fun newInstance(topicName: String, currentQuestionIndex: Int) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TOPIC_NAME, topicName)
                    putInt(ARG_CURRENT_QUESTION_INDEX, currentQuestionIndex)
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            topicName = it.getString(ARG_TOPIC_NAME)
            currentQuestionIndex = it.getInt(ARG_CURRENT_QUESTION_INDEX)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_question, container, false)

        val app = activity?.application as QuizApp
        val topic = app.topicRepository.getTopicByName(topicName!!)
        val question = topic.questions[currentQuestionIndex!!]

        // render question and options
        renderQuestion(question)

        // submit button
        val submitButton = rootView.findViewById<TextView>(R.id.submit_button)
        submitButton.isEnabled = false

        val optionGroup = rootView.findViewById<android.widget.RadioGroup>(R.id.option_group)
        optionGroup.setOnCheckedChangeListener { group, checkedId ->
            // enable submit button
            submitButton.isEnabled = true

            selectedOption = group.indexOfChild(group.findViewById(checkedId))
        }

        submitButton.setOnClickListener {
            val listener = activity as OnSubmitButtonClickedListener
            listener.onSubmitButtonClicked(selectedOption!!)
        }

        return rootView
    }

    private fun renderQuestion(question: Question) {
        rootView.findViewById<TextView>(R.id.question_text).text = question.questionText
        rootView.findViewById<TextView>(R.id.option1).text = question.options[0]
        rootView.findViewById<TextView>(R.id.option2).text = question.options[1]
        rootView.findViewById<TextView>(R.id.option3).text = question.options[2]
        rootView.findViewById<TextView>(R.id.option4).text = question.options[3]
    }
}