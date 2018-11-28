package heijung.ledcontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends Activity {
    private static final String TAG = "bluetooth";

    Button btn1, btn2, btn3, btn4, btn5, btn6;
    TextView txtArduino, dateNow;
    RelativeLayout rlayout;
    Handler h;

    long now = System.currentTimeMillis();  //현재시간 가져오기

    Date date = new Date(now);  //Date 생성

    /*시간데이터 가져오고 싶은 형식으로 가져오기*/
    SimpleDateFormat sdfNowDate = new SimpleDateFormat("yy/MM/dd HH:mm", Locale.KOREA);
    String formatDate = sdfNowDate.format(date);

    final int RECEIVE_MESSAGE = 1;  //Status for Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();

    private ConnectedThread mConnectedThread;

    //SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //MAC-address of Bluetooth module
    private static String address = "98:D3:51:F5:E8:0F";    //'HC-05'의 MAC 주소

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);//activity_main.xml 파일에 접근

        dateNow = (TextView) findViewById(R.id.dateNow);
        dateNow.setText(formatDate);    //안드로이드에 현재 시간 출력

        btn1 = (Button) findViewById(R.id.button1);//'현재 시간(12시간)' 버튼
        btn2 = (Button) findViewById(R.id.button2);//'현재 시간(24시간)' 버튼
        btn3 = (Button) findViewById(R.id.button3);//'현재 기온 표시' 버튼
        btn4 = (Button) findViewById(R.id.button4);//'오늘 날짜 표시' 버튼
        btn5 = (Button) findViewById(R.id.button5);//'LED 색상 조정' 버튼
        btn6 = (Button) findViewById(R.id.button6);//'LED 밝기 조절' 버튼

        txtArduino = (TextView) findViewById(R.id.txtArduino);
        rlayout = (RelativeLayout) findViewById(R.id.layout);
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RECEIVE_MESSAGE:   //아두이노에서 보낸 문장을 안드로이드에 출력
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);
                        int endofLineIndex = sb.indexOf("\r\n");
                        if (endofLineIndex > 0) {
                            String sbprint = sb.substring(0, endofLineIndex);
                            sb.delete(0, sb.length());
                            txtArduino.setText("아두이노 실행: " + sbprint);  //상단 출력 문장

                            btn1.setEnabled(true);
                            btn2.setEnabled(true);
                            btn3.setEnabled(true);
                            btn4.setEnabled(true);
                            btn5.setEnabled(true);
                            btn6.setEnabled(true);
                        }
                        break;
                }
            };
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();   //get Bluetooth adapter
        checkBTState(); //연결 확인

        /*'현재 시간(12시간)'버튼 눌렀을 때 실행할 것*/
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("1");    //아두이노에 '1' 전송(아두이노: BluetoothData = 1)
            }
        });
        /*'현재 시간(24시간)'버튼 눌렀을 때 실행할 것*/
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("2");    //아두이노에 '2' 전송(아두이노: BluetoothData = 2)
            }
        });
        /*'현재 기온 표시'버튼 눌렀을 때 실행할 것*/
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("3");    //아두이노에 '3' 전송(아두이노: BluetoothData = 3)
            }
        });
        /*'오늘 날짜 표시'버튼 눌렀을 때 실행할 것*/
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("4");    //아두이노에 '4' 전송(아두이노: BluetoothData = 4)
            }
        });
        /*'LED 색상 조정'버튼 눌렀을 때 실행할 것*/
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("5");    //아두이노에 '5' 전송(아두이노: BluetoothData = 5)
            }
        });
        /*'LED 밝기 조절'버튼 눌렀을 때 실행할 것*/
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectedThread.write("6");    //아두이노에 '6' 전송(아두이노: BluetoothData = 6)
            }
        });
    }   //안드로이드에서는 위처럼 1~6까지의 숫자를 전송하여 실행 함수의 경우의 수를 나눠줄 뿐이다.(명령수행은 모두 아두이노 프로그래밍)

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection", e);
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume = try connect...");

        //Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        btAdapter.cancelDiscovery();

        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "....Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        //Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        //Check for Bluetooth support and then check to make sure it is turned on
        //Emulator doesn't support Bluetooth and will return nll
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    /*안드로이드-아두이노 통신(블루투스 연결됐을 경우)*/
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;   //버퍼(임시저장)
            OutputStream tmpOut = null; //버퍼(임시저장)
            //데이터의 송수신 즉시 저장되기 때문에 반드시 임시저장 변수가 필요하다.

            try {
                tmpIn = socket.getInputStream();    //블루투스 소켓에서 데이터 읽어옴
                tmpOut = socket.getOutputStream();  //블루투스 소켓에 데이터를 작성함
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  //버퍼
            int bytes; //리턴값

            //항상 값을 체크
            while (true) {
                try {
                    //input stream에서 읽어오기
                    bytes = mmInStream.read(buffer);    //buffer의 크기와 buffer에 저장된 값 가져오기
                    h.obtainMessage(RECEIVE_MESSAGE, bytes, -1, buffer).sendToTarget(); //Handler에 메시지 데이터 전송
                } catch (IOException e) {
                    break;
                }
            }
        }

        /*아두이노에 데이터 작성할 때 사용하는 함수*/
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }
}
