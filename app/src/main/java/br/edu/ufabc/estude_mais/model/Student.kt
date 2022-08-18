package br.edu.ufabc.estude_mais.model


typealias Students = List<Student>

/**
 * A Student.
 * @property id the id
 * @property email the email
 * @property username the username
 * @property name the name
 * @property password the password
 * @property biography the biography
 * @property experience the experience
 * @property allowRankingVisualization the allowRankingVisualization
 * @property activitiesDelivered the activitiesDelivered
 * @property coursesEntered the coursesEntered
 * @property coursesCompleted the coursesCompleted
 * @property fullMark the fullMark
 * @property topOne the topOne
 * @property courses the list of courses
 * @property activities the list of activities
 */
data class Student(
    val id: Long,
    val email: String,
    val username: String,
    val name: String,
    val password: String,
    val biography: String,
    val experience: Long,
    val allowRankingVisualization: Boolean,
    val activitiesDelivered: Long,
    val coursesEntered: Long,
    val coursesCompleted: Long,
    val fullMark: Long,
    val topOne: Long,
    val courses: List<String>?,
    val activities: List<String>?
)
