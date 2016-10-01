package ru.bmstu.tp.rk_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import java.util.ArrayList;

import android.widget.Button;
import android.widget.TextView;


import ru.mail.weather.lib.City;
import ru.mail.weather.lib.WeatherStorage;


public class SecondActivity extends ActionBarActivity {
    private ArrayList<String> data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        ViewGroup root = (ViewGroup) findViewById(R.id.root);

        for (final City city : City.values()) {
            Button button = new Button(this);
            button.setText(city.name());

            button.setOnClickListener(new View.OnClickListener () {
               @Override
                public void onClick(View v) {
                   WeatherStorage.getInstance(SecondActivity.this).setCurrentCity(city);
                   finish();
               }
            });
            root.addView(button);
        }


    }

}
