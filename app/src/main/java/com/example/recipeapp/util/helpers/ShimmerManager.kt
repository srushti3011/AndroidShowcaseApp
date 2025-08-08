package com.example.recipeapp.util.helpers

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.example.recipeapp.R

class ShimmerManager {
    companion object {
        fun applyShimmerEffect(view: View) {
            val paint = Paint()
            val gradient = LinearGradient(
                -200f, 0f, 0f, 0f,
                intArrayOf(Color.LTGRAY, Color.WHITE, Color.LTGRAY),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
            paint.shader = gradient
            val animator = ValueAnimator.ofFloat(0f, 1f)
            animator.duration = 2000L
            animator.repeatCount = ValueAnimator.INFINITE
            animator.addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                val width = view.width.toFloat()
                val animatedGradient = LinearGradient(
                    -width + 2 * width * progress, 0f, width * progress, 0f,
                    intArrayOf(
                        ContextCompat.getColor(
                            view.rootView.context, R.color.colorSecondary
                        ),
                        Color.WHITE,
                        ContextCompat.getColor(
                            view.rootView.context, R.color.colorSecondary
                        ),
                    ),
                    floatArrayOf(0f, 0.35f, 0.7f),
                    Shader.TileMode.CLAMP
                )
                paint.shader = animatedGradient
                view.background = object : Drawable() {
                    override fun draw(canvas: Canvas) {
                        canvas.drawRect(0f, 0f, view.width.toFloat(), view.height.toFloat(), paint)
                    }
                    override fun setAlpha(alpha: Int) {}
                    override fun getOpacity(): Int = PixelFormat.OPAQUE
                    override fun setColorFilter(colorFilter: ColorFilter?) {}
                }
            }
            animator.start()
        }
    }
}