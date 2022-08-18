package br.edu.ufabc.estude_mais.model

import java.text.SimpleDateFormat
import java.util.*

typealias Activities = List<Activity>

/**
 * A Activity.
 * @property id the id
 * @property name the name
 * @property description the description
 * @property creation the creation
 * @property deadline the deadline
 * @property courseId the courseId
 * @property students the list of students
 */
data class Activity(
    val id: Long,
    val name: String,
    val description: String,
    val creation: String,
    val deadline: Date?,
    val courseId: Long,
    val students: List<String>?
)  {
    /**
     * Format the deadline in shorthand notation.
     */
    fun formattedDeadline(): String = deadline?.let { formatDate(deadline) } ?: ""

    companion object {
        private val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)

        /**
         * Converts a string to a date.
         * @param date the string
         * @return the date
         */
        fun parseDate(date: String): Date? = format.parse(date)

        /**
         * Converts a date to a string.
         * @param date the date
         * @return the string
         */
        fun formatDate(date: Date): String = format.format(date)

    }
}

