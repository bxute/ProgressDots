package xute.progressdot;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class ProgressDotView extends View {
    public static final int DEFAULT_DOTS_COUNT = 5;
    public static final int SMALL_DOT_RADIUS_IN_DP = 8;
    public static final int LARGE_DOT_RADIUS_IN_DP = 12;
    public static final int SPACE_BETWEEN_DOTS_IN_DP = 8;
    public static final String ACTIVE_DOT_COLOR = "#009988";
    public static final String INACTIVE_DOT_COLOR = "#EEEEEE";

    private static final int DOT_ANIMATION_DURATION = 500;
    private int mAnimationTime;
    private int mDotsCount;
    private int mSmallDotRadius;
    private int mLargeDotRadius;
    private int activeDotColor;
    private int inActiveDotColor;
    private int mSpaceBetweenDots;
    private int calculatedViewWidth;
    private int calculatedViewHeight;
    private int[] radiuses;
    private Paint mDotsPaint;
    private int[] colors;
    private int mLastActiveIndex;
    private int mActiveIndex;
    private int radiusDiff;
    private boolean animating;
    private Resources resources;

    public ProgressDotView(Context context) {
        super(context);
        setDefaults(context);
        init(context);
    }

    private void setDefaults(Context context) {
        Resources resources = context.getResources();
        mSmallDotRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SMALL_DOT_RADIUS_IN_DP, resources.getDisplayMetrics());
        mLargeDotRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, LARGE_DOT_RADIUS_IN_DP, resources.getDisplayMetrics());
        mSpaceBetweenDots = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SPACE_BETWEEN_DOTS_IN_DP, resources.getDisplayMetrics());
        activeDotColor = Color.parseColor(ACTIVE_DOT_COLOR);
        inActiveDotColor = Color.parseColor(INACTIVE_DOT_COLOR);
        mDotsCount = DEFAULT_DOTS_COUNT;
        mAnimationTime = DOT_ANIMATION_DURATION;
    }

    public ProgressDotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Resources resources = context.getResources();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressDotView, 0, 0);
        try {
            mSmallDotRadius = getPxFromDp(resources, (int) typedArray.getDimension(R.styleable.ProgressDotView_smallDotRadius, SMALL_DOT_RADIUS_IN_DP));
            mLargeDotRadius = getPxFromDp(resources, (int) typedArray.getDimension(R.styleable.ProgressDotView_largeDotRadius, LARGE_DOT_RADIUS_IN_DP));
            mSpaceBetweenDots = getPxFromDp(resources, (int) typedArray.getDimension(R.styleable.ProgressDotView_spaceBetweenDots, SPACE_BETWEEN_DOTS_IN_DP));
            mAnimationTime = typedArray.getInteger(R.styleable.ProgressDotView_switchTimeInMillis, DOT_ANIMATION_DURATION);
            mDotsCount = typedArray.getInteger(R.styleable.ProgressDotView_dotCount, DEFAULT_DOTS_COUNT);
            activeDotColor = typedArray.getColor(R.styleable.ProgressDotView_activeDotColor, Color.parseColor(ACTIVE_DOT_COLOR));
            inActiveDotColor = typedArray.getColor(R.styleable.ProgressDotView_inActiveDotColor, Color.parseColor(INACTIVE_DOT_COLOR));
        } finally {
            typedArray.recycle();
        }
        init(context);
    }

    private void init(Context context) {
        resources = context.getResources();
        mDotsPaint = new Paint();
        mDotsPaint.setAntiAlias(true);
        mDotsPaint.setStyle(Paint.Style.FILL);
        radiusDiff = mLargeDotRadius - mSmallDotRadius;
        setDotCount(mDotsCount);
    }

    public void setDotCount(int count) {
        if (count < 2) {
            throw new IllegalArgumentException("There should be atleast 2 Dots");
        }
        this.mDotsCount = count;
        Log.d("ViewD","count "+count);
        calculatedViewWidth = (count * 2 * mSmallDotRadius) + (count + 1) * mSpaceBetweenDots;
        calculatedViewHeight = 2 * mSpaceBetweenDots + mLargeDotRadius;
        radiuses = new int[count];
        colors = new int[count];
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                colors[i] = activeDotColor;
                radiuses[i] = mLargeDotRadius; //default active is 1st dot
            } else {
                colors[i] = inActiveDotColor;
                radiuses[i] = mSmallDotRadius;
            }
        }
        invalidate();
    }

    public void setActiveDotColor(int _activeColor){
        this.activeDotColor = _activeColor;
    }

    public void setInactiveDotColor(int _inActiveColor){
        this.inActiveDotColor = _inActiveColor;
    }

    public void setSmallDotRadiusInDp(int dp){
        this.mSmallDotRadius = getPxFromDp(resources,dp);
    }

    public void setLargeDotRadiusInDp(int dp){
        this.mLargeDotRadius = getPxFromDp(resources,dp);
    }

    public void setDotAnimationDuration(int millis){
        this.mAnimationTime = millis;
    }

    public void setSpaceBetweenDotsInDp(int dp){
        this.mSpaceBetweenDots = getPxFromDp(resources,dp);
    }

    public void moveToNext() {
        if (!animating) {
            mLastActiveIndex = mActiveIndex;
            if (mActiveIndex < mDotsCount - 1) {
                mActiveIndex++;
            } else {
                mActiveIndex = 0;
            }
            startAnimating();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cx = mSpaceBetweenDots + mSmallDotRadius;
        int cy = mSpaceBetweenDots + mSmallDotRadius;
        for (int i = 0; i < mDotsCount; i++) {
            mDotsPaint.setColor(colors[i]);
            //draw circle
            canvas.drawCircle(cx, cy, radiuses[i], mDotsPaint);
            //update next cx
            cx += 2 * mSmallDotRadius + mSpaceBetweenDots;
        }
    }

    private void startAnimating() {
        ValueAnimator animator1 = getRadiusAnimator();
        ValueAnimator animator2 = getActiveToInActiveColorAnimator();
        ValueAnimator animator3 = getInActiveToActiveColorAnimator();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(animator1, animator2, animator3);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

    private ValueAnimator getRadiusAnimator() {
        final ValueAnimator radiusAnimator = ValueAnimator.ofInt(0, radiusDiff);
        radiusAnimator.setDuration(mAnimationTime);
        radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int v = (int) valueAnimator.getAnimatedValue();
                radiuses[mActiveIndex] = mSmallDotRadius + v;
                radiuses[mLastActiveIndex] = mLargeDotRadius - v;
                invalidate();
            }
        });
        return radiusAnimator;
    }

    private ValueAnimator getActiveToInActiveColorAnimator() {
        final ValueAnimator colorAnimator = ValueAnimator.ofArgb(activeDotColor, inActiveDotColor);
        colorAnimator.setDuration(mAnimationTime);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                colors[mLastActiveIndex] = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        return colorAnimator;
    }

    private ValueAnimator getInActiveToActiveColorAnimator() {
        final ValueAnimator colorAnimator = ValueAnimator.ofArgb(inActiveDotColor, activeDotColor);
        colorAnimator.setDuration(mAnimationTime);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                colors[mActiveIndex] = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        return colorAnimator;
    }

    private int getPxFromDp(Resources resources, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getPaddingStart() + getPaddingEnd() + calculatedViewWidth;
        int height = getPaddingTop() + getPaddingBottom() + calculatedViewHeight;
        int w = resolveSizeAndState(width, widthMeasureSpec, 0);
        int h = resolveSizeAndState(height, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }
}
