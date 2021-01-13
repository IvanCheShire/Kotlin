package ru.geekbrains.kotlin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MyActivity extends Activity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mTextView = (TextView) findViewById(R.id.textView);
    }
    public void onClick(View view) {
        mTextView.setText("Здравствуй товарищ!");
    }
}