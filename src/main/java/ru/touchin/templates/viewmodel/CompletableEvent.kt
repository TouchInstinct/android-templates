package ru.touchin.templates.viewmodel

/**
 * Created by Denis Karmyshakov on 14.03.18.
 * Event class that emits from [io.reactivex.Completable].
 */
sealed class CompletableEvent {

    class Loading: CompletableEvent()

    class Completed: CompletableEvent()

    class Error(val throwable: Throwable): CompletableEvent()

}
