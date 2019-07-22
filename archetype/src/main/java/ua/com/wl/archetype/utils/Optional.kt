package ua.com.wl.archetype.utils

/**
 * @author Denis Makovskyi
 */

class Optional<T> private constructor(var value: T? = null) {

    companion object {

        fun <T> empty(): Optional<T> = Optional()

        fun <T> of(value: T): Optional<T> = Optional(value)

        fun <T> ofNullable(value: T?): Optional<T> = Optional(value)
    }

    fun isEmpty(): Boolean = value == null

    fun getOrElse(other: T): T? = value?.let { it } ?: other

    fun getUnsafe(): T? = value

    fun getOrThrow(): T = value?.let { it } ?: throw NoSuchElementException("No value present")

    fun ifPresent(block: (T) -> Unit) {
        value?.let { block(it) }
    }

    fun ifPresentOrElse(block: (T) -> Unit, otherwise: () -> Unit) {
        value?.let { block(it) } ?: otherwise()
    }

    fun ifPresentOrThrow(block: (T) -> Unit) {
        value?.let { block(it) } ?: throw NoSuchElementException("No value present")
    }
}