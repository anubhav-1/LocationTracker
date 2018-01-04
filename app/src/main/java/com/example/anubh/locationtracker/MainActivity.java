package com.example.anubh.locationtracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;

import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

public class MainActivity extends Activity implements LocationListener {
    TextView txt;
    double lati, longi;
    private static final String dburl="jdbc:mysql://192.168.0.101:3306/locationtracker";
    private static final String username="Mobile";
    private static final String password="123456@Sam";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (TextView) this.findViewById(R.id.loca);

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.onLocationChanged(null);

    }


    public void go(){
        Send objSend = new Send();
        objSend.execute("");
    }

    private class Send extends AsyncTask<String, String , String>{



        @Override
        protected String doInBackground(String... strings) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(dburl, username, password);
                if(conn==null){

                }
                else {
                    String query = "INSERT INTO `1` (Latitude, Longitude)" +"VALUES ('"+lati+"','"+longi+"')";
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(query);
                    conn.close();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        if(location == null){
            txt.setText("Detecting.....Make sure GPS is turned ON");
        }
        else{
            lati = location.getLatitude();
            longi = location.getLongitude();
            txt.setText(lati+" , "+longi);
            go();

        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        TextView txt = (TextView) this.findViewById(R.id.loca);
        txt.setText("Turn ON GPS...");

    }







}
