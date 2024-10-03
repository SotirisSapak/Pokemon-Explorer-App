package com.sotirisapak.libs.pokemonexplorer.backend

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sotirisapak.libs.pokemonexplorer.backend.remote.PokemonApi
import com.sotirisapak.libs.pokemonexplorer.backend.requests.Base
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApiObjectTests {

    @Test
    fun testStandardCall() = runBlocking {
        val instance = PokemonApi.instance.create(Base::class.java)
        val response = instance.basic()
        Log.d("test1", response.raw().message())
    }
    
}