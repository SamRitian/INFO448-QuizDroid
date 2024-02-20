package edu.uw.ischool.xyou.quizdroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TOPIC_NAME = "topicName"
private const val ARG_CORRECT_ANSWER_COUNT = "correctAnswerCount"
private const val ARG_CURRENT_QUESTION_INDEX = "currentQuestionIndex"
private const val ARG_SELECTED_OPTION = "selectedOption"

/**
 * A simple [Fragment] subclass.
 * Use the [AnswerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnswerFragment : Fragment() {
    private lateinit var rootView: View
    private var topicName: String? = null
    private var correctAnswerCount: Int? = null
    private var currentQuestionIndex: Int? = null
    private var selectedOption: Int? = null

    interface OnNextButtonClickedListener {
        fun onNextButtonClicked()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            topicName = it.getString(ARG_TOPIC_NAME)
            correctAnswerCount = it.getInt(ARG_CORRECT_ANSWER_COUNT)
            currentQuestionIndex = it.getInt(ARG_CURRENT_QUESTION_INDEX)
            selectedOption = it.getInt(ARG_SELECTED_OPTION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_answer, container, false)

        val app = activity?.application as QuizApp
        val topic = app.topicRepository.getTopicByName(topicName!!)
        val question = topic.questions[currentQuestionIndex!!]

        rootView.findViewById<TextView>(R.id.user_answer).text = getString(R.string.user_answer, question.options[selectedOption!!])
        rootView.findViewById<TextView>(R.id.correct_answer).text = getString(R.string.correct_answer, question.options[question.correctAnswerIndex - 1])
        rootView.findViewById<TextView>(R.id.performance).text = getString(R.string.performance, correctAnswerCount, topic.questions.size)

        val nextFinishButton = rootView.findViewById<Button>(R.id.next_finish_button)
        if (currentQuestionIndex == topic.questions.size - 1) {
            nextFinishButton.text = getString(R.string.next_finish_button, "Finish")
        } else {
            nextFinishButton.text = getString(R.string.next_finish_button, "Next")
        }
        nextFinishButton.setOnClickListener {
            if (currentQuestionIndex == topic.questions.size - 1) {
                activity?.finish()
            } else {
                val listener = activity as OnNextButtonClickedListener
                listener.onNextButtonClicked()
            }
        }

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(topicName: String, correctAnswerCount: Int, currentQuestionIndex: Int, selectedOption: Int) =
            AnswerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TOPIC_NAME, topicName)
                    putInt(ARG_CORRECT_ANSWER_COUNT, correctAnswerCount)
                    putInt(ARG_CURRENT_QUESTION_INDEX, currentQuestionIndex)
                    putInt(ARG_SELECTED_OPTION, selectedOption)
                }
            }
    }
}