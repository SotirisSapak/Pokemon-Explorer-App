package com.sotirisapak.libs.pokemonexplorer.core.lifecycle

import android.util.Log
import androidx.annotation.UiThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
         * A map to hold all the jobs that are running in [viewModelScope]
         * @author SotirisSapak
         * @since 1.0.0
         */
        val jobs = mutableMapOf<String, Job>()

    }

    /**
     * Standard viewModel properties class instance
     * @author SotirisSapak
     * @since 1.0.0
     * @see [Props]
     */
    val properties = Props()

    /**
     * Finish a pending job filtered by given [tag]. If you use sequential logic with no more than
     * one job at a time then leave tag as is.
     * @param tag the job tag name to finish
     * @author SotirisSapak
     * @since 1.0.0
     */
    @UiThread
    fun finishJobByTag(tag: String = TAG_JOB_NO_NAME) {
        properties.jobs.forEach { (key, job) ->
            if(tag == key) {
                // check if this job is active
                if(job.isActive) {
                    job.cancel()
                    return@forEach
                }
            }
        }
        // remove job from list
        properties.jobs.remove(tag)
    }

    /**
     * Finish all pending jobs unconditionally.
     * @author SotirisSapak
     * @since 1.0.0
     */
    @UiThread
    fun finishAllJobs() = properties.jobs.forEach { (key, _) -> finishJobByTag(key) }

    /**
     * Create a new job instance to execute on viewModel scope. Generated job will automatically be
     * saved to [Props.jobs] map with [tag] as job name and the actual [Job] as value. If there is
     * any job active with same [tag] then will be finished first, and re-created.
     *
     * If an exception occurs, then the [Props.error] property will be notified if [notifyException]
     * is enabled (true by default).
     * @param tag the job name
     * @param notifyException Flag to use in case the [action] throw an exception then the [Props.error]
     * will be notified and set the exception error message by default.
     * @param action the action to trigger
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun newJob(
        tag: String = TAG_JOB_NO_NAME,
        notifyException: Boolean = true,
        action: suspend () -> Unit
    ) {
        Log.d(tag, "Terminate possible pending job and then create a new one")
        finishJobByTag(tag)
        properties.jobs[tag] = viewModelScope.launch {
            try {
                Log.d(tag, "Start...")
                action.invoke()
            } catch (ex: Exception) {
                if(notifyException) properties.error.set(ex.message ?: "Unknown error")
            }
        }
        Log.d(tag, "Job terminated successfully")
    }

    /**
     * Create a new job instance to execute on viewModel scope and in background thread. Generated
     * job will automatically be saved to [Props.jobs] map with [tag] as job name and the
     * actual [Job] as value. If there is any job active with same [tag] then will be finished
     * first, and re-created.
     *
     * If an exception occurs, then the [Props.error] property will be notified if [notifyException]
     * is enabled (true by default).
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
        notifyException: Boolean = true,
        action: suspend () -> Unit
    ) {
        Log.d(tag, "Terminate possible pending job and then create a new one")
        finishJobByTag(tag)
        properties.jobs[tag] = viewModelScope.launch {
            try {
                Log.d(tag, "Start...")
                onBackground { action.invoke() }
            } catch (ex: Exception) {
                if(notifyException) properties.error.set(ex.message ?: "Unknown error")
            }
        }
        Log.d(tag, "Job terminated successfully")
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
     *          onProgress = { progress.set(it) },
     *          onError = { error.set(it) }
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
        onSuccess: (res: T) -> Unit
    ) = newBackgroundJob(tag) {
        onUiThread { onProgress.invoke(true) }
        when(val response = action.invoke()) {
            is ApiResult.Failure -> onUiThread { onError.invoke(response.error) }
            is ApiResult.Success -> onUiThread { onSuccess.invoke(response.data) }
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
        finishAllJobs()
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