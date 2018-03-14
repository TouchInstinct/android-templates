package ru.touchin.templates.livedata

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import ru.touchin.templates.viewmodel.CompletableEvent
import ru.touchin.templates.viewmodel.MaybeEvent
import ru.touchin.templates.viewmodel.ObservableEvent
import ru.touchin.templates.viewmodel.SingleEvent

interface LiveDataDispatcher {

    fun <T> Observable<T>.dispatchTo(liveData: MutableLiveData<ObservableEvent<T>>)

    fun <T> Single<T>.dispatchTo(liveData: MutableLiveData<SingleEvent<T>>)

    fun Completable.dispatchTo(liveData: MutableLiveData<CompletableEvent>)

    fun <T> Maybe<T>.dispatchTo(liveData: MutableLiveData<MaybeEvent<T>>)

}
