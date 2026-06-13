package io.github.matkurban.contactlistview.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Debounces rapid calls inside [scope], matching the Dart package debounce behavior.
 */
class Debouncer(
    private val scope: CoroutineScope,
    private val debounceTimeMillis: Long = 16L,
) {
    private var job: Job? = null

    fun submit(action: () -> Unit) {
        job?.cancel()
        job = scope.launch {
            delay(debounceTimeMillis.milliseconds)
            action()
        }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }
}
