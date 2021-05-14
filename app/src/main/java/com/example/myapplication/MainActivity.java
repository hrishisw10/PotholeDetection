package com.example.myapplication;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.facebook.stetho.Stetho;

import java.io.Console;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
    TextView x,y,z,record;
    Button start,stop;
    SQLiteDatabase SQLITEDATABASE ;     //this is database
    String SQLiteQuery;
    String Xacc;
    String Yacc;
    String Zacc;
    Boolean flag = false;
    Vibrator v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener((SensorEventListener)this, accelerometer,SensorManager.SENSOR_STATUS_ACCURACY_HIGH);

        setContentView(R.layout.activity_main);
        x = findViewById(R.id.x);
        y = findViewById(R.id.y);
        z = findViewById(R.id.z);
        record = findViewById(R.id.record);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);


        DBCreate();

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                record.setText("Recording");
                record.setTextColor(Color.GREEN);
                stop.setBackgroundColor(Color.RED);
                start.setBackgroundColor(Color.GRAY);
                //start.setBackgroundColor(0x0106000d);


            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                record.setText("Not Recording");
                record.setTextColor(Color.RED);

                start.setBackgroundColor(Color.GREEN);
                stop.setBackgroundColor(Color.GRAY);


            }
        });



    }

    private void SubmitData2SQLiteDB() {
       Xacc = x.getText().toString();
       Yacc= y.getText().toString();
       Zacc = z.getText().toString();

        SQLiteQuery = "INSERT INTO accelereometerData3(x,y,z) VALUES('"+Xacc+"', '"+Yacc+"', '"+Zacc+"');";
        SQLITEDATABASE.execSQL(SQLiteQuery);
        Toast.makeText(MainActivity.this,"Data Submit Successfully", Toast.LENGTH_LONG).show();


    }

    private void DBCreate() {
        SQLITEDATABASE = openOrCreateDatabase("SKDataBase", Context.MODE_PRIVATE, null);  //this will create DATABASE
        SQLITEDATABASE.execSQL(
                "CREATE TABLE IF NOT EXISTS accelereometerData3(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, x VARCHAR, y VARCHAR, z VARCHAR);"
        );
        Log.d("Creation","dbcreate is working"+DebugDB.getAddressLog()+"");


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x.setText("X: " + event.values[0] + "");
        y.setText("Y: " + event.values[1] + "");
        z.setText("Z: " + event.values[2] + "");
        if(flag) {
            SubmitData2SQLiteDB();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        t1.setText("YOUR LOCATION : "+"\n Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            t2.setText("\n"+addresses.get(0).getAddressLine(0)+ ", " +
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));

        }catch(Exception e)
        {

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();

    }
}

