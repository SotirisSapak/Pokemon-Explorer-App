package com.sotirisapak.libs.pokemonexplorer.backend.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Extension method to create a new object [T] from a [json] string.
 * @param json the json string to be parsed
 * @return a new [T] parsed instance
 * @author SotirisSapak
 * @since 1.0.0
 */
inline fun <reified T: Any> Gson.fromJson(json: String): T = fromJson(json, object : TypeToken<T>() {}.type)