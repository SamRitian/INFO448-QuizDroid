package edu.uw.ischool.xyou.quizdroid

interface TopicRepository {
    fun getTopics(): List<Topic>
    fun getTopicByName(name: String): Topic
}