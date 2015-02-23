package com.labrats.destinytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.Dialog;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.main_menu.about: {
                Dialog dlg = new Dialog(this, getString(R.string.dialog_about_title), "");

                dlg.show();

                //Olhando a documentação, tem um linear layout dentro de um ScrollView. Como o ScrollView aceita somente uma child,
                //pego ela e tento trocar o conteúdo pelo meu conteúdo que foi criado.
                ScrollView dlgLayout = (ScrollView)dlg.findViewById(R.id.message_scrollView);
                LinearLayout l = (LinearLayout)dlgLayout.getChildAt(0);
                l.removeAllViews();
                l.addView(getLayoutInflater().inflate(R.layout.dialog_about, null));

                dlg.setButtonCancel(null);
                dlg.getButtonAccept().setText(getString(R.string.app_dialog_gotit));
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cardsOnClickListener(View v) {
        switch(v.getId()) {
            case R.id.main_card_atheon: {
                Intent intent = new Intent(MainActivity.this, AtheonActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.main_card_crota: {
                Intent intent = new Intent(MainActivity.this, CrotaActivity.class);
                startActivity(intent);
                break;
            }
        }
    };
}
