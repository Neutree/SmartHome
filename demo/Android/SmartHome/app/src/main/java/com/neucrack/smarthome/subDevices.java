package com.neucrack.smarthome;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.neucrack.communication.ToServer;

public class subDevices extends AppCompatActivity {

    boolean lightOn=false;
    boolean curtainOn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_devices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id._subdevices_fab);

        final Button light = (Button) findViewById(R.id._light);
        final Button curtain = (Button) findViewById(R.id._curtain);
        final Button lightSensor = (Button) findViewById(R.id._lightSensor);
        final Button updateStatus = (Button) findViewById(R.id._updateStatus);


        Bundle bundle = getIntent().getExtras();
        final String deviceName=bundle.getString("deviceName");
        Toast.makeText(getApplicationContext(),deviceName,Toast.LENGTH_SHORT).show();


        final ToServer toServer = new ToServer();




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true == toServer.SetSwitch(deviceName, 1, !lightOn)) {
                    lightOn = !lightOn;
                    if (lightOn)
                        light.setBackgroundColor(0xFF2EBD5F);
                    else
                        light.setBackgroundColor(0xff4444);

                } else {
                    Toast.makeText(getApplicationContext(), "灯光控制失败", Toast.LENGTH_SHORT).show();
                }
            }
        });


        curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(toServer.SetSwitch(deviceName,2,!lightOn)){
                        curtainOn = !curtainOn;
                        if(curtainOn)
                            curtain.setBackgroundColor(0xFF2EBD5F);
                        else
                            curtain.setBackgroundColor(0xff4444);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"窗帘控制失败",Toast.LENGTH_SHORT).show();
                    }
            }
        });

        lightSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    long value = toServer.GetSensor(deviceName,1);
                    if(value>=0){
                        lightSensor.setBackgroundColor(0xFF2EBD5F);
                        lightSensor.setText("光感:"+value);
                    }
                    else{
                        lightSensor.setBackgroundColor(0xffff4444);
                        Toast.makeText(getApplicationContext(),"光感状态查询失败",Toast.LENGTH_SHORT).show();
                    }
            }
        });

        updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    long value = toServer.GetSensor(deviceName,1);
                    if(value>=0){
                        lightSensor.setBackgroundColor(0xFF2EBD5F);
                        lightSensor.setText("光感:"+value);
                    }
                    else{
                        lightSensor.setBackgroundColor(0xff4444);
                        Toast.makeText(getApplicationContext(),"光感状态查询失败",Toast.LENGTH_SHORT).show();
                    }
                    value = toServer.GetSwitchStatus(deviceName,1);
                    if(value>=0){
                        if(value>0)
                            light.setBackgroundColor(0xFF2EBD5F);
                        else
                            light.setBackgroundColor(0xff4444);
                    }
                    value = toServer.GetSwitchStatus(deviceName,2);
                    if(value>=0){
                        if(value>0)
                            light.setBackgroundColor(0xFF2EBD5F);
                        else
                            light.setBackgroundColor(0xff4444);
                    }
            }
        });
    }

}
