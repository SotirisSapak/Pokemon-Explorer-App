package com.sotirisapak.libs.pokemonexplorer.di

import android.app.Application
import androidx.room.Room
import com.sotirisapak.libs.pokemonexplorer.backend.remote.PokemonApi
import com.sotirisapak.libs.pokemonexplorer.backend.local.databases.FavoriteDatabase
import com.sotirisapak.libs.pokemonexplorer.backend.local.services.FavoritesService
import com.sotirisapak.libs.pokemonexplorer.backend.remote.services.PokemonService
import com.sotirisapak.libs.pokemonexplorer.backend.remote.services.TypeService
import retrofit2.Retrofit

/**
 * The master application component to provide dependencies to each viewModel
 * @author SotirisSapak
 * @since 1.0.0
 */
class PokemonApplication: Application() {

    /**
     * The retrofit instance to provide api to services
     */
    private lateinit var retrofit: Retrofit

    /**
     * The service to provide api utilities for pokemon types
     */
    private lateinit var typeService: TypeService

    /**
     * The service to provide api utilities for pokemon
     */
    private lateinit var pokemonService: PokemonService

    /**
     * The service to provide access to local database in order to manipulate user's favorite pokemon
     */
    private lateinit var favoritesService: FavoritesService

    /**
     * Initialize all dependencies here when application is about to be created
     */
    override fun onCreate() {
        super.onCreate()
        // init retrofit instance
        retrofit = PokemonApi.instance
        // initialize all services
        typeService = TypeService(retrofit)
        pokemonService = PokemonService(retrofit)
        favoritesService = FavoritesService(initializeFavoritesDatabase())
    }

    /**
     * Method to initialize room database for favorites.
     * @return return a new [FavoriteDatabase] instance
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun initializeFavoritesDatabase() = Room.databaseBuilder(
        context = this,
        klass = FavoriteDatabase::class.java,
        name = "database-favorites"
    ).build()

    /**
     * Get the initialized [typeService] instance
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun getTypeService() = typeService

    /**
     * Get the initialized [pokemonService] instance
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun getPokemonService() = pokemonService

    /**
     * Get the initialized [favoritesService] instance
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun getFavoritesService() = favoritesService

}