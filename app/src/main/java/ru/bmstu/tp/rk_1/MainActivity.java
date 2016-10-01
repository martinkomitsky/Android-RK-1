package ru.bmstu.tp.rk_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;

public class MainActivity extends ActionBarActivity {
    public static final String WEATHER_ERROR_ACTION = "ru.bmstu.tp.WEATHER_ERROR_ACTION";
    public static final String WEATHER_CHANGED_ACTION = "ru.bmstu.tp.WEATHER_CHANGED_ACTION";
    public static final String WEATHER_LOAD_ACTION = "ru.bmstu.tp.WEATHER_LOAD_ACTION";

    private TextView cityName;
    private TextView temperature;
    private TextView weatherDescription;
    private WeatherStorage weatherStorageInstance;
    private City selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent serviceIntent = new Intent(this, WeatherService.class);
//        intent.setAction(WEATHER_LOAD_ACTION);
        startService(serviceIntent);
        setContentView(R.layout.main_activity);
        getSupportActionBar().hide();

        weatherStorageInstance = WeatherStorage.getInstance(MainActivity.this);
        selectedCity = weatherStorageInstance.getCurrentCity();
        weatherStorageInstance.setCurrentCity(selectedCity);

        cityName = (TextView)this.findViewById(R.id.city_name);
        temperature = (TextView)this.findViewById(R.id.temperature);
        weatherDescription = (TextView)this.findViewById(R.id.weather_description);

        IntentFilter intentFilter = new IntentFilter(WEATHER_LOAD_ACTION);
        intentFilter.addAction(WEATHER_ERROR_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedCity = weatherStorageInstance.getCurrentCity();
        cityName.setText(selectedCity.name());
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, WeatherService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void changeCity(View view){
        Intent ccIntent = new Intent(MainActivity.this, SecondActivity.class);
        MainActivity.this.startActivity(ccIntent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case WEATHER_LOAD_ACTION:
                    Weather currentWeather = weatherStorageInstance.getLastSavedWeather(selectedCity);
                    temperature.setText(String.valueOf(currentWeather.getTemperature()));
                    weatherDescription.setText(currentWeather.getDescription());
                    break;

                case WEATHER_ERROR_ACTION:
                    temperature.setText("");
                    weatherDescription.setText("Update error");
                    break;
            }
        }
    };
}
