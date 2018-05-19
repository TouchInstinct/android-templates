package ru.touchin.templates.viewmodel

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import ru.touchin.roboswag.components.utils.destroyable.BaseDestroyable
import ru.touchin.roboswag.components.utils.destroyable.Destroyable
import ru.touchin.templates.livedata.BaseLiveDataDispatcher
import ru.touchin.templates.livedata.LiveDataDispatcher

/**
 * Created by Denis Karmyshakov on 14.03.18.
 * Base class of ViewModel with [io.reactivex.disposables.Disposable] handling.
 */
open class RxViewModel(
        private val destroyable: BaseDestroyable = BaseDestroyable(),
        private val liveDataDispatcher: BaseLiveDataDispatcher = BaseLiveDataDispatcher(destroyable)
) : ViewModel(), Destroyable by destroyable, LiveDataDispatcher by liveDataDispatcher {

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        destroyable.onDestroy()
    }

}
