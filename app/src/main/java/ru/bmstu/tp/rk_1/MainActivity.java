package ru.bmstu.tp.rk_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class MainActivity extends ActionBarActivity {

    private TextView cityName;
    private TextView temperature;
    private TextView weatherDescription;
    private WeatherStorage weatherStorageInstance;
    private City selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent serviceIntent = new Intent(this, WeatherService.class);
        setContentView(R.layout.main_activity);
        getSupportActionBar().hide();

        weatherStorageInstance = WeatherStorage.getInstance(MainActivity.this);

        cityName = (TextView)this.findViewById(R.id.city_name);
        temperature = (TextView)this.findViewById(R.id.temperature);
        weatherDescription = (TextView)this.findViewById(R.id.weather_description);

        IntentFilter intentFilter = new IntentFilter(WeatherService.WEATHER_LOAD_ACTION);
        intentFilter.addAction(WeatherService.WEATHER_ERROR_ACTION);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    public void changeCity(View view){
        Intent ccIntent = new Intent(MainActivity.this, SecondActivity.class);
        MainActivity.this.startActivity(ccIntent);
    }

    public void turnWeatherSilentUpdateOn(View view) {
        Intent turnOnIntent = new Intent(MainActivity.this, WeatherService.class);
        turnOnIntent.setAction(WeatherService.WEATHER_LOAD_ACTION);
        WeatherUtils.getInstance().schedule(MainActivity.this, turnOnIntent);
    }

    public void turnWeatherSilentUpdateOff(View view) {
        Intent turnOffIntent = new Intent(MainActivity.this, WeatherService.class);
        turnOffIntent.setAction(WeatherService.WEATHER_LOAD_ACTION);
        WeatherUtils.getInstance().unschedule(MainActivity.this, turnOffIntent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case WeatherService.WEATHER_LOAD_ACTION:
                    Weather currentWeather = weatherStorageInstance.getLastSavedWeather(selectedCity);
                    if (currentWeather != null) {
                        temperature.setText(String.valueOf(currentWeather.getTemperature()));
                        weatherDescription.setText(currentWeather.getDescription());
                    } else {
                        temperature.setText("0");
                        weatherDescription.setText("An error ocurred");

                    }

            break;
                case WeatherService.WEATHER_ERROR_ACTION:
                    temperature.setText("");
                    weatherDescription.setText("Update error");
                    break;
            }
        }
    };
}
