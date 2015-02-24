package com.labrats.destinytracker;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;
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

                mRaids.add(raid);
            }

            //Nível Hard
            for(int i = 0; i < 3; i++) {
                DestinyActivity raid = new DestinyActivity();
                raid.type = "RAID";
                raid.description = "VOG";
                raid.charNumber = i + 1;
                raid.level = "HARD";

                raid.save();

                mRaids.add(raid);
            }
        } else {
            LoadRaidInfoNormal();
            LoadRaidInfoHard();
        }

        //Carrego as configurações para ler o char que está lá e trocar a badge dele
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        switch(prefs.getString("pref_key_char1", "")) {
            case "A": {
                ((ImageView)findViewById(R.id.imgBadgeChar1Normal)).setImageDrawable(getResources().getDrawable(R.drawable.warlock_badge));
                ((ImageView)findViewById(R.id.imgBadgeChar1Hard)).setImageDrawable(getResources().getDrawable(R.drawable.warlock_badge));
            } break;
            case "T": {
                ((ImageView)findViewById(R.id.imgBadgeChar1Normal)).setImageDrawable(getResources().getDrawable(R.drawable.titan_badge));
                ((ImageView)findViewById(R.id.imgBadgeChar1Hard)).setImageDrawable(getResources().getDrawable(R.drawable.titan_badge));
            } break;
            case "C": {
                ((ImageView)findViewById(R.id.imgBadgeChar1Normal)).setImageDrawable(getResources().getDrawable(R.drawable.hunter_badge));
                ((ImageView)findViewById(R.id.imgBadgeChar1Hard)).setImageDrawable(getResources().getDrawable(R.drawable.hunter_badge));
            } break;
        }

        switch(prefs.getString("pref_key_char2", "")) {
            case "A": {
                ((ImageView)findViewById(R.id.imgBadgeChar2Normal)).setImageDrawable(getResources().getDrawable(R.drawable.warlock_badge));
                ((ImageView)findViewById(R.id.imgBadgeChar2Hard)).setImageDrawable(getResources().getDrawable(R.drawable.warlock_badge));
            } break;
            case "T": {
                ((ImageView)findViewById(R.id.imgBadgeChar2Normal)).setImageDrawable(getResources().getDrawable(R.drawable.titan_badge));
                ((ImageView)findViewById(R.id.imgBadgeChar2Hard)).setImageDrawable(getResources().getDrawable(R.drawable.titan_badge));
            } break;
            case "C": {
                ((ImageView)findViewById(R.id.imgBadgeChar2Normal)).setImageDrawable(getResources().getDrawable(R.drawable.hunter_badge));
                ((ImageView)findViewById(R.id.imgBadgeChar2Hard)).setImageDrawable(getResources().getDrawable(R.drawable.hunter_badge));
            } break;
        }

        switch(prefs.getString("pref_key_char3", "")) {
            case "A": {
                ((ImageView)findViewById(R.id.imgBadgeChar3Normal)).setImageDrawable(getResources().getDrawable(R.drawable.warlock_badge));
                ((ImageView)findViewById(R.id.imgBadgeChar3Hard)).setImageDrawable(getResources().getDrawable(R.drawable.warlock_badge));
            } break;
            case "T": {
                ((ImageView)findViewById(R.id.imgBadgeChar3Normal)).setImageDrawable(getResources().getDrawable(R.drawable.titan_badge));
                ((ImageView)findViewById(R.id.imgBadgeChar3Hard)).setImageDrawable(getResources().getDrawable(R.drawable.titan_badge));
            } break;
            case "C": {
                ((ImageView)findViewById(R.id.imgBadgeChar3Normal)).setImageDrawable(getResources().getDrawable(R.drawable.hunter_badge));
                ((ImageView)findViewById(R.id.imgBadgeChar3Hard)).setImageDrawable(getResources().getDrawable(R.drawable.hunter_badge));
            } break;
        }
    }

    private void LoadRaidInfoNormal() {
        //Char1 Normal
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint1_char1)).setChecked(mRaids.get(0).checkpoint1);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint2_char1)).setChecked(mRaids.get(0).checkpoint2);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint3_char1)).setChecked(mRaids.get(0).checkpoint3);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint4_char1)).setChecked(mRaids.get(0).checkpoint4);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint5_char1)).setChecked(mRaids.get(0).checkpoint5);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint6_char1)).setChecked(mRaids.get(0).checkpoint6);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint7_char1)).setChecked(mRaids.get(0).checkpoint7);

        ((CheckBox)findViewById(R.id.atheon_normal_chest1_char1)).setChecked(mRaids.get(0).chest1);
        ((CheckBox)findViewById(R.id.atheon_normal_chest2_char1)).setChecked(mRaids.get(0).chest2);
        ((CheckBox)findViewById(R.id.atheon_normal_chest3_char1)).setChecked(mRaids.get(0).chest3);
        ((CheckBox)findViewById(R.id.atheon_normal_chest4_char1)).setChecked(mRaids.get(0).chest4);
        ((CheckBox)findViewById(R.id.atheon_normal_chest5_char1)).setChecked(mRaids.get(0).chest5);

        //Char2 Normal
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint1_char2)).setChecked(mRaids.get(1).checkpoint1);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint2_char2)).setChecked(mRaids.get(1).checkpoint2);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint3_char2)).setChecked(mRaids.get(1).checkpoint3);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint4_char2)).setChecked(mRaids.get(1).checkpoint4);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint5_char2)).setChecked(mRaids.get(1).checkpoint5);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint6_char2)).setChecked(mRaids.get(1).checkpoint6);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint7_char2)).setChecked(mRaids.get(1).checkpoint7);

        ((CheckBox)findViewById(R.id.atheon_normal_chest1_char2)).setChecked(mRaids.get(1).chest1);
        ((CheckBox)findViewById(R.id.atheon_normal_chest2_char2)).setChecked(mRaids.get(1).chest2);
        ((CheckBox)findViewById(R.id.atheon_normal_chest3_char2)).setChecked(mRaids.get(1).chest3);
        ((CheckBox)findViewById(R.id.atheon_normal_chest4_char2)).setChecked(mRaids.get(1).chest4);
        ((CheckBox)findViewById(R.id.atheon_normal_chest5_char2)).setChecked(mRaids.get(1).chest5);

        //Char3 Normal
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint1_char3)).setChecked(mRaids.get(2).checkpoint1);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint2_char3)).setChecked(mRaids.get(2).checkpoint2);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint3_char3)).setChecked(mRaids.get(2).checkpoint3);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint4_char3)).setChecked(mRaids.get(2).checkpoint4);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint5_char3)).setChecked(mRaids.get(2).checkpoint5);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint6_char3)).setChecked(mRaids.get(2).checkpoint6);
        ((CheckBox)findViewById(R.id.atheon_normal_checkpoint7_char3)).setChecked(mRaids.get(2).checkpoint7);

        ((CheckBox)findViewById(R.id.atheon_normal_chest1_char3)).setChecked(mRaids.get(2).chest1);
        ((CheckBox)findViewById(R.id.atheon_normal_chest2_char3)).setChecked(mRaids.get(2).chest2);
        ((CheckBox)findViewById(R.id.atheon_normal_chest3_char3)).setChecked(mRaids.get(2).chest3);
        ((CheckBox)findViewById(R.id.atheon_normal_chest4_char3)).setChecked(mRaids.get(2).chest4);
        ((CheckBox)findViewById(R.id.atheon_normal_chest5_char3)).setChecked(mRaids.get(2).chest5);
    }

    private void LoadRaidInfoHard() {
        //Char1 Hard
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint1_char1)).setChecked(mRaids.get(3).checkpoint1);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint2_char1)).setChecked(mRaids.get(3).checkpoint2);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint3_char1)).setChecked(mRaids.get(3).checkpoint3);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint4_char1)).setChecked(mRaids.get(3).checkpoint4);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint5_char1)).setChecked(mRaids.get(3).checkpoint5);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint6_char1)).setChecked(mRaids.get(3).checkpoint6);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint7_char1)).setChecked(mRaids.get(3).checkpoint7);

        ((CheckBox)findViewById(R.id.atheon_hard_chest1_char1)).setChecked(mRaids.get(3).chest1);
        ((CheckBox)findViewById(R.id.atheon_hard_chest2_char1)).setChecked(mRaids.get(3).chest2);
        ((CheckBox)findViewById(R.id.atheon_hard_chest3_char1)).setChecked(mRaids.get(3).chest3);
        ((CheckBox)findViewById(R.id.atheon_hard_chest4_char1)).setChecked(mRaids.get(3).chest4);
        ((CheckBox)findViewById(R.id.atheon_hard_chest5_char1)).setChecked(mRaids.get(3).chest5);

        ((CheckBox)findViewById(R.id.atheon_hard_chest1_char1)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest2_char1)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest3_char1)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest4_char1)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest5_char1)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);

        //Char2 Hard
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint1_char2)).setChecked(mRaids.get(4).checkpoint1);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint2_char2)).setChecked(mRaids.get(4).checkpoint2);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint3_char2)).setChecked(mRaids.get(4).checkpoint3);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint4_char2)).setChecked(mRaids.get(4).checkpoint4);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint5_char2)).setChecked(mRaids.get(4).checkpoint5);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint6_char2)).setChecked(mRaids.get(4).checkpoint6);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint7_char2)).setChecked(mRaids.get(4).checkpoint7);

        ((CheckBox)findViewById(R.id.atheon_hard_chest1_char2)).setChecked(mRaids.get(4).chest1);
        ((CheckBox)findViewById(R.id.atheon_hard_chest2_char2)).setChecked(mRaids.get(4).chest2);
        ((CheckBox)findViewById(R.id.atheon_hard_chest3_char2)).setChecked(mRaids.get(4).chest3);
        ((CheckBox)findViewById(R.id.atheon_hard_chest4_char2)).setChecked(mRaids.get(4).chest4);
        ((CheckBox)findViewById(R.id.atheon_hard_chest5_char2)).setChecked(mRaids.get(4).chest5);

        ((CheckBox)findViewById(R.id.atheon_hard_chest1_char2)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest2_char2)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest3_char2)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest4_char2)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest5_char2)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);

        //Char3 Hard
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint1_char3)).setChecked(mRaids.get(5).checkpoint1);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint2_char3)).setChecked(mRaids.get(5).checkpoint2);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint3_char3)).setChecked(mRaids.get(5).checkpoint3);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint4_char3)).setChecked(mRaids.get(5).checkpoint4);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint5_char3)).setChecked(mRaids.get(5).checkpoint5);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint6_char3)).setChecked(mRaids.get(5).checkpoint6);
        ((CheckBox)findViewById(R.id.atheon_hard_checkpoint7_char3)).setChecked(mRaids.get(5).checkpoint7);

        ((CheckBox)findViewById(R.id.atheon_hard_chest1_char3)).setChecked(mRaids.get(5).chest1);
        ((CheckBox)findViewById(R.id.atheon_hard_chest2_char3)).setChecked(mRaids.get(5).chest2);
        ((CheckBox)findViewById(R.id.atheon_hard_chest3_char3)).setChecked(mRaids.get(5).chest3);
        ((CheckBox)findViewById(R.id.atheon_hard_chest4_char3)).setChecked(mRaids.get(5).chest4);
        ((CheckBox)findViewById(R.id.atheon_hard_chest5_char3)).setChecked(mRaids.get(5).chest5);

        ((CheckBox)findViewById(R.id.atheon_hard_chest1_char3)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest2_char3)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest3_char3)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest4_char3)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
        ((CheckBox)findViewById(R.id.atheon_hard_chest5_char3)).setOnCheckedChangeListener(chestCheckBoxOnCheckedChanged);
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
                SaveRaidInfoHard();

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
                    mRaids.get(i).checkpoint3 = false;
                    mRaids.get(i).checkpoint4 = false;
                    mRaids.get(i).checkpoint5 = false;
                    mRaids.get(i).checkpoint6 = false;
                    mRaids.get(i).checkpoint7 = false;

                    mRaids.get(i).chest1 = false;
                    mRaids.get(i).chest2 = false;
                    mRaids.get(i).chest3 = false;
                    mRaids.get(i).chest4 = false;
                    mRaids.get(i).chest5 = false;

                    mRaids.get(i).save();
                }

                NavUtils.navigateUpFromSameTask(AtheonActivity.this);
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
        mRaids.get(0).checkpoint1 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint1_char1)).isChecked();
        mRaids.get(0).checkpoint2 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint2_char1)).isChecked();
        mRaids.get(0).checkpoint3 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint3_char1)).isChecked();
        mRaids.get(0).checkpoint4 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint4_char1)).isChecked();
        mRaids.get(0).checkpoint5 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint5_char1)).isChecked();
        mRaids.get(0).checkpoint6 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint6_char1)).isChecked();
        mRaids.get(0).checkpoint7 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint7_char1)).isChecked();

        mRaids.get(0).chest1 = ((CheckBox)findViewById(R.id.atheon_normal_chest1_char1)).isChecked();
        mRaids.get(0).chest2 = ((CheckBox)findViewById(R.id.atheon_normal_chest2_char1)).isChecked();
        mRaids.get(0).chest3 = ((CheckBox)findViewById(R.id.atheon_normal_chest3_char1)).isChecked();
        mRaids.get(0).chest4 = ((CheckBox)findViewById(R.id.atheon_normal_chest4_char1)).isChecked();
        mRaids.get(0).chest5 = ((CheckBox)findViewById(R.id.atheon_normal_chest5_char1)).isChecked();

        mRaids.get(0).save();

        //Char2 Normal
        mRaids.get(1).checkpoint1 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint1_char2)).isChecked();
        mRaids.get(1).checkpoint2 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint2_char2)).isChecked();
        mRaids.get(1).checkpoint3 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint3_char2)).isChecked();
        mRaids.get(1).checkpoint4 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint4_char2)).isChecked();
        mRaids.get(1).checkpoint5 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint5_char2)).isChecked();
        mRaids.get(1).checkpoint6 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint6_char2)).isChecked();
        mRaids.get(1).checkpoint7 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint7_char2)).isChecked();

        mRaids.get(1).chest1 = ((CheckBox)findViewById(R.id.atheon_normal_chest1_char2)).isChecked();
        mRaids.get(1).chest2 = ((CheckBox)findViewById(R.id.atheon_normal_chest2_char2)).isChecked();
        mRaids.get(1).chest3 = ((CheckBox)findViewById(R.id.atheon_normal_chest3_char2)).isChecked();
        mRaids.get(1).chest4 = ((CheckBox)findViewById(R.id.atheon_normal_chest4_char2)).isChecked();
        mRaids.get(1).chest5 = ((CheckBox)findViewById(R.id.atheon_normal_chest5_char2)).isChecked();

        mRaids.get(1).save();

        //Char2 Normal
        mRaids.get(2).checkpoint1 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint1_char3)).isChecked();
        mRaids.get(2).checkpoint2 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint2_char3)).isChecked();
        mRaids.get(2).checkpoint3 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint3_char3)).isChecked();
        mRaids.get(2).checkpoint4 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint4_char3)).isChecked();
        mRaids.get(2).checkpoint5 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint5_char3)).isChecked();
        mRaids.get(2).checkpoint6 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint6_char3)).isChecked();
        mRaids.get(2).checkpoint7 = ((CheckBox)findViewById(R.id.atheon_normal_checkpoint7_char3)).isChecked();

        mRaids.get(2).chest1 = ((CheckBox)findViewById(R.id.atheon_normal_chest1_char3)).isChecked();
        mRaids.get(2).chest2 = ((CheckBox)findViewById(R.id.atheon_normal_chest2_char3)).isChecked();
        mRaids.get(2).chest3 = ((CheckBox)findViewById(R.id.atheon_normal_chest3_char3)).isChecked();
        mRaids.get(2).chest4 = ((CheckBox)findViewById(R.id.atheon_normal_chest4_char3)).isChecked();
        mRaids.get(2).chest5 = ((CheckBox)findViewById(R.id.atheon_normal_chest5_char3)).isChecked();

        mRaids.get(2).save();
    }

    private void SaveRaidInfoHard() {
        //Char1 hard
        mRaids.get(3).checkpoint1 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint1_char1)).isChecked();
        mRaids.get(3).checkpoint2 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint2_char1)).isChecked();
        mRaids.get(3).checkpoint3 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint3_char1)).isChecked();
        mRaids.get(3).checkpoint4 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint4_char1)).isChecked();
        mRaids.get(3).checkpoint5 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint5_char1)).isChecked();
        mRaids.get(3).checkpoint6 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint6_char1)).isChecked();
        mRaids.get(3).checkpoint7 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint7_char1)).isChecked();

        mRaids.get(3).chest1 = ((CheckBox)findViewById(R.id.atheon_hard_chest1_char1)).isChecked();
        mRaids.get(3).chest2 = ((CheckBox)findViewById(R.id.atheon_hard_chest2_char1)).isChecked();
        mRaids.get(3).chest3 = ((CheckBox)findViewById(R.id.atheon_hard_chest3_char1)).isChecked();
        mRaids.get(3).chest4 = ((CheckBox)findViewById(R.id.atheon_hard_chest4_char1)).isChecked();
        mRaids.get(3).chest5 = ((CheckBox)findViewById(R.id.atheon_hard_chest5_char1)).isChecked();

        mRaids.get(3).save();

        //Char2 hard
        mRaids.get(4).checkpoint1 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint1_char2)).isChecked();
        mRaids.get(4).checkpoint2 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint2_char2)).isChecked();
        mRaids.get(4).checkpoint3 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint3_char2)).isChecked();
        mRaids.get(4).checkpoint4 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint4_char2)).isChecked();
        mRaids.get(4).checkpoint5 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint5_char2)).isChecked();
        mRaids.get(4).checkpoint6 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint6_char2)).isChecked();
        mRaids.get(4).checkpoint7 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint7_char2)).isChecked();

        mRaids.get(4).chest1 = ((CheckBox)findViewById(R.id.atheon_hard_chest1_char2)).isChecked();
        mRaids.get(4).chest2 = ((CheckBox)findViewById(R.id.atheon_hard_chest2_char2)).isChecked();
        mRaids.get(4).chest3 = ((CheckBox)findViewById(R.id.atheon_hard_chest3_char2)).isChecked();
        mRaids.get(4).chest4 = ((CheckBox)findViewById(R.id.atheon_hard_chest4_char2)).isChecked();
        mRaids.get(4).chest5 = ((CheckBox)findViewById(R.id.atheon_hard_chest5_char2)).isChecked();

        mRaids.get(4).save();

        //Char2 hard
        mRaids.get(5).checkpoint1 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint1_char3)).isChecked();
        mRaids.get(5).checkpoint2 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint2_char3)).isChecked();
        mRaids.get(5).checkpoint3 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint3_char3)).isChecked();
        mRaids.get(5).checkpoint4 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint4_char3)).isChecked();
        mRaids.get(5).checkpoint5 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint5_char3)).isChecked();
        mRaids.get(5).checkpoint6 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint6_char3)).isChecked();
        mRaids.get(5).checkpoint7 = ((CheckBox)findViewById(R.id.atheon_hard_checkpoint7_char3)).isChecked();

        mRaids.get(5).chest1 = ((CheckBox)findViewById(R.id.atheon_hard_chest1_char3)).isChecked();
        mRaids.get(5).chest2 = ((CheckBox)findViewById(R.id.atheon_hard_chest2_char3)).isChecked();
        mRaids.get(5).chest3 = ((CheckBox)findViewById(R.id.atheon_hard_chest3_char3)).isChecked();
        mRaids.get(5).chest4 = ((CheckBox)findViewById(R.id.atheon_hard_chest4_char3)).isChecked();
        mRaids.get(5).chest5 = ((CheckBox)findViewById(R.id.atheon_hard_chest5_char3)).isChecked();

        mRaids.get(5).save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_activity, menu);
        return true;
    }

    private CompoundButton.OnCheckedChangeListener chestCheckBoxOnCheckedChanged = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == R.id.atheon_hard_chest1_char1) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest1_char1)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest1_char2) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest1_char2)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest1_char3) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest1_char3)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest2_char1) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest2_char1)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest2_char2) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest2_char2)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest2_char3) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest2_char3)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest3_char1) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest3_char1)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest3_char2) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest3_char2)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest3_char3) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest3_char3)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest4_char1) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest4_char1)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest4_char2) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest4_char2)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest4_char3) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest4_char3)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest5_char1) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest5_char1)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest5_char2) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest5_char2)).setChecked(isChecked);
            }

            if (buttonView.getId() == R.id.atheon_hard_chest5_char3) {
                ((CheckBox)findViewById(R.id.atheon_normal_chest5_char3)).setChecked(isChecked);
            }
        }
    };
}
