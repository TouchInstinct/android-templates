package ru.touchin.templates.livedata.event

/**
 * Created by Denis Karmyshakov on 14.03.18.
 * Event class that emits from [io.reactivex.Single].
 */
sealed class SingleEvent<out T>(open val data: T?) {

    class Loading<out T>(data: T?): SingleEvent<T>(data)

    class Success<out T>(override val data: T): SingleEvent<T>(data)

    class Error<out T>(val throwable: Throwable, data: T?): SingleEvent<T>(data)

}
