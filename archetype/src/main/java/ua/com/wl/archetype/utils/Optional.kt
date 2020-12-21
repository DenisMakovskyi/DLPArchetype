package ua.com.wl.archetype.utils

/**
 * @author Denis Makovskyi
 */
@Deprecated("Moved to DLPCore.")
class Optional<T> private constructor(val value: T? = null) {

    companion object {

        fun <T> empty(): Optional<T> = Optional()

        fun <T> of(value: T): Optional<T> = Optional(value)

        fun <T> ofNullable(value: T?): Optional<T> = Optional(value)
    }

    fun isEmpty(): Boolean = value == null

    fun isNotEmpty(): Boolean = value != null

    fun getOrElse(other: T): T? = value ?: other

    fun getUnsafe(): T? = value

    fun getOrThrow(): T = value ?: throw NoSuchElementException("No value present")

    fun <R> map(block: (T) -> R): Optional<R> {
        return flatMap { lift(block(it)) }
    }

    fun <R> flatMap(block: (T) -> Optional<R>): Optional<R> {
        return when(value) {
            null -> empty()
            else -> block(value)
        }
    }

    fun <T> lift(value: T?): Optional<T> = ofNullable(value)

    fun ifPresent(block: (T) -> Unit) {
        value?.let { block(it) }
    }

    fun ifPresentOrElse(block: (T) -> Unit, otherwise: () -> Unit) {
        value?.let { block(it) } ?: otherwise()
    }

    fun ifPresentOrThrow(block: (T) -> Unit) {
        value?.let { block(it) } ?: throw NoSuchElementException("No value present")
    }

    fun ifPresentOrError(block: (T) -> Unit, throwable: Throwable) {
        value?.let { block(it) } ?: throw throwable
    }

    fun <R> ifPresentOrDefault(block: (T) -> R, otherwise: () -> R): R {
        return value?.let { block(it) } ?: otherwise()
    }

    suspend fun sIfPresent(block: suspend (T) -> Unit) {
        value?.let { block(it) }
    }

    suspend fun sIfPresentOrElse(
        block: suspend (T) -> Unit,
        otherwise: suspend () -> Unit
    ) {
        value?.let { block(it) } ?: otherwise()
    }

    suspend fun sIfPresentOrThrow(block: suspend (T) -> Unit) {
        value?.let { block(it) } ?: throw NoSuchElementException("No value present")
    }

    suspend fun sIfPresentOrError(
        block: suspend (T) -> Unit,
        throwable: Throwable
    ) {
        value?.let { block(it) } ?: throw throwable
    }

    suspend fun <R> sIfPresentOrDefault(
        block: suspend (T) -> R,
        otherwise: suspend () -> R
    ): R {
        return value?.let { block(it) } ?: otherwise()
    }
}