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
        setContentView(R.layout.activity_main);

        Button ConnectBtn = (Button) findViewById(R.id.connectBtn);
        //Button TimeBtn = (Button) findViewById(R.id.timeBtn);
        //Button TempBtn = (Button) findViewById(R.id.tempBtn);
        //Button ColourBtn = (Button) findViewById(R.id.colourBtn);
        //Button BrBtn = (Button) findViewById(R.id.brBtn);

        ConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,ButtonActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
