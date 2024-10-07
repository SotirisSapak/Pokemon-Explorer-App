package com.sotirisapak.libs.pokemonexplorer.core.lifecycle

import android.util.Log
import androidx.annotation.UiThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sotirisapak.libs.pokemonexplorer.core.extensions.clear
import com.sotirisapak.libs.pokemonexplorer.core.extensions.onBackground
import com.sotirisapak.libs.pokemonexplorer.core.extensions.onUiThread
import com.sotirisapak.libs.pokemonexplorer.core.extensions.set
import com.sotirisapak.libs.pokemonexplorer.core.models.ApiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Clean viewModel infrastructure based on [ViewModel]. Apart from naming, [ViewModelBase] inheritance
 * will provide some standard properties that are constantly be used across the entire application along
 * with some coroutine methods to quickly generate background jobs.
 * @author SotirisSapak
 * @since 1.0.0
 */
open class ViewModelBase: ViewModel() {

    /**
     * Some standard viewModel properties to hold different states
     * @author SotirisSapak
     * @since 1.0.0
     */
    inner class Props {

        /**
         * __Stated__
         *
         * <code>
         *
         *     // Usually attached in view class via dataBinding
         * </code>
         *
         * Property to use in order to show user that a job is pending
         * @author SotirisSapak
         * @since 1.0.0
         */
        val progress = MutableLiveData(false)

        /**
         * __Stated__
         *
         * <code>
         *
         *     // Usually attached in view class via dataBinding
         * </code>
         *
         * Property to use in order to show user that an error has been occurred
         * @author SotirisSapak
         * @since 1.0.0
         */
        val error = MutableLiveData("")

        /**
         * __Stated__
         *
         * <code>
         *
         *     // Usually observed from view class to go to next destination
         * </code>
         *
         * Property to use in order to show user that all actions is done and user want to proceed to
         * next available destination
         * @author SotirisSapak
         * @since 1.0.0
         */
        val proceed =  MutableLiveData(false)

        /**
         * A new job instance to hold current job
         * @author SotirisSapak
         * @since 1.0.0
         */
        var job: Job = Job()

    }

    /**
     * Standard viewModel properties class instance
     * @author SotirisSapak
     * @since 1.0.0
     * @see [Props]
     */
    val properties = Props()

    /**
     * Finish job unconditionally.
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun finishJobIfActive() {
        if(properties.job.isActive) {
            properties.job.cancel()
            properties.progress.clear()
        }
    }

    /**
     * Create a new job instance to execute on viewModel scope. Generated job will automatically be
     * saved to [Props.job]. If an exception occurs, then the [Props.error] property will be
     * notified if [notifyException] is enabled (false by default).
     * @param tag the job name
     * @param notifyException Flag to use in case the [action] throw an exception then the [Props.error]
     * will be notified and set the exception error message by default.
     * @param action the action to trigger
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun newJob(
        tag: String = TAG_JOB_NO_NAME,
        notifyException: Boolean = false,
        action: suspend (tag: String) -> Unit
    ) {
        finishJobIfActive()
        properties.job = viewModelScope.launch {
            try {
                action.invoke(tag)
            } catch (ex: Exception) {
                if(notifyException) properties.error.set(ex.message ?: "Unknown error")
            }
        }
    }

    /**
     * Create a new job instance to execute on viewModel scope and in background thread.
     * If an exception occurs, then the [Props.error] property will be notified if [notifyException]
     * is enabled (false by default).
     * @param tag the job name
     * @param notifyException Flag to use in case the [action] throw an exception then the [Props.error]
     * will be notified and set the exception error message by default.
     * @param action the action to trigger
     * @author SotirisSapak
     * @since 1.0.0
     * @see [newJob]
     */
    fun newBackgroundJob(
        tag: String = TAG_JOB_NO_NAME,
        notifyException: Boolean = false,
        action: suspend (tag: String) -> Unit
    ) {
        finishJobIfActive()
        properties.job = viewModelScope.launch {
            try {
                onBackground { action.invoke(tag) }
            } catch (ex: Exception) {
                if(notifyException) properties.error.set(ex.message ?: "Unknown error")
            }
        }
    }

    /**
     * Create a new background job based on [newBackgroundJob] and by using parameter methods, we can
     * create a [ApiResult] based job.
     *
     * Usage example:
     *
     * <code>
     *
     *     fun onFetch() = newResponseJob(
     *          tag = "tag-response-job",
     *          action = { sampleUseCase() },
     *          onProgress = { properties.progress.set(it) },
     *          onError = { properties.error.set(it) }
     *     ) { resultModel ->
     *          /* on success execute */
     *     }
     * </code>
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun <T: Any> newApiJob(
        tag: String = TAG_JOB_NO_NAME,
        action: suspend () -> ApiResult<T, String>,
        onProgress: (progress: Boolean) -> Unit = { properties.progress.set(it) },
        onError: (e: String) -> Unit = { properties.error.set(it) },
        after: (suspend (res: T) -> Unit)? = null,
        onSuccess: (res: T) -> Unit
    ) = newBackgroundJob(tag) {
        onUiThread { onProgress.invoke(true) }
        when(val response = action.invoke()) {
            is ApiResult.Failure -> onUiThread { onError.invoke(response.error) }
            is ApiResult.Success -> onUiThread {
                onSuccess.invoke(response.data)
                after?.invoke(response.data)
            }
        }
        onUiThread { onProgress.invoke(false) }
    }

    /**
     *
     * __View related method__
     *
     * <code>
     *
     *     // call it to your Ui state
     *     fun onBack() = viewModel.onBackPressed()
     * </code>
     * Since we want to go back to previous activity of fragment, then any pending job should be
     * terminated. So this method will offer this functionality out of the box. If you want to use
     * any other action along with job termination, you should override it to your own viewModel.
     * call any void method to be called when viewModel is about to be destroyed.
     * @author SotirisSapak
     * @since 1.0.0
     */
    @UiThread
    open fun onBackPressed(action: () -> Unit = {}) {
        finishJobIfActive()
        action.invoke()
    }

    companion object {

        /**
         * A tag for giving a quick tag for a new job to be saved in list if we want to avoid using
         * multiple jobs in our viewModel. If we want to only use sequential logic, then this tag
         * is automatically be used to avoid writing custom tags.
         * @author SotirisSapak
         * @since 1.0.0
         */
        const val TAG_JOB_NO_NAME = "unknown-job"

    }
    
}