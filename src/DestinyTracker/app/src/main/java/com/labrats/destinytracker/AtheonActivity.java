package com.labrats.destinytracker;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.labrats.destinytracker.database.DestinyActivity;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;


public class AtheonActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {

    private View mImageView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;

    //DatabaseList
    List<DestinyActivity> mRaids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atheon);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mImageView = findViewById(R.id.image);
        mToolbarView = findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mToolbarView.setN

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.primary);
        float alpha = 1 - (float) Math.max(0, mParallaxImageHeight - scrollY) / mParallaxImageHeight;
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
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
                mRaids.get(0).checkpoint1 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint1_char1)).isChecked();
                mRaids.get(0).checkpoint2 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint2_char1)).isChecked();
                mRaids.get(0).checkpoint3 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint3_char1)).isChecked();
                mRaids.get(0).checkpoint4 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint4_char1)).isChecked();
                mRaids.get(0).checkpoint5 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint5_char1)).isChecked();
                mRaids.get(0).checkpoint6 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint6_char1)).isChecked();
                mRaids.get(0).checkpoint7 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint7_char1)).isChecked();
                mRaids.get(0).save();

                NavUtils.navigateUpFromSameTask(this);
                return true;

            }

            case R.activity_menu.clear: {

            }

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

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
