package com.weatheraggregator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import com.weatheraggregator.app.R;

public class ExpandablePanel extends LinearLayout {
    public final static int VERTIVAL = 0;
    public final static int HORIZONTAL = 1;

    private final int mHandleId;
    private final int mContentId;

    private int mOrientation;

    private View mHandle;
    private View mContent;

    private boolean mExpanded = false;

    private int mCollapsedHeight = 0;
    private int mContentHeight = 0;

    private int mCollapsedWith = 0;
    private int mContentWith = 0;

    private int mAnimationDuration = 0;

    private OnExpandListener mListener;

    public ExpandablePanel(Context context) {
        this(context, null);
    }

    public ExpandablePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mListener = new DefaultOnExpandListener();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandablePanel, 0, 0);

        // How high the content should be in "collapsed" state
        mCollapsedHeight = (int) a.getDimension(R.styleable.ExpandablePanel_collapsedHeight, 0.0f);
        mCollapsedWith = (int) a.getDimension(R.styleable.ExpandablePanel_collapsedWith, 0.0f);
        mCollapsedWith = mCollapsedHeight;
        // How long the animation should take
        mAnimationDuration = a.getInteger(R.styleable.ExpandablePanel_animationDuration, 500);

        int handleId = a.getResourceId(R.styleable.ExpandablePanel_handle, 0);
        if (handleId == 0) {
            throw new IllegalArgumentException("The handle attribute is required and must refer " + "to a valid child.");
        }

        int contentId = a.getResourceId(R.styleable.ExpandablePanel_content, 0);
        if (contentId == 0) {
            throw new IllegalArgumentException("The content attribute is required and must refer to a valid child.");
        }

        mOrientation = a.getResourceId(R.styleable.ExpandablePanel_orientation, 1);
        mHandleId = handleId;
        mContentId = contentId;

        a.recycle();
    }

    public void setOnExpandListener(OnExpandListener listener) {
        mListener = listener;
    }

    public void setCollapsedWith(int collapsedWiht) {
        mCollapsedWith = collapsedWiht;
    }

    public void setCollapsedHeight(int collapsedHeight) {
        mCollapsedHeight = collapsedHeight;
    }

    public void setAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mHandle = findViewById(mHandleId);

        if (mHandle == null) {

            throw new IllegalArgumentException("The handle attribute is must refer to an" + " existing child.");
        }

        mContent = findViewById(mContentId);
        if (mContent == null) {
            throw new IllegalArgumentException("The content attribute must refer to an" + " existing child.");
        }

        android.view.ViewGroup.LayoutParams lp = mContent.getLayoutParams();
        if (mOrientation == VERTIVAL) {
            lp.height = mCollapsedHeight;
        } else {
            lp.width = mCollapsedWith;
        }

        mContent.setLayoutParams(lp);

        mHandle.setOnClickListener(new PanelToggler());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // First, measure how high content wants to be
        mContent.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
        mContentHeight = mContent.getMeasuredHeight();
        mContentWith = mContent.getMeasuredWidth();

        //hideOrShowViewGroup();

        // Then let the usual thing happen
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Animation expandVertical() {
        Animation a;
        if (mExpanded) {
            a = new ExpandAnimation(mContentHeight, mCollapsedHeight, mOrientation);
            mListener.onCollapse(mHandle, mContent);
        } else {
            a = new ExpandAnimation(mCollapsedHeight, mContentHeight, mOrientation);
            mListener.onExpand(mHandle, mContent);
        }
        return a;
    }

    private Animation expandHorizontal() {
        Animation a;
        if (mExpanded) {
            a = new ExpandAnimation(mContentHeight, mCollapsedWith, mOrientation);
            mListener.onCollapse(mHandle, mContent);
        } else {
            a = new ExpandAnimation(mCollapsedWith, mContentWith, mOrientation);
            mListener.onExpand(mHandle, mContent);
        }
        return a;
    }

    private void checkContentViewWithAndHeight() {
        if (mContent.getLayoutParams().height == 0) // Need to do this or
        // else the animation
        // will not play if the
        // height is 0
        {
            android.view.ViewGroup.LayoutParams lp = mContent.getLayoutParams();
            lp.height = 1;
            mContent.setLayoutParams(lp);
            mContent.requestLayout();
        }
        if (mContent.getLayoutParams().width == 0) // Need to do this or
        // else the animation
        // will not play if the
        // height is 0
        {
            android.view.ViewGroup.LayoutParams lp = mContent.getLayoutParams();
            lp.width = 1;
            mContent.setLayoutParams(lp);
            mContent.requestLayout();
        }
    }

    private class PanelToggler implements OnClickListener {
        public void onClick(View v) {
            Animation a;
            if (mOrientation == VERTIVAL) {
                a = expandVertical();
            } else {
                a = expandHorizontal();
            }
            a.setDuration(mAnimationDuration);
            if (mContent.getLayoutParams().width == 0) {

            }
            checkContentViewWithAndHeight();
            mContent.startAnimation(a);
            mExpanded = !mExpanded;
        }
    }

    private class ExpandAnimation extends Animation {
        private final int mStartValue;
        private final int mDeltaValue;

        private final int mOrientation;

        public ExpandAnimation(int startValue, int endValue, int orientation) {
            mOrientation = orientation;

            mStartValue = startValue;
            mDeltaValue = (endValue - startValue);

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            android.view.ViewGroup.LayoutParams lp = mContent.getLayoutParams();
            if (mOrientation == VERTIVAL) {
                lp.height = (int) (mStartValue + mDeltaValue * interpolatedTime);
            } else {
                lp.width = (int) (mStartValue + mDeltaValue * interpolatedTime);
            }
            mContent.setLayoutParams(lp);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public interface OnExpandListener {
        public void onExpand(View handle, View content);

        public void onCollapse(View handle, View content);
    }

    private class DefaultOnExpandListener implements OnExpandListener {
        public void onCollapse(View handle, View content) {
        }

        public void onExpand(View handle, View content) {
        }
    }
}

//private void hideOrShowViewGroup()
//{
//	if (mOrientation == VERTIVAL)
//	{
//		if (mContentHeight < mCollapsedHeight)
//		{
//			viewGroup.setVisibility(View.GONE);
//			// mHandle.setVisibility(View.GONE);
//
//		}
//		else
//		{
//			viewGroup.setVisibility(View.VISIBLE);
//			// mHandle.setVisibility(View.VISIBLE);
//		}
//	}
//	else
//	{
//		if (mContentWith < mCollapsedWith)
//		{
//			viewGroup.setVisibility(View.GONE);
//			// mHandle.setVisibility(View.GONE);
//
//		}
//		else
//		{
//			viewGroup.setVisibility(View.VISIBLE);
//			// mHandle.setVisibility(View.VISIBLE);
//		}
//	}
//}

