package cs.hku.group14.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText grade1,grade2,grade3;
    private Button btn_Calculate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
    }
    private void initView() {
        btn_Calculate = (Button) findViewById(R.id.btn_calcGPA);
        grade1 = (EditText) findViewById(R.id.grade1);
        grade2 = (EditText) findViewById(R.id.grade2);
        grade3 = (EditText) findViewById(R.id.grade3);

        btn_Calculate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
