package com.example.hotelhackapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitInternal
import org.orbitmvi.orbit.syntax.ContainerContext
import org.orbitmvi.orbit.viewmodel.container

@OptIn(OrbitInternal::class)
abstract class OrbitViewModel<STATE : Any, SIDE_EFFECT : Any>(
    initialState: STATE
) : ContainerHost<STATE, SIDE_EFFECT>, ViewModel() {

    final override val container = container<STATE, SIDE_EFFECT>(initialState)

    protected fun orbit(
        block: suspend ContainerContext<STATE, SIDE_EFFECT>.() -> Unit
    ) {
        viewModelScope.launch {
            container.orbit(block)
        }
    }

    protected fun orbitLoading(
        showLoading: STATE.() -> STATE,
        hideLoading: STATE.() -> STATE,
        block: suspend ContainerContext<STATE, SIDE_EFFECT>.() -> Unit
    ) = orbit {
        reduce { showLoading(it) }
        try {
            block()
        } finally {
            reduce { hideLoading(it) }
        }
    }

    protected fun orbitCatch(
        onError: (Throwable) -> SIDE_EFFECT,
        block: suspend ContainerContext<STATE, SIDE_EFFECT>.() -> Unit
    ) = orbit {
        try {
            block()
        } catch (e: Throwable) {
            postSideEffect(onError(e))
        }
    }

}
