package edu.uw.ischool.xyou.quizdroid

import org.junit.Test
import org.junit.Before
import org.junit.Assert.*

class TopicRepositoryTest {

    private lateinit var repository: InMemoryTopicRepository

    @Before
    fun setUp() {
        repository = InMemoryTopicRepository()
    }

    @Test
    fun `getTopics returns all topics` () {
        val topics = repository.getTopics()
        assertEquals(4, topics.size)
    }

    @Test
    fun `getTopicByName returns the correct topic`() {
        val topic = repository.getTopicByName("Math")
        assertNotNull(topic)
        assertEquals("Math", topic.name)
    }
}