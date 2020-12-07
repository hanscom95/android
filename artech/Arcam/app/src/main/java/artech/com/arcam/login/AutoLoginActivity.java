package artech.com.arcam.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import artech.com.arcam.MainActivity;
import artech.com.arcam.R;
import artech.com.arcam.utility.DBManager;


public class AutoLoginActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDbManager;

    Button mStartButton;
    TextView mNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mDbManager = new DBManager(mContext, "arcam.db", null, 1);

        setContentView(R.layout.content_autologin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bluebg));
        }

        mNameText = (TextView) findViewById(R.id.name_text);

        mStartButton = (Button) findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String name = mDbManager.selectId();
        if (!"".equals(name)) {
            mNameText.setText(name);
        }
    }
}
