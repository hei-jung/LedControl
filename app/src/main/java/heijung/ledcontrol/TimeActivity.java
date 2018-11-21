package heijung.ledcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeActivity extends Activity {
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm", Locale.KOREA);
    String formatDate = sdfNow.format(date);

    TextView dateNow;
    Button RefreshBtn;

    private String getTime(){
        now = System.currentTimeMillis();
        date = new Date(now);
        return sdfNow.format(date);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        dateNow = (TextView)findViewById(R.id.dateNow);
        dateNow.setText(formatDate);

        RefreshBtn = (Button)findViewById(R.id.refreshBtn);

        RefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.refreshBtn:
                        dateNow.setText(getTime());
                        break;
                    default:
                        break;
                }
            }
        });
    }


}
