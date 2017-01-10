package com.project.material;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends Activity {

    Button back;
    String[] str;
    TextView textview;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        // TODO Auto-generated method stub
        back = (Button) findViewById(R.id.button2);
        textview = (TextView) findViewById(R.id.textView2);
        str = getIntent().getStringArrayExtra("value");

        String mystring = "";
        mystring = "Preference: " + str[0] + "<br>";
        mystring += "Date: " + str[12] + "<br>";
        mystring += "Width1: " + str[1] + "<br>";
        mystring += "Height1: " + str[2] + "<br>";
        mystring += "Width2: " + str[3] + "<br>";
        mystring += "Height2: " + str[4] + "<br>";
        mystring += "Paving Job: " + str[5] + "<br>";
        mystring += "Paving Stones: " + str[6] + "<br>";
        mystring += "Sub-Surface: " + str[7] + "<br>";
        mystring += "Bedding Sand: " + str[8] + "<br>";
        mystring += "Joint Sand: " + str[9] + "<br>";
        mystring += "Cement Edge: " + str[10] + "<br>";
        mystring += "Project Time: " + str[11] + "<br>";
        textview.setText(Html.fromHtml(mystring));

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

}
