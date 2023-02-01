package com.example.nestedrv

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView

/**
 * Author: 芒硝
 * Email : 1248389474@qq.com
 * Date  : 2023/2/1 16:35
 * Desc  :
 */
class MxNested(context: Context, attrs: AttributeSet?): NestedScrollView(context, attrs) {

    constructor(context: Context): this(context, null)

    var mParentScrollHeight = 0
    var mScrollY = 0

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        Log.i("Lanier", "$dx $dy ${consumed[0]} ${consumed[1]}")
        Log.e("Lanier", "$mScrollY $mParentScrollHeight")
        if (mScrollY < mParentScrollHeight) {
            consumed[0] = dx
            consumed[1] = dy
            scrollBy(0, dy)
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
//        Log.e("Lanier", "$l $t $oldl $oldt")
        mScrollY = t
    }
}