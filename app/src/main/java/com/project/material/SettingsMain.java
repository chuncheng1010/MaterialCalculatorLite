package com.project.material;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SettingsMain extends Activity {

    Button userpreference, metrialcost, plant;
    ImageView back, home, folder;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Auto-generated method stub
        setContentView(R.layout.settingsmain);
        userpreference = (Button) findViewById(R.id.button5);
        metrialcost = (Button) findViewById(R.id.button1);
        plant = (Button) findViewById(R.id.button3);

        back = (ImageView) findViewById(R.id.imageView1);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(v.getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
        home = (ImageView) findViewById(R.id.imageView2);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(v.getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
        folder = (ImageView) findViewById(R.id.imageView3);
        folder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(v.getContext(), JobMenuActivity.class);
                startActivity(intent);
            }
        });

        userpreference.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                // TODO Auto-generated method stub
                Intent intent = new Intent(v.getContext(),
                        CalculatorSetting1.class);
                startActivityForResult(intent, 0);
                */
            }
        });
        metrialcost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*
				 * SharedPreferences shared; shared =
				 * getSharedPreferences("calculator",MODE_PRIVATE); String str =
				 * shared.getString("method",""); Intent intent;
				 * if(str.equals("metric")) { intent = new
				 * Intent(v.getContext(), CalculatorSetting2.class);
				 * intent.putExtra("method", "metric");
				 * startActivityForResult(intent, 0); return; }
				 * if(str.equals("imperial")) { intent = new
				 * Intent(v.getContext(), CalculatorSetting2.class);
				 * intent.putExtra("method", "imperial");
				 * startActivityForResult(intent, 0); return;
				 * 
				 * } else { Toast.makeText(getBaseContext(),
				 * "Please first select User Preferences",
				 * Toast.LENGTH_SHORT).show(); intent = new
				 * Intent(v.getContext(), CalculatorSetting1.class);
				 * startActivityForResult(intent, 0); return; }
				 */
                /*
                Intent intent = new Intent(v.getContext(), MaterialCost.class);
                startActivityForResult(intent, 0);
                */
            }
        });

        plant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(v.getContext(), PlanEquipment.class);
                startActivityForResult(intent, 0);
                return;
            }
        });
    }

}
