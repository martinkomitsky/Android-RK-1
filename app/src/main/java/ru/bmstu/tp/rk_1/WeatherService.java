package ru.bmstu.tp.rk_1;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class WeatherService extends IntentService {
    public static final String WEATHER_ERROR_ACTION = "ru.bmstu.tp.WEATHER_ERROR_ACTION";
    public static final String WEATHER_CHANGED_ACTION = "ru.bmstu.tp.WEATHER_CHANGED_ACTION";
    public static final String WEATHER_LOAD_ACTION = "ru.bmstu.tp.WEATHER_LOAD_ACTION";

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("!@!", "onHandleIntent");
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);

        if (intent != null) {
            try {
                City city = WeatherStorage.getInstance(this).getCurrentCity();
                Weather weather = WeatherUtils.getInstance().loadWeather(city);
                WeatherStorage.getInstance(this).saveWeather(city, weather);
                broadcastManager.sendBroadcast(new Intent(WEATHER_LOAD_ACTION));
            } catch (IOException e) {
                broadcastManager.sendBroadcast(new Intent(WEATHER_ERROR_ACTION));
                e.printStackTrace();
            }
        }
    }
}
