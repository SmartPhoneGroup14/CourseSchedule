package cs.hku.group14.schedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText grade1,grade2,grade3;
    private Button btn_Calculate;
    private TextView GPAresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        initView();
    }
    private void initView() {
        btn_Calculate = (Button) findViewById(R.id.btn_calcGPA);
        grade1 = (EditText) findViewById(R.id.grade1);
        grade2 = (EditText) findViewById(R.id.grade2);
        grade3 = (EditText) findViewById(R.id.grade3);
        GPAresult = (TextView)findViewById(R.id.GPAresult);

        btn_Calculate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_calcGPA) {
            int grade_1=Integer.parseInt(grade1.getText().toString());
            int grade_2=Integer.parseInt(grade2.getText().toString());
            int grade_3=Integer.parseInt(grade3.getText().toString());
            float average = (float) (grade_1+grade_2+grade_3)/3;
            Double gpa = 1.0;
            if(average>85)
                gpa=4.0;
            else if(average<60)
                gpa=1.0;
            else {
                gpa = 1.5 + (average - 60) * 0.1;
            }
            String result = gpa.toString();
            GPAresult.setText(result);
        }
    }
}
