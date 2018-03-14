package ru.touchin.templates.livedata

import android.arch.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import ru.touchin.roboswag.components.utils.destroyable.Destroyable
import ru.touchin.templates.viewmodel.CompletableEvent
import ru.touchin.templates.viewmodel.MaybeEvent
import ru.touchin.templates.viewmodel.ObservableEvent
import ru.touchin.templates.viewmodel.SingleEvent

class BaseLiveDataDispatcher(private val destroyable: Destroyable) : LiveDataDispatcher {

    override fun <T> Observable<T>.dispatchTo(liveData: MutableLiveData<ObservableEvent<T>>) {
        liveData.value = ObservableEvent.Loading(liveData.value?.data)
        destroyable.untilDestroy(this,
                { data -> liveData.value = ObservableEvent.Success(data) },
                { throwable -> liveData.value = ObservableEvent.Error(throwable, liveData.value?.data) },
                { liveData.value = ObservableEvent.Completed(liveData.value?.data) })
    }

    override fun <T> Single<T>.dispatchTo(liveData: MutableLiveData<SingleEvent<T>>) {
        liveData.value = SingleEvent.Loading(liveData.value?.data)
        destroyable.untilDestroy(this,
                { data -> liveData.value = SingleEvent.Success(data) },
                { throwable -> liveData.value = SingleEvent.Error(throwable, liveData.value?.data) })
    }

    override fun Completable.dispatchTo(liveData: MutableLiveData<CompletableEvent>) {
        liveData.value = CompletableEvent.Loading()
        destroyable.untilDestroy(this,
                { liveData.value = CompletableEvent.Completed() },
                { throwable -> liveData.value = CompletableEvent.Error(throwable) })
    }

    override fun <T> Maybe<T>.dispatchTo(liveData: MutableLiveData<MaybeEvent<T>>) {
        liveData.value = MaybeEvent.Loading(liveData.value?.data)
        destroyable.untilDestroy(this,
                { data -> liveData.value = MaybeEvent.Success(data) },
                { throwable -> liveData.value = MaybeEvent.Error(throwable, liveData.value?.data) },
                { liveData.value = MaybeEvent.Completed(liveData.value?.data) })
    }

}
