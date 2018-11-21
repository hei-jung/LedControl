package heijung.ledcontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class ButtonActivity extends Activity {

    BluetoothAdapter bluetoothAdapter;
    CheckBox cbSearchAllow;

    ListView searchDeviceList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        cbSearchAllow = (CheckBox)findViewById(R.id.cbSearchAllow);
        cbSearchAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCbSearchAllow();
            }
        });

        IntentFilter scanFilter = new IntentFilter();
        scanFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
    }

    private void setCbSearchAllow(){
        if (cbSearchAllow.isChecked()){
            Toast.makeText(getApplicationContext(),"주변에 블루투스 연결 가능한 장치에서 내 장치를 검색하도록 허용합니다.",Toast.LENGTH_SHORT).show();

            if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE){
                Intent searchAllowIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                searchAllowIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,60);
                startActivity(searchAllowIntent);
            }
        }
    }
}
