package br.edu.ufabc.estude_mais.model

//typealias Teachers = List<Teacher>

/**
 * A Teacher.
 * @property id the id
 * @property email the email
 * @property username the username
 * @property name the name
 * @property password the password
 * @property biography the biography
 * @property courses the list of courses
 */
data class Teacher(
    val id: Long,
    val email: String,
    val username: String,
    val name: String,
    val password: String,
    val biography: String,
    val courses: List<String>?
)
