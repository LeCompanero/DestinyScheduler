package com.labrats.destinytracker;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.gc.materialdesign.widgets.Dialog;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.labrats.destinytracker.database.DestinyActivity;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

/**
 * Created by felipe on 25/02/2015.
 */
public class WeeklyActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {

    private View mImageView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;

    //DatabaseList
    List<DestinyActivity> mRaids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        setSupportActionBar(toolbar);

        mImageView = findViewById(R.id.image);
        mToolbarView = findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.primary)));

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        //Verifica se já existe alguma informação da RAID no banco de dados, se não existir eu gero os registros
        mRaids = DestinyActivity.find(DestinyActivity.class, "type = ? and description = ?", "STK", "SEM");

        if(mRaids.size() == 0) {
            //Nivel Normal
            for(int i = 0; i < 3; i++) {
                DestinyActivity raid = new DestinyActivity();
                raid.type = "STK";
                raid.description = "SEM";
                raid.charNumber = i + 1;
                raid.level = "NORMAL";

                raid.save();

                mRaids.add(raid);
            }
        } else {
            LoadRaidInfoNormal();
        }

        //Carrego as configurações para ler o char que está lá e trocar a badge dele
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        switch(prefs.getString("pref_key_char1", "")) {
            case "A": {
                ((ImageView)findViewById(R.id.imgBadgeChar1Normal)).setImageDrawable(getResources().getDrawable(R.drawable.warlock_badge));
            } break;
            case "T": {
                ((ImageView)findViewById(R.id.imgBadgeChar1Normal)).setImageDrawable(getResources().getDrawable(R.drawable.titan_badge));
            } break;
            case "C": {
                ((ImageView)findViewById(R.id.imgBadgeChar1Normal)).setImageDrawable(getResources().getDrawable(R.drawable.hunter_badge));
            } break;
        }

        switch(prefs.getString("pref_key_char2", "")) {
            case "A": {
                ((ImageView)findViewById(R.id.imgBadgeChar2Normal)).setImageDrawable(getResources().getDrawable(R.drawable.warlock_badge));
            } break;
            case "T": {
                ((ImageView)findViewById(R.id.imgBadgeChar2Normal)).setImageDrawable(getResources().getDrawable(R.drawable.titan_badge));
            } break;
            case "C": {
                ((ImageView)findViewById(R.id.imgBadgeChar2Normal)).setImageDrawable(getResources().getDrawable(R.drawable.hunter_badge));
            } break;
        }

        switch(prefs.getString("pref_key_char3", "")) {
            case "A": {
                ((ImageView)findViewById(R.id.imgBadgeChar3Normal)).setImageDrawable(getResources().getDrawable(R.drawable.warlock_badge));
            } break;
            case "T": {
                ((ImageView)findViewById(R.id.imgBadgeChar3Normal)).setImageDrawable(getResources().getDrawable(R.drawable.titan_badge));
            } break;
            case "C": {
                ((ImageView)findViewById(R.id.imgBadgeChar3Normal)).setImageDrawable(getResources().getDrawable(R.drawable.hunter_badge));
            } break;
        }
    }

    private void LoadRaidInfoNormal() {
        //Char1 Normal
        ((CheckBox)findViewById(R.id.weekly_normal_checkpoint1_char1)).setChecked(mRaids.get(0).checkpoint1);
        ((CheckBox)findViewById(R.id.weekly_normal_checkpoint2_char1)).setChecked(mRaids.get(0).checkpoint2);

        //Char2 Normal
        ((CheckBox)findViewById(R.id.weekly_normal_checkpoint1_char2)).setChecked(mRaids.get(1).checkpoint1);
        ((CheckBox)findViewById(R.id.weekly_normal_checkpoint2_char2)).setChecked(mRaids.get(1).checkpoint2);

        //Char3 Normal
        ((CheckBox)findViewById(R.id.weekly_normal_checkpoint1_char3)).setChecked(mRaids.get(2).checkpoint1);
        ((CheckBox)findViewById(R.id.weekly_normal_checkpoint2_char3)).setChecked(mRaids.get(2).checkpoint2);
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
                SaveRaidInfoNormal();

                NavUtils.navigateUpFromSameTask(this);
                return true;

            }

            case R.activity_menu.clear: {
                ClearRaidInfo();
                return true;
            }

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ClearRaidInfo() {

        Dialog dlg = new Dialog(this, getString(R.string.dialog_clear_title), getString(R.string.dialog_clear_description));
        dlg.addCancelButton(getString(R.string.app_dialog_cancel));

        dlg.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 6; i++) {
                    mRaids.get(i).checkpoint1 = false;
                    mRaids.get(i).checkpoint2 = false;

                    mRaids.get(i).save();
                }

                NavUtils.navigateUpFromSameTask(WeeklyActivity.this);
            }
        });

        dlg.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dlg.show();

        dlg.getButtonAccept().setText(getString(R.string.app_dialog_gotit));
    }

    private void SaveRaidInfoNormal() {
        //Char1 Normal
        mRaids.get(0).checkpoint1 = ((CheckBox)findViewById(R.id.weekly_normal_checkpoint1_char1)).isChecked();
        mRaids.get(0).checkpoint2 = ((CheckBox)findViewById(R.id.weekly_normal_checkpoint2_char1)).isChecked();

        mRaids.get(0).save();

        //Char1 Normal
        mRaids.get(1).checkpoint1 = ((CheckBox)findViewById(R.id.weekly_normal_checkpoint1_char2)).isChecked();
        mRaids.get(1).checkpoint2 = ((CheckBox)findViewById(R.id.weekly_normal_checkpoint2_char2)).isChecked();

        mRaids.get(1).save();

        //Char1 Normal
        mRaids.get(2).checkpoint1 = ((CheckBox)findViewById(R.id.weekly_normal_checkpoint1_char3)).isChecked();
        mRaids.get(2).checkpoint2 = ((CheckBox)findViewById(R.id.weekly_normal_checkpoint2_char3)).isChecked();

        mRaids.get(2).save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_activity, menu);
        return true;
    }
}