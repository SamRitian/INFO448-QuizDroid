package edu.uw.ischool.xyou.quizdroid

import android.util.Log
import org.json.JSONArray

class InMemoryTopicRepository : TopicRepository {
    private val TAG = "InMemoryTopicRepository"
    private val topics: List<Topic> = initTopics()

    private fun initTopics(): List<Topic> {
        // read from assets/data/questions.json
        val jsonString = QuizApp.instance.assets.open("data/questions.json").bufferedReader().use { it.readText() }
        return parseQuizTopics(jsonString)
    }

    private fun parseQuizTopics(jsonString: String): List<Topic> {
        // handle topics
        val topics = mutableListOf<Topic>()
        val topicArray = JSONArray(jsonString)

        for (i in 0 until topicArray.length()) {
            val topicObject = topicArray.getJSONObject(i)

            val name = topicObject.getString("title")
            val shortDescription = topicObject.getString("desc")
            val longDescription = topicObject.getString("desc")

            // handle questions
            val questions = mutableListOf<Question>()
            val questionArray = topicObject.getJSONArray("questions")
            for (j in 0 until questionArray.length()) {
                val questionObject = questionArray.getJSONObject(j)

                val questionText = questionObject.getString("text")
                val correctAnswerIndex = questionObject.getInt("answer")

                // handle options
                val options = mutableListOf<String>()
                val optionsArray = questionObject.getJSONArray("answers")
                for (k in 0 until optionsArray.length()) {
                    options.add(optionsArray.getString(k))
                }

                questions.add(Question(questionText, options, correctAnswerIndex))
            }

            topics.add(Topic(name, shortDescription, longDescription, questions))
        }

        Log.i(TAG, "$topics")

        return topics
    }

    override fun getTopics(): List<Topic> = topics

    override fun getTopicByName(name: String): Topic {
        return topics.find { it.name == name } ?: throw IllegalArgumentException("No such topic")
    }
}