package com.neucrack.smarthome;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.neucrack.communication.ToServer;

public class subDevices extends AppCompatActivity {

    boolean lightOn=false;
    boolean curtainOn=false;
    ToServer toServer = null;
    String deviceName=null;

    Button light =null;
    Button curtain =null;
    Button lightSensor =null;
    Button updateStatus =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_devices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id._subdevices_fab);

        light = (Button) findViewById(R.id._light);
        curtain = (Button) findViewById(R.id._curtain);
        lightSensor = (Button) findViewById(R.id._lightSensor);
        updateStatus = (Button) findViewById(R.id._updateStatus);


        Bundle bundle = getIntent().getExtras();
        deviceName=bundle.getString("deviceName");
        Toast.makeText(getApplicationContext(),deviceName,Toast.LENGTH_SHORT).show();


        toServer = new ToServer();




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
                // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI(socket通信必须不在UI线程下进行，以免失去响应)
                new Thread(SetLight).start();
            }
        });


        curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI(socket通信必须不在UI线程下进行，以免失去响应)
                new Thread(SetCurtain).start();
            }
        });

        lightSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI(socket通信必须不在UI线程下进行，以免失去响应)
                new Thread(GetSensor).start();
            }
        });

        updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI(socket通信必须不在UI线程下进行，以免失去响应)
                new Thread(GetLight).start();
                new Thread(GetCurtain).start();
                new Thread(GetSensor).start();
            }
        });

        // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI(socket通信必须不在UI线程下进行，以免失去响应)
        new Thread(GetLight).start();
        new Thread(GetCurtain).start();
        new Thread(GetSensor).start();

    }

    Handler SetLightHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean result = data.getBoolean("result",false);
            if ( result) {
                lightOn = !lightOn;
                if (lightOn)
                    light.setBackgroundColor(0xFF2EBD5F);
                else
                    light.setBackgroundColor(0xffff4444);
                Toast.makeText(getApplicationContext(), "灯光控制成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "灯光控制失败！！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable SetLight = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            boolean result =toServer.SetSwitch(deviceName,1,(!lightOn));
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putBoolean("result", result);
            msg.setData(data);
            SetLightHandler.sendMessage(msg);
        }
    };
    Handler SetCurrainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean result = data.getBoolean("result",false);
            if(result){
                curtainOn = !curtainOn;
                if(curtainOn)
                    curtain.setBackgroundColor(0xFF2EBD5F);
                else
                    curtain.setBackgroundColor(0xffff4444);

            }
            else{
                Toast.makeText(getApplicationContext(),"窗帘控制失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable SetCurtain = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            boolean result = toServer.SetSwitch(deviceName,2,!lightOn);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putBoolean("result", result);
            msg.setData(data);
            SetCurrainHandler.sendMessage(msg);
        }
    };

    Handler GetLightHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean result = data.getBoolean("result",false);
            boolean value = data.getBoolean("value",false);
            if ( result) {
                lightOn = value;
                if (lightOn)
                    light.setBackgroundColor(0xFF2EBD5F);
                else
                    light.setBackgroundColor(0xffff4444);
                Toast.makeText(getApplicationContext(), "灯光状态获取成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "灯光状态获取失败！！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable GetLight = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            int result =toServer.GetSwitchStatus(deviceName,1);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putBoolean("result", result>=0);
            data.putBoolean("value",result>0);
            msg.setData(data);
            GetLightHandler.sendMessage(msg);
        }
    };
    Handler GetCurrainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean result = data.getBoolean("result",false);
            boolean value = data.getBoolean("value",false);
            if ( result) {
                curtainOn = value;
                if (curtainOn)
                    curtain.setBackgroundColor(0xFF2EBD5F);
                else
                    curtain.setBackgroundColor(0xffff4444);
                Toast.makeText(getApplicationContext(), "窗帘状态获取成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "窗帘状态获取失败！！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable GetCurtain = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            int result =toServer.GetSwitchStatus(deviceName,2);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putBoolean("result", result>=0);
            data.putBoolean("value",result>0);
            msg.setData(data);
            GetCurrainHandler.sendMessage(msg);
        }
    };


    Handler GetSensorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            boolean result = data.getBoolean("result",false);
            long value = data.getLong("value",0);
            if ( result) {
                lightSensor.setBackgroundColor(0xFF2EBD5F);
                lightSensor.setText("光感:" + value);
                Toast.makeText(getApplicationContext(), "光感值获取成功", Toast.LENGTH_SHORT).show();
            } else {
                lightSensor.setBackgroundColor(0xffff4444);
                Toast.makeText(getApplicationContext(), "光感值获取失败！！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 网络操作相关的子线程
     */
    Runnable GetSensor = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            long result =toServer.GetSensor(deviceName,1);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putBoolean("result", result>=0);
            data.putLong("value",result);
            msg.setData(data);
            GetSensorHandler.sendMessage(msg);
        }
    };

}