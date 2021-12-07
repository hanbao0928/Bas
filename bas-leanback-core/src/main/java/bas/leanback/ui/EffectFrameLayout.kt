package bas.leanback.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import bas.leanback.core.R
import java.util.*


class EffectFrameLayout : FrameLayout, View.OnFocusChangeListener {

    private var mIsBounceInterpolator = true
    private var mBringToFront = true
    private var mIsParent = false
    private var mScale = 1.05f
    private var mShimmerLinearGradient: LinearGradient? = null
    private var mShimmerGradientMatrix: Matrix? = null
    private var mShimmerPaint: Paint? = null
    private val shimmerPath: Path = Path()
    private val frameRectF: RectF = RectF()
    private var mShimmerTranslate = 0f
    private var mShimmerAnimating = false
    private var startAnimationPreDrawListener: ViewTreeObserver.OnPreDrawListener? = null
    private var mAnimatorSet: AnimatorSet? = null
    private var shadowView: LeanbackEffectView? = null

    private var mIsBorder = false

    //是否是圆角形状
    private var isRoundedShape = false
    private var mRefreshRectF: RectF? = null
    private var mIsDrawn = false

    private var params: EffectParams

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        setWillNotDraw(false)
        params = EffectParams(context, attrs)
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.ShimmerShadowLayout, 0, 0)
        mBringToFront = ta.getBoolean(R.styleable.ShimmerShadowLayout_mBringToFront, false)
        mIsBounceInterpolator =
            ta.getBoolean(R.styleable.ShimmerShadowLayout_mIsBounceInterpolator, true)
        mIsParent = ta.getBoolean(R.styleable.ShimmerShadowLayout_mIsParent, false)
        mScale = ta.getFloat(R.styleable.ShimmerShadowLayout_mScale, 1.05f)
        mIsBorder = ta.getBoolean(R.styleable.ShimmerShadowLayout_mIsBorder, true)
        ta.recycle()
        if (!mIsParent) {
            onFocusChangeListener = this
        }
        mShimmerPaint = Paint()
        mShimmerGradientMatrix = Matrix()
        isRoundedShape =
            params.cornerSizeTopLeft != 0f || params.cornerSizeTopRight != 0f || params.cornerSizeBottomRight != 0f || params.cornerSizeBottomLeft != 0f
    }

    override fun isInEditMode(): Boolean {
        return true
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
        if (shadowView == null) {
            shadowView = LeanbackEffectView(params, context)
            val layoutParams = LayoutParams(width, height)
            addView(shadowView, layoutParams)
        }

        frameRectF.set(
            paddingLeft + params.shadowWidth + params.strokeWidth / 2,
            paddingTop + params.shadowWidth + params.strokeWidth / 2,
            width - paddingRight - params.shadowWidth - params.strokeWidth / 2,
            height - paddingBottom - params.shadowWidth - params.strokeWidth / 2
        )

        shimmerPath.reset()

        //必须使用这种方式，否则在某些情况下会出现中间有个阴影色的色块
        if (params.cornerSizeTopLeft != 0f || params.cornerSizeTopRight != 0f || params.cornerSizeBottomLeft != 0f || params.cornerSizeBottomRight != 0f) {
            shimmerPath.addRoundRect(frameRectF, params.cornerRadius, Path.Direction.CW)
        } else {
            shimmerPath.addRoundRect(frameRectF, 0f, 0f, Path.Direction.CW)
        }

        if ((height != oldw || height != oldh) && isRoundedShape) {
            mRefreshRectF = RectF(
                paddingLeft.toFloat(),
                paddingTop.toFloat(),
                (width - paddingRight).toFloat(),
                (height - paddingBottom).toFloat()
            )
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (mIsDrawn || !isRoundedShape) {
            super.dispatchDraw(canvas)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(mRefreshRectF, null)
            } else {
                canvas.saveLayer(mRefreshRectF, null, Canvas.ALL_SAVE_FLAG)
            }
            super.dispatchDraw(canvas)
            canvas.restore()
        }
        onDrawShimmer(canvas)
    }

    override fun draw(canvas: Canvas) {
        if (!isRoundedShape) {
            super.draw(canvas)
        } else {
            mIsDrawn = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(mRefreshRectF, null)
            } else {
                canvas.saveLayer(mRefreshRectF, null, Canvas.ALL_SAVE_FLAG)
            }
            super.draw(canvas)
            canvas.restore()
        }
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
        super.onDetachedFromWindow()
    }

    /**
     * 绘制闪光
     *
     * @param canvas
     */
    protected fun onDrawShimmer(canvas: Canvas) {
        if (mShimmerAnimating) {
            canvas.save()
            val shimmerTranslateX = frameRectF!!.width() * mShimmerTranslate
            val shimmerTranslateY = frameRectF!!.height() * mShimmerTranslate
            mShimmerGradientMatrix!!.setTranslate(shimmerTranslateX, shimmerTranslateY)
            mShimmerLinearGradient!!.setLocalMatrix(mShimmerGradientMatrix)
            canvas.drawPath(shimmerPath!!, mShimmerPaint!!)
            canvas.restore()
        }
    }

    private fun setShimmerAnimating(shimmerAnimating: Boolean) {
        mShimmerAnimating = shimmerAnimating
        if (mShimmerAnimating) {
            mShimmerLinearGradient = LinearGradient(
                0f,
                0f,
                frameRectF.width(),
                frameRectF.height(),
                intArrayOf(
                    0x00FFFFFF,
                    reduceColorAlphaValueToZero(params.shimmerColor),
                    params.shimmerColor,
                    reduceColorAlphaValueToZero(params.shimmerColor),
                    0x00FFFFFF
                ),
                floatArrayOf(0f, 0.2f, 0.5f, 0.8f, 1f),
                Shader.TileMode.CLAMP
            )
            mShimmerPaint!!.shader = mShimmerLinearGradient
        }
    }

    private fun reduceColorAlphaValueToZero(actualColor: Int): Int {
        return Color.argb(
            0x1A,
            Color.red(actualColor),
            Color.green(actualColor),
            Color.blue(actualColor)
        )
    }

    fun startAnimation() {
        if (width == 0) {
            startAnimationPreDrawListener = object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    startAnimation()
                    return true
                }
            }
            viewTreeObserver.addOnPreDrawListener(startAnimationPreDrawListener)
            return
        }
        if (null != mAnimatorSet) {
            mAnimatorSet!!.cancel()
        }
        if (shadowView != null) {
            shadowView!!.start()
        }
        createAnimatorSet(true)
        mAnimatorSet!!.start()
        isSelected = true
    }

    fun stopAnimation() {
        if (startAnimationPreDrawListener != null) {
            viewTreeObserver.removeOnPreDrawListener(startAnimationPreDrawListener)
        }
        if (null != mAnimatorSet) {
            mAnimatorSet!!.cancel()
        }
        createAnimatorSet(false)
        if (shadowView != null) {
            shadowView!!.stop()
        }
        mAnimatorSet!!.start()
        isSelected = false
    }

    private fun createAnimatorSet(isStart: Boolean) {
        val together: MutableList<Animator> = ArrayList()
        if (isStart) {
            together.add(getScaleXAnimator(mScale))
            together.add(getScaleYAnimator(mScale))
        } else {
            together.add(getScaleXAnimator(1.0f))
            together.add(getScaleYAnimator(1.0f))
        }
        val sequentially: MutableList<Animator> = ArrayList()
        if (params.shimmerEnabled && isStart) {
            sequentially.add(shimmerAnimator)
        }
        mAnimatorSet = AnimatorSet()
        mAnimatorSet!!.playTogether(together)
        mAnimatorSet!!.playSequentially(sequentially)
    }

    private fun getScaleXAnimator(scale: Float): ObjectAnimator {
        val scaleXObjectAnimator =
            ObjectAnimator.ofFloat(this, "scaleX", scale)
                .setDuration(params.scaleAnimDuration)
        if (mIsBounceInterpolator) {
            scaleXObjectAnimator.interpolator = BounceInterpolator()
        }
        return scaleXObjectAnimator
    }

    private fun getScaleYAnimator(scale: Float): ObjectAnimator {
        val scaleYObjectAnimator =
            ObjectAnimator.ofFloat(this, "scaleY", scale)
                .setDuration(params.scaleAnimDuration)
        if (mIsBounceInterpolator) {
            scaleYObjectAnimator.interpolator = BounceInterpolator()
        }
        return scaleYObjectAnimator
    }

    private val shimmerAnimator: ObjectAnimator
        private get() {
            val mShimmerAnimator = ObjectAnimator.ofFloat(this, "shimmerTranslate", -1f, 1f)
            mShimmerAnimator.interpolator = DecelerateInterpolator(1f)
            val screenWidth = resources.displayMetrics.widthPixels
            val max = if (width >= height) width else height
            val duration = if (max > screenWidth / 3) screenWidth / 3 else max
            mShimmerAnimator.duration = (duration * 3).toLong()
            mShimmerAnimator.startDelay = params.shimmerDelay
            mShimmerAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    setShimmerAnimating(true)
                }

                override fun onAnimationEnd(animation: Animator) {
                    setShimmerAnimating(false)
                }
            })
            return mShimmerAnimator
        }
    protected var shimmerTranslate: Float
        protected get() = mShimmerTranslate
        protected set(shimmerTranslate) {
            if (params.shimmerEnabled && mShimmerTranslate != shimmerTranslate) {
                mShimmerTranslate = shimmerTranslate
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            if (mBringToFront) {
                v.bringToFront()
            }
            v.isSelected = true
            startAnimation()
        } else {
            v.isSelected = false
            stopAnimation()
        }
    }

}