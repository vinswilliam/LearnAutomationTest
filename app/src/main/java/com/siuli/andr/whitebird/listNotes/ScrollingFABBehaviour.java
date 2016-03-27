package com.siuli.andr.whitebird.listNotes;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

/**
 * Created by william on 3/7/2016.
 */
public class ScrollingFABBehaviour extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private int toolbarHeight;

    public ScrollingFABBehaviour(Context context, AttributeSet attrs){
        super(context, attrs);
        toolbarHeight = 57;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if(dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = child.getHeight() + fabBottomMargin;
            float ratio = (float) dependency.getY()/(float) toolbarHeight;
            child.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}
