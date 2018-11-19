package heijung.ledcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothSocket clientSocket;
    String DEVICE_UID = "98:D3:51:F5:E8:0F";    //'HC-05'의 MAC 주소

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//activity_main.xml 파일에 접근

        Button ConnectBtn = (Button) findViewById(R.id.connectBtn);//'블루투스 연결' 버튼
        final Button TimeBtn = (Button) findViewById(R.id.timeBtn);//'현재 시간 표시' 버튼
        final Button TempBtn = (Button) findViewById(R.id.tempBtn);//'현재 기온 표시' 버튼
        final Button ColourBtn = (Button) findViewById(R.id.colourBtn);//'LED 색상 조정' 버튼
        final Button BrBtn = (Button) findViewById(R.id.brBtn);//'LED 밝기 조절' 버튼

        /*'블루투스 연결'버튼 눌렀을 때 실행할 것*/
        ConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ButtonActivity.class);//ButtonActivity.java 파일로 넘어가는 동작
                startActivity(myIntent);//넘어가는 동작을 실행
            }
        });

        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        startActivityForResult(new Intent(enableBT),0);

        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

        try{
            BluetoothDevice device = bluetooth.getRemoteDevice(DEVICE_UID);
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});

            clientSocket = (BluetoothSocket)m.invoke(device,1);
            clientSocket.connect();

            Toast.makeText(getApplicationContext(),"연결됨",Toast.LENGTH_LONG).show();
        }   catch (InvocationTargetException e){
            e.printStackTrace();
        }   catch (NoSuchMethodException e){
            e.printStackTrace();
        }   catch (IllegalAccessException e){
            e.printStackTrace();
        }   catch (IOException e){
            e.printStackTrace();
        }

        /*'현재 시간 표시'버튼 눌렀을 때 실행할 것*/
        TimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timeIntent = new Intent(MainActivity.this, TimeActivity.class);
                startActivity(timeIntent);

                try{
                    OutputStream outputStream = clientSocket.getOutputStream();

                    String value = "0";

                    outputStream.write(value.getBytes());
                }   catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        /*'현재 기온 표시'버튼 눌렀을 때 실행할 것*/
        TempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    OutputStream outputStream = clientSocket.getOutputStream();

                    String value = "1";

                    outputStream.write(value.getBytes());
                }   catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        /*'LED 색상 조정'버튼 눌렀을 때 실행할 것*/
        ColourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    OutputStream outputStream = clientSocket.getOutputStream();

                    String value = "2";

                    outputStream.write(value.getBytes());
                }   catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        /*'LED 밝기 조절'버튼 눌렀을 때 실행할 것*/
        BrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    OutputStream outputStream = clientSocket.getOutputStream();

                    String value = "3";

                    outputStream.write(value.getBytes());
                }   catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
