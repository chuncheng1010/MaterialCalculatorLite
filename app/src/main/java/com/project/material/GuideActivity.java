package com.project.material;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GuideActivity extends Activity {

    Button how, tool, instruction, lay;
    ImageView back, home, folder;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides);

        how = (Button) findViewById(R.id.button1);
        tool = (Button) findViewById(R.id.button2);
        instruction = (Button) findViewById(R.id.button3);
        back = (ImageView) findViewById(R.id.imageView1);
        lay = (Button) findViewById(R.id.button5);

        how.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TextActivity.class);
                intent.putExtra("pos", "0");
                startActivityForResult(intent, 0);
            }
        });

        tool.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TextActivity.class);
                intent.putExtra("pos", "1");
                startActivityForResult(intent, 0);
            }
        });

        instruction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TextActivity.class);
                intent.putExtra("pos", "3");
                startActivityForResult(intent, 0);
            }
        });

        back = (ImageView) findViewById(R.id.imageView1);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home = (ImageView) findViewById(R.id.imageView2);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        folder = (ImageView) findViewById(R.id.imageView3);
        folder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), JobMenuActivity.class);
                startActivity(intent);
            }
        });

        lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TextActivity.class);
                intent.putExtra("pos", "4");
                startActivityForResult(intent, 0);
            }
        });
    }

}
