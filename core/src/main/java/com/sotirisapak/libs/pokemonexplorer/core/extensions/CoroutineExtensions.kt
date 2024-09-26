package com.sotirisapak.libs.pokemonexplorer.core.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Execute any block of commands in coroutine background thread using [withContext] method.
 * @param block the block of commands to execute in background
 * @author SotirisSapak
 * @since 0.0.1
 */
suspend fun <T> onBackground(block: suspend CoroutineScope.() -> T){
    withContext(Dispatchers.IO){ block.invoke(this) }
}

/**
 * Execute any block of commands in coroutine ui thread using [withContext] method.
 * @param block the block of commands to execute in foreground (ui thread)
 * @author SotirisSapak
 * @since 0.0.1
 */
suspend fun <T> onUiThread(block: suspend CoroutineScope.() -> T){
    withContext(Dispatchers.Main) { block.invoke(this) }
}