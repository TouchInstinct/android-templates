package ru.touchin.templates.livedata

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import ru.touchin.templates.livedata.event.CompletableEvent
import ru.touchin.templates.livedata.event.MaybeEvent
import ru.touchin.templates.livedata.event.ObservableEvent
import ru.touchin.templates.livedata.event.SingleEvent

interface LiveDataDispatcher {

    fun <T> Flowable<T>.dispatchTo(liveData: MutableLiveData<ObservableEvent<T>>): Disposable

    fun <T> Observable<T>.dispatchTo(liveData: MutableLiveData<ObservableEvent<T>>): Disposable

    fun <T> Single<T>.dispatchTo(liveData: MutableLiveData<SingleEvent<T>>): Disposable

    fun Completable.dispatchTo(liveData: MutableLiveData<CompletableEvent>): Disposable

    fun <T> Maybe<T>.dispatchTo(liveData: MutableLiveData<MaybeEvent<T>>): Disposable

}
