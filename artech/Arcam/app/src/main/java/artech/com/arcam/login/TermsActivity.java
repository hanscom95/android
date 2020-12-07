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
import android.widget.Toast;

import artech.com.arcam.R;


public class TermsActivity extends AppCompatActivity {

    Context mContext;

    Button mCloseButton, mBeforeButton, mNextButton;
    CheckBox mAllCheckBox, mFirstCheckBox, mSecondCheckBox, mThirdCheckBox, mFourthCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.content_terms);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bluebg));
        }

        mCloseButton = (Button) findViewById(R.id.close_button);
        mBeforeButton = (Button) findViewById(R.id.before_button);
        mNextButton = (Button) findViewById(R.id.next_button);

        mAllCheckBox = (CheckBox) findViewById(R.id.terms_all_check);
        mFirstCheckBox = (CheckBox) findViewById(R.id.terms_first_check);
        mSecondCheckBox = (CheckBox) findViewById(R.id.terms_second_check);
        mThirdCheckBox = (CheckBox) findViewById(R.id.terms_third_check);
        mFourthCheckBox = (CheckBox) findViewById(R.id.terms_fourth_check);


        CompoundButton.OnClickListener onClickListener = new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.terms_all_check:
                        if(mAllCheckBox.isChecked()) {
                            mFirstCheckBox.setChecked(true);
                            mSecondCheckBox.setChecked(true);
                            mThirdCheckBox.setChecked(true);
                            mFourthCheckBox.setChecked(true);
                        }else {
                            mFirstCheckBox.setChecked(false);
                            mSecondCheckBox.setChecked(false);
                            mThirdCheckBox.setChecked(false);
                            mFourthCheckBox.setChecked(false);
                        }
                        break;
                    case R.id.terms_first_check:
                        if(!mFirstCheckBox.isChecked()) {
                            mAllCheckBox.setChecked(false);
                        }
                        break;
                    case R.id.terms_second_check:
                        if(!mSecondCheckBox.isChecked()) {
                            mAllCheckBox.setChecked(false);
                        }
                        break;
                    case R.id.terms_third_check:
                        if(!mThirdCheckBox.isChecked()) {
                            mAllCheckBox.setChecked(false);
                        }
                        break;
                    case R.id.terms_fourth_check:
                        if(!mFourthCheckBox.isChecked()) {
                            mAllCheckBox.setChecked(false);
                        }
                        break;
                }

                if(mFirstCheckBox.isChecked() && mSecondCheckBox.isChecked() && mThirdCheckBox.isChecked() && mFourthCheckBox.isChecked()) {
                    mAllCheckBox.setChecked(true);
                }
            }
        };

        mAllCheckBox.setOnClickListener(onClickListener);
        mFirstCheckBox.setOnClickListener(onClickListener);
        mSecondCheckBox.setOnClickListener(onClickListener);
        mThirdCheckBox.setOnClickListener(onClickListener);
        mFourthCheckBox.setOnClickListener(onClickListener);

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBeforeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFirstCheckBox.isChecked() && mSecondCheckBox.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(mContext, getText(R.string.terms_required_check).toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
