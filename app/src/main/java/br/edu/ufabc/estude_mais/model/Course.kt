package br.edu.ufabc.estude_mais.model

typealias Courses = List<Course>

/**
 * A Course.
 * @property id the id
 * @property name the name
 * @property password the password
 * @property description the description
 * @property creation the creation
 * @property teacherId the teacherId
 * @property students the list of students
 * @property activities the list of activities
 */
data class Course(
    val id: Long,
    val name: String,
    val password: String,
    val description: String,
    val creation: String,
    val teacherId: Long,
    val students: List<String>?,
    val activities: List<String>?
)

