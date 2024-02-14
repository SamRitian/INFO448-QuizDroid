package edu.uw.ischool.xyou.quizdroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionOverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionOverviewFragment : Fragment() {

    interface OnBeginButtonClickedListener {
        fun onBeginButtonClicked()
    }
    companion object {
        val TOPIC_NAME = "topicName"
        fun newInstance(topicName: String): QuestionOverviewFragment {
            val args = Bundle().apply {
                putString(TOPIC_NAME, topicName)
            }

            val thisFragment = QuestionOverviewFragment()
            thisFragment.arguments = args

            return thisFragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_question_overview, container, false)

        val longDescrTextView = rootView.findViewById<TextView>(R.id.topic_long_description)
        val questionCountTextView = rootView.findViewById<TextView>(R.id.topic_question_count)
        val beginButton = rootView.findViewById<Button>(R.id.begin)

        val app = activity?.application as QuizApp
        arguments?.let {
            val topicName = it.getString(TOPIC_NAME)
            if (topicName != null) {
                val topic = app.topicRepository.getTopicByName(topicName)
                longDescrTextView.text = topic.longDescription
                questionCountTextView.text = getString(R.string.question_count, topic.questions.size)
            }
        }

        beginButton.setOnClickListener {
            val listener = activity as OnBeginButtonClickedListener
            listener.onBeginButtonClicked()
        }

        return rootView
    }
}