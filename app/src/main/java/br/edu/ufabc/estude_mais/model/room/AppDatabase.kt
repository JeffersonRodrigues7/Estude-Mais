package br.edu.ufabc.estude_mais.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * The Room database.
 */
@Database(
    entities = [TeacherRoom::class, StudentRoom::class, CourseRoom::class, ActivityRoom::class, StudentCourse::class, StudentActivity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * The teacherDao DAO.
     */
    abstract fun teacherDao(): TeacherDao

    /**
     * The studentDao DAO.
     */
    abstract fun studentDao(): StudentDao

    /**
     * The courseDao DAO.
     */
    abstract fun courseDao(): CourseDao

    /**
     * The activityDao DAO.
     */
    abstract fun activityDao(): ActivityDao

    /**
     * The studentCourseDao (join table) DAO.
     */
    abstract fun studentCourseDao(): StudentCourseDao

    /**
     * The studentActivityDao (join table) DAO.
     */
    abstract fun studentActivityDao(): StudentActivityDao
}