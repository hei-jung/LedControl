package heijung.ledcontrol;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//activity_main.xml 파일에 접근

        Button ConnectBtn = (Button) findViewById(R.id.connectBtn);//'블루투스 연결' 버튼
        Button TimeBtn = (Button) findViewById(R.id.timeBtn);//'현재 시간 표시' 버튼
        //Button TempBtn = (Button) findViewById(R.id.tempBtn);//'현재 기온 표시' 버튼
        //Button ColourBtn = (Button) findViewById(R.id.colourBtn);//'LED 색상 조정' 버튼
        //Button BrBtn = (Button) findViewById(R.id.brBtn);//'LED 밝기 조절' 버튼

        /*'블루투스 연결'버튼 눌렀을 때 실행할 것*/
        ConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,ButtonActivity.class);//ButtonActivity.java 파일로 넘어가는 동작
                startActivity(myIntent);//넘어가는 동작을 실행
            }
        });

        TimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timeIntent = new Intent(MainActivity.this,TimeActivity.class);
                startActivity(timeIntent);
            }
        });
    }
}
