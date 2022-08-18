package br.edu.ufabc.estude_mais.model.room

import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.ForeignKey
import br.edu.ufabc.estude_mais.model.Activity
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CourseRoom::class,
            childColumns = ["courseId"],
            parentColumns = ["id"],
        )
    ]
)
data class ActivityRoom (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String,
    val creation: String,
    val deadline: Date?,
    val courseId: Long
){
    companion object {
        fun fromActivity(activity: Activity) = ActivityRoom(
            id = activity.id,
            name = activity.name,
            description = activity.description,
            creation = activity.creation,
            deadline = activity.deadline,
            courseId = activity.courseId,
        )
    }
}