package com.labrats.destinytracker;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.widgets.Dialog;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.labrats.destinytracker.database.DestinyActivity;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;


public class AtheonActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final boolean TOOLBAR_IS_STICKY = true;

    private View mToolbar;
    private View mImageView;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private TextView mTitleView;
    private int mActionBarSize;
    private int mFlexibleSpaceShowFabOffset;
    private int mFlexibleSpaceImageHeight;
    private int mFabMargin;
    private int mToolbarColor;
    private boolean mFabIsShown;

    //DatabaseList
    List<DestinyActivity> mRaids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atheon);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mActionBarSize = getActionBarSize();
        mToolbarColor = getResources().getColor(R.color.primary);

        mToolbar = findViewById(R.id.toolbar);
        if (!TOOLBAR_IS_STICKY) {
            mToolbar.setBackgroundColor(Color.TRANSPARENT);
        }
        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(getTitle());
        setTitle(null);
        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                //mScrollView.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);

                // If you'd like to start from scrollY == 0, don't write like this:
                //mScrollView.scrollTo(0, 0);
                // The initial scrollY is 0, so it won't invoke onScrollChanged().
                // To do this, use the following:
                onScrollChanged(0, false, false);

                // You can also achieve it with the following codes.
                // This causes scroll change from 1 to 0.
                //mScrollView.scrollTo(0, 1);
                //mScrollView.scrollTo(0, 0);
            }
        });

        //Verifica se já existe alguma informação da RAID no banco de dados, se não existir eu gero os registros
        mRaids = DestinyActivity.find(DestinyActivity.class, "type = ? and description = ?", "RAID", "VOG");

        if(mRaids.size() == 0) {
            //Nivel Normal
            for(int i = 0; i < 3; i++) {
                DestinyActivity raid = new DestinyActivity();
                raid.type = "RAID";
                raid.description = "VOG";
                raid.charNumber = i + 1;
                raid.level = "NORMAL";

                raid.save();
            }

            //Nível Hard
            for(int i = 0; i < 3; i++) {
                DestinyActivity raid = new DestinyActivity();
                raid.type = "RAID";
                raid.description = "VOG";
                raid.charNumber = i + 1;
                raid.level = "HARD";

                raid.save();
            }
        } else {
            LoadRaidInfo();
        }
    }

    private void LoadRaidInfo() {
        //Char1 Normal
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint1_char1)).setChecked(mRaids.get(0).checkpoint1);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint2_char1)).setChecked(mRaids.get(0).checkpoint2);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint3_char1)).setChecked(mRaids.get(0).checkpoint3);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint4_char1)).setChecked(mRaids.get(0).checkpoint4);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint5_char1)).setChecked(mRaids.get(0).checkpoint5);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint6_char1)).setChecked(mRaids.get(0).checkpoint6);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint7_char1)).setChecked(mRaids.get(0).checkpoint7);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        if (TOOLBAR_IS_STICKY) {
            titleTranslationY = Math.max(0, titleTranslationY);
        }
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        if (TOOLBAR_IS_STICKY) {
            // Change alpha of toolbar background
            if (-scrollY + mFlexibleSpaceImageHeight <= mActionBarSize) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, mToolbarColor));
            } else {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolbarColor));
            }
        } else {
            // Translate Toolbar
            if (scrollY < mFlexibleSpaceImageHeight) {
                ViewHelper.setTranslationY(mToolbar, 0);
            } else {
                ViewHelper.setTranslationY(mToolbar, -scrollY);
            }
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.activity_menu.save: {
                //Char1 Normal
                mRaids.get(0).checkpoint1 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint1_char1)).isCheck();
                mRaids.get(0).checkpoint2 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint2_char1)).isCheck();
                mRaids.get(0).checkpoint3 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint3_char1)).isCheck();
                mRaids.get(0).checkpoint4 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint4_char1)).isCheck();
                mRaids.get(0).checkpoint5 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint5_char1)).isCheck();
                mRaids.get(0).checkpoint6 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint6_char1)).isCheck();
                mRaids.get(0).checkpoint7 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint7_char1)).isCheck();
                mRaids.get(0).save();

            }

            case R.activity_menu.clear: {

            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_activity, menu);
        return true;
    }
}
