/*
 * Copyright (C) 2018 Lucio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmName("TextsKt")
@file:JvmMultifileClass

package bas.droid.core.text

import android.graphics.Paint
import android.text.InputFilter
import android.text.TextPaint
import android.widget.TextView

/**
 * 添加最大长度过滤器：用于显示输入框最多只能输入多少个字符
 */
fun TextView.addLengthFilter(max: Int) {
    this.appendInputFilter(InputFilter.LengthFilter(max))
}

/**
 * 添加正整数输入过滤器
 */
fun TextView.addPositiveNumberInputFilter() {
    this.appendInputFilter(PositiveNumberInputFilter())
}

/**
 * 添加金额输入过滤器
 */
fun TextView.addCashierInputFilter() {
    this.appendInputFilter(CashierInputFilter())
}

/**
 * 添加删除线
 */
fun TextView.applyDeleteLine() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

/**
 * 添加下划线
 */
fun TextView.applyUnderLine() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

/**
 * 文字加粗
 */
fun TextView.applyBold() {
    this.paintFlags = this.paintFlags or Paint.FAKE_BOLD_TEXT_FLAG
}

///**
// * 渐变色文字
// */
//fun TextView.applyGradientText() {
//    val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE)//颜色的数组
//    val position = floatArrayOf(0f, 0.7f, 1.0f)//颜色渐变位置的数组
//    val shadow = LinearGradient(
//        0f, 0f, paint.textSize * text.length, 0f, colors, position,
//        Shader.TileMode.CLAMP
//    )
//    paint.shader = shadow
//    invalidate()
//}

/**
 * 获取文本高度
 */
fun getTextHeight(textSize: Float): Int {
    val replyPaint = TextPaint()
    replyPaint.isAntiAlias = true
    replyPaint.textSize = textSize
    return replyPaint.textHeight() + 2
}

/**
 * 获取画笔绘制的文本高度
 */
fun TextPaint.textHeight(): Int {
    val fm = fontMetrics
    return Math.ceil((fm.descent - fm.ascent).toDouble()).toInt()
}

