package xute.progressdotsSample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import xute.progressdot.ProgressDotView;

public class MainActivity extends AppCompatActivity {

    ProgressDotView progressDotView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDotView = findViewById(R.id.dotProgressView);
    }

    public void moveToNext(View view) {
        progressDotView.moveToNext();
    }
}
