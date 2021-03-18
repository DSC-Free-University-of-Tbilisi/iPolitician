package com.example.ipolitician.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton.OnVisibilityChangedListener


class SubmitFloatLayout(context: Context?, attrs: AttributeSet?): FloatingActionButton.Behavior() {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(
                    coordinatorLayout, child!!, directTargetChild, target,
                    axes, type
                )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyConsumed < 0 && child.visibility === View.VISIBLE) {
            child.hide(object : OnVisibilityChangedListener() {
                override fun onHidden(floatingActionButon: FloatingActionButton) {
                    super.onShown(floatingActionButon)
                    floatingActionButon.visibility = View.INVISIBLE
                }
            })
        } else if (dyConsumed > 0 && child.visibility !== View.VISIBLE) {
            child.show()
        }
    }
}