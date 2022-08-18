package br.edu.ufabc.estude_mais.model

import android.app.Application
import br.edu.ufabc.estude_mais.model.room.RepositoryRoom

/**
 * A repository factory.
 * @property application the application object
 */
class RepositoryFactory(private val application: Application) {

    /**
     * The types of repositories.
     */
    enum class Type {
        Room
    }

    /**
     * Create a new repository given its type.
     * @param type the repository type
     */
    fun create(type: Type = Type.Room) = when (type) {

        Type.Room -> RepositoryRoom(application)
    }
}
