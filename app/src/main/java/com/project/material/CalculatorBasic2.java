package com.project.material;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.material.helper.DatabaseHelper;
import com.project.material.helper.Fraction;

public class CalculatorBasic2 extends Activity {

    /**
     * Called when the activity is first created.
     */

    EditText length1, depth1, length2, depth2, joint;
    TextView length1unit, depth1unit, length2unit, depth2unit, jointunit,
            answer, title, sqrt, sqrt3, sqrt1, sqrt33;
    EditText tiles, totalarea, totaltiles, totalspaces, totalglue, grout;
    TextView tileslabel, totalarealabel, totaltileslabel, totalspaceslabel,
            totalgluelabel, groutunit;
    TextView tilesunit, totalareaunit, totaltilesunit, totalspacesunit,
            totalglueunit;
    ImageView back, home, folder;
    SharedPreferences shared;
    SharedPreferences.Editor edit;
    int flag = 0;
    String method, type;
    int main, up, down;
    Fraction frac;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Auto-generated method stub
        setContentView(R.layout.calculatorbasic1);
        answer = (TextView) findViewById(R.id.text_answer);
        String mystring = new String("6. Press for Calculator Answers");
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        answer.setText(content);

		/*
         * sqrt = (TextView)findViewById(R.id.sqrt); sqrt3 =
		 * (TextView)findViewById(R.id.sqrt3); sqrt33 =
		 * (TextView)findViewById(R.id.sqrt33); sqrt1 =
		 * (TextView)findViewById(R.id.sqrt1);
		 */
        Spanned htmlText = Html.fromHtml("&#178;");
        // Spanned htmlText1 = Html.fromHtml("&#179;");

		/*
		 * sqrt.setText("M" + htmlText); sqrt1.setText("M" + htmlText);
		 * sqrt3.setText("M" + htmlText1); sqrt33.setText("M" + htmlText1);
		 */

        back = (ImageView) findViewById(R.id.imageView1);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag = 1;
                datasaveDialog();
                // finish();
            }
        });
        home = (ImageView) findViewById(R.id.imageView2);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag = 2;
                datasaveDialog();
				/*
				 * Intent intent = new Intent(); intent.setClass(v.getContext(),
				 * HomeActivity.class); startActivity(intent);
				 */
            }
        });
        folder = (ImageView) findViewById(R.id.imageView3);
        folder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                flag = 3;
                datasaveDialog();
				/*
				 * Intent intent = new Intent(); intent.setClass(v.getContext(),
				 * JobMenuActivity.class); startActivity(intent);
				 */
            }
        });

        shared = getSharedPreferences("calculator", MODE_PRIVATE);
        edit = shared.edit();

        length1 = (EditText) findViewById(R.id.length1);

        depth1 = (EditText) findViewById(R.id.depth1);

        length2 = (EditText) findViewById(R.id.length2);

        depth2 = (EditText) findViewById(R.id.depth2);

        joint = (EditText) findViewById(R.id.joint);

        title = (TextView) findViewById(R.id.text_title);

        method = shared.getString("method", "metric");
        type = shared.getString("spinnerMaterial", "protected");

        length1unit = (TextView) findViewById(R.id.length1unit);
        depth1unit = (TextView) findViewById(R.id.depth1unit);

        length2unit = (TextView) findViewById(R.id.length2unit);
        depth2unit = (TextView) findViewById(R.id.depth2unit);
        jointunit = (TextView) findViewById(R.id.jointunit);
        groutunit = (TextView) findViewById(R.id.groutunit);

        tileslabel = (TextView) findViewById(R.id.tileslabel);
        totalarealabel = (TextView) findViewById(R.id.totalarealabel);
        totaltileslabel = (TextView) findViewById(R.id.totaltileslabel);
        totalspaceslabel = (TextView) findViewById(R.id.totalspacelabel);
        totalgluelabel = (TextView) findViewById(R.id.totalgluelabel);

        tilesunit = (TextView) findViewById(R.id.tilesunit);
        totalareaunit = (TextView) findViewById(R.id.totalareaunit);
        totaltilesunit = (TextView) findViewById(R.id.totaltilessunit);
        totalspacesunit = (TextView) findViewById(R.id.totalspaceunit);
        totalglueunit = (TextView) findViewById(R.id.totalglueunit);

        if (method.equals("metric")) {
            length1unit.setText("METERS");
            depth1unit.setText("METERS");
            length2unit.setText("MM");
            depth2unit.setText("MM");
            jointunit.setText("MM");
            groutunit.setText("Kg");

            tileslabel.setText(String.format("Tiles - (Per M%s)", htmlText));
            totalarealabel.setText("Total Area");
            totaltileslabel.setText("Total Tiles");
            totalspaceslabel.setText("Total Spaces");
            totalgluelabel.setText("Total Glue");

            tilesunit.setText("Qty");
            totalareaunit.setText("M" + htmlText);
            totaltilesunit.setText("Qty");
            totalspacesunit.setText("Qty");
            totalglueunit.setText("Kg");

            if (type.equals("wall")) {
                title.setText("Wall Tiling - Metric");

            }
        } else {
            length1unit.setText("FEET");
            depth1unit.setText("FEET");
            length2unit.setText("INCHES");
            depth2unit.setText("INCHES");
            jointunit.setText("INCH");
            groutunit.setText("Lbs");

            tileslabel.setText(String.format("Tiles - (Per Ft%s)", htmlText));
            totalarealabel.setText("Total Area");
            totaltileslabel.setText("Total Tiles");
            totalspaceslabel.setText("Total Spaces");
            totalgluelabel.setText("Total Glue");

            tilesunit.setText("Qty");
            totalareaunit.setText("Ft" + htmlText);
            totaltilesunit.setText("Qty");
            totalspacesunit.setText("Qty");
            totalglueunit.setText("Lbs");

            if (type.equals("wall")) {
                title.setText("Wall Tiling - Imperial");

            }
        }

        tiles = (EditText) findViewById(R.id.tiles);
        totalarea = (EditText) findViewById(R.id.totalarea);
        totaltiles = (EditText) findViewById(R.id.totaltitles);
        totalspaces = (EditText) findViewById(R.id.totalspace);
        totalglue = (EditText) findViewById(R.id.totalglue);
        grout = (EditText) findViewById(R.id.grout);

        answer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                double checkval = 0, length1val, depth1val, length2val, depth2val, jointval, value, valuearea, valuetotaltiles;
                if (length1.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(),
                            "Please input Area Length", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (depth1.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(),
                            "Please input Area Height", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (length2.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(),
                            "Please input Tile Length", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (depth2.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Please input Depth",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (joint.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Please input Joint",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (method.equals("metric")) {
                    // protected metric
                    if (type.equals("wall")) {
                        try {
                            checkval = Double.parseDouble(length1.getText()
                                    .toString());
                            if (checkval < 0.05 || checkval > 1000) {
                                Toast.makeText(getBaseContext(),
                                        "Please input correct Area Length",
                                        Toast.LENGTH_SHORT).show();
                                length1.setText("");
                                return;
                            }
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(),
                                    "Please input correct Area Length",
                                    Toast.LENGTH_SHORT).show();
                            length1.setText("");
                            return;
                        }

                        try {
                            checkval = Double.parseDouble(depth1.getText()
                                    .toString());
                            if (checkval < 0.05 || checkval > 1000) {
                                Toast.makeText(getBaseContext(),
                                        "Please input correct Area Height",
                                        Toast.LENGTH_SHORT).show();
                                depth1.setText("");
                                return;
                            }
                        } catch (Exception e) {

                            Toast.makeText(getBaseContext(),
                                    "Please input correct Area Height",
                                    Toast.LENGTH_SHORT).show();
                            depth1.setText("");
                            return;

                        }

                        try {
                            checkval = Double.parseDouble(length2.getText()
                                    .toString());
                            if (checkval < 1 || checkval > 1000) {
                                Toast.makeText(getBaseContext(),
                                        "Please input correct Tile Length",
                                        Toast.LENGTH_SHORT).show();
                                length2.setText("");
                                return;
                            }
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(),
                                    "Please input correct Tile Length",
                                    Toast.LENGTH_SHORT).show();
                            length2.setText("");
                            return;
                        }

                        try {
                            checkval = Double.parseDouble(depth2.getText()
                                    .toString());
                            if (checkval < 1 || checkval > 1000) {
                                Toast.makeText(getBaseContext(),
                                        "Please input correct Depth",
                                        Toast.LENGTH_SHORT).show();
                                depth2.setText("");
                                return;
                            }
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(),
                                    "Please input correct Depth",
                                    Toast.LENGTH_SHORT).show();
                            depth2.setText("");
                            return;
                        }

                        try {
                            checkval = Double.parseDouble(joint.getText()
                                    .toString());
                            if (checkval < 0 || checkval > 25) {
                                Toast.makeText(getBaseContext(),
                                        "Please input correct Joint",
                                        Toast.LENGTH_SHORT).show();
                                joint.setText("");
                                return;
                            }
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(),
                                    "Please input correct Joint",
                                    Toast.LENGTH_SHORT).show();
                            joint.setText("");
                            return;
                        }

                        length1.setText(String.format("%.3f", Double
                                .parseDouble(length1.getText().toString())));
                        depth1.setText(String.format("%.3f",
                                Double.parseDouble(depth1.getText().toString())));
                        length2.setText(String.format("%d", (int) Double
                                .parseDouble(length2.getText().toString())));
                        depth2.setText(String.format("%d", (int) Double
                                .parseDouble(depth2.getText().toString())));
                        joint.setText(String.format("%.1f",
                                Double.parseDouble(joint.getText().toString())));

                        // calcuate values
                        length1val = Double.parseDouble(length1.getText()
                                .toString());
                        depth1val = Double.parseDouble(depth1.getText()
                                .toString());
                        length2val = Double.parseDouble(length2.getText()
                                .toString());
                        depth2val = Double.parseDouble(depth2.getText()
                                .toString());
                        jointval = Double.parseDouble(joint.getText()
                                .toString());

                        value = length1val * depth1val;
                        valuearea = value;
                        totalarea.setText(String.format("%.2f",
                                Math.round(value * 100) / 100.0));

                        value = value
                                / ((length2val + jointval) / 1000
                                * (depth2val + jointval) / 1000);
                        valuetotaltiles = value;
                        totaltiles.setText(String.format("%.1f",
                                Math.round(value * 10) / 10.0));

                        value = valuetotaltiles / valuearea;
                        tiles.setText(String.format("%.1f",
                                Math.round(value * 10) / 10.0));

                        value = valuearea * 25;
                        totalspaces.setText(String.format("%d",
                                Math.round(value)));

                        value = (valuearea / 6.5) * 20;
                        totalglue.setText(String.format("%.1f",
                                Math.round(value * 10) / 10.0));

                        value = valuearea * 1.2;
                        grout.setText(String.format("%.1f",
                                Math.round(value * 10) / 10.0));

                    }

                } else {
                    Fraction length1fraction, depth1fraction, length2fraction, depth2fraction, jointfraction;
                    // imperial protected
                    if (type.equals("wall")) {
                        int ret = checkCompleteValue(length1, new Fraction(1,
                                0, 0), new Fraction(50, 0, 0));
                        if (ret == 0) {
                            length1.setText("");
                            Toast.makeText(getBaseContext(),
                                    "Please input correct data.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else if (ret == 1) {
                            length1.setText("");
                            Toast.makeText(getBaseContext(),
                                    "Data Range is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        length1fraction = new Fraction(main, up, down);

                        ret = checkCompleteValue(depth1, new Fraction(1, 0, 0),
                                new Fraction(50, 0, 0));
                        if (ret == 0) {
                            depth1.setText("");
                            Toast.makeText(getBaseContext(),
                                    "Please input correct data.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else if (ret == 1) {
                            depth1.setText("");
                            Toast.makeText(getBaseContext(),
                                    "Data Range is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        depth1fraction = new Fraction(main, up, down);

                        ret = checkCompleteValue(length2,
                                new Fraction(1, 0, 0), new Fraction(36, 0, 0));
                        if (ret == 0) {
                            length2.setText("");
                            Toast.makeText(getBaseContext(),
                                    "Please input correct data.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else if (ret == 1) {
                            length2.setText("");
                            Toast.makeText(getBaseContext(),
                                    "Data Range is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        length2fraction = new Fraction(main, up, down);

                        ret = checkCompleteValue(depth2, new Fraction(1, 0, 0),
                                new Fraction(36, 0, 0));
                        if (ret == 0) {
                            depth2.setText("");
                            Toast.makeText(getBaseContext(),
                                    "Please input correct data.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else if (ret == 1) {
                            depth2.setText("");
                            Toast.makeText(getBaseContext(),
                                    "Data Range is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        depth2fraction = new Fraction(main, up, down);

                        ret = checkCompleteValue(joint, new Fraction(0, 0, 0),
                                new Fraction(1, 0, 0));
                        if (ret == 0) {
                            joint.setText("");
                            Toast.makeText(getBaseContext(),
                                    "Please input correct data.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else if (ret == 1) {
                            joint.setText("");
                            Toast.makeText(getBaseContext(),
                                    "Data Range is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        jointfraction = new Fraction(main, up, down);

                        length1val = (length1fraction.main * 16 + length1fraction.up) / 16.0;
                        depth1val = (depth1fraction.main * 16 + depth1fraction.up) / 16.0;
                        length2val = (length2fraction.main * 16 + length2fraction.up) / 16.0;
                        depth2val = (depth2fraction.main * 16 + depth2fraction.up) / 16.0;

                        jointval = (jointfraction.main * 16 + jointfraction.up) / 16.0;

                        value = length1val * depth1val;
                        valuearea = value;
                        totalarea.setText(String.format("%.2f",
                                Math.round(value * 100) / 100.0));

                        value = value
                                / ((length2val + jointval) / 12
                                * (depth2val + jointval) / 12);
                        valuetotaltiles = value;
                        totaltiles.setText(String.format("%.1f",
                                Math.round(value * 10) / 10.0));

                        value = valuetotaltiles / valuearea;
                        tiles.setText(String.format("%.1f",
                                Math.round(value * 10) / 10.0));

                        value = valuearea * 2.35;
                        totalspaces.setText(String.format("%d",
                                Math.round(value)));

                        value = (valuearea / 69.9654) * 44;
                        totalglue.setText(String.format("%.1f",
                                Math.round(value * 10) / 10.0));

                        value = valuearea * 0.3;
                        grout.setText(String.format("%.1f",
                                Math.round(value * 10) / 10.0));

                    }

                }

            }
        });
    }

    public int checkCompleteValue(EditText edit, Fraction min, Fraction max) {

        double value;
        String str = edit.getText().toString();
        String temp;
        Pattern pat = Pattern.compile("(^[0-9]+)\\p{Space}([0-9]+)/([1-9]+)$");
        Matcher m = pat.matcher(str);
        if (m.find()) {
            main = Integer.parseInt(m.group(1));
            up = Integer.parseInt(m.group(2));
            down = Integer.parseInt(m.group(3));
            main += up / down;
            value = up / (down * 1.0);
            // value = Math.round(value * 10)/10.0;
            down = 16;
            up = (int) Math.round(value * 16);
            // System.out.println(main + " " + up + " " + down);
            if (main * 16 + up < min.main * 16 + min.up)
                return 1;
            if (main * 16 + up > max.main * 16 + max.up)
                return 1;

        } else {
            pat = Pattern.compile("(^[0-9]+)\\.([0-9]+)$");
            m = pat.matcher(str);
            if (m.find()) {
                main = Integer.parseInt(m.group(1));
                temp = String.format("%s.%s", m.group(1), m.group(2));
                value = Double.parseDouble(temp);
                value = value - main;
                down = 16;
                up = (int) Math.round(value * 16);
                // System.out.println(main + " " + up + " " + down);
            } else {
                pat = Pattern.compile("(^[0-9]+)/([1-9]+)$");
                m = pat.matcher(str);
                if (m.find()) {
                    main = Integer.parseInt(m.group(1))
                            / Integer.parseInt(m.group(2));
                    up = Integer.parseInt(m.group(1)) - main
                            * Integer.parseInt(m.group(2));
                    value = (up * 1.0) / (Integer.parseInt(m.group(2)) * 1.0);
                    down = 16;
                    up = (int) Math.round(value * 16);

                } else {
                    pat = Pattern.compile("(^[0-9]+)$");
                    m = pat.matcher(str);
                    if (m.find()) {
                        main = Integer.parseInt(m.group(0));
                        up = 0;
                        down = 16;
                    } else
                        return 0;
                }
            }

        }
        System.out.println(main + " " + up + " " + down);
        if (main * 16 + up < min.main * 16 + min.up)
            return 1;
        if (main * 16 + up > max.main * 16 + max.up)
            return 1;
        edit.setText(String.format("%s", display(main, up, 16)));
        return 2;
    }

    public String display(int m, int u, int d) {
        if (m == 0 && u == 0)
            return String.format("%d", 0);
        if (m != 0 && u != 0)
            return String.format("%d %d/%d", m, u, 16);
        else if (m == 0)
            return String.format("%d/%d", u, 16);
        else
            return String.format("%d", m);
    }

    public void noMessage() {
        Intent intent;
        switch (flag) {
            case 1:
                finish();
                break;
            case 2:
                intent = new Intent();
                intent.setClass(CalculatorBasic2.this, HomeActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent();
                intent.setClass(CalculatorBasic2.this, JobMenuActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void datasaveDialog() {
        if (totalarea.getText().toString().equals("")) {
            noMessage();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Save")
                .setMessage("Would you like to save calculator answers?")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub

                                datasave();

                            }
                        }
                )
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent;
                        switch (flag) {
                            case 1:
                                finish();
                                break;
                            case 2:
                                intent = new Intent();
                                intent.setClass(CalculatorBasic2.this,
                                        HomeActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent();
                                intent.setClass(CalculatorBasic2.this,
                                        JobMenuActivity.class);
                                startActivity(intent);
                                break;
                        }

                    }
                }).show();
    }

    public void datasave() {

        final EditText input = new EditText(CalculatorBasic2.this);

        input.setMaxLines(1);

        new AlertDialog.Builder(CalculatorBasic2.this)
                .setTitle("Job Details")
                .setMessage("Please enter job name.")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String number, message;
                        SQLiteDatabase localSQLiteDatabase = null;
                        try {
                            localSQLiteDatabase = new DatabaseHelper(
                                    CalculatorBasic2.this,
                                    getString(R.string.app_name), null, 1)
                                    .getWritableDatabase();
                            ContentValues localContentValues = new ContentValues();

                            if (input.getText().toString().length() == 0) {
                                Toast.makeText(getBaseContext(),
                                        "Can not save the data.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            localContentValues.put("title", input.getText()
                                    .toString());
                            localContentValues.put("date", "");
                            localContentValues.put("name", "");
                            localContentValues.put("address", "");
                            localContentValues.put("phone", "");
                            localContentValues.put("email", "");
                            localContentValues.put("margin", "");
                            localContentValues.put("tax", "");
                            localContentValues.put("method", method);
                            localContentValues.put("spinnerMaterial", type);

                            localContentValues.put("depth1", depth1.getText()
                                    .toString());
                            localContentValues.put("length1", length1.getText()
                                    .toString());
                            localContentValues.put("length2", length2.getText()
                                    .toString());
                            localContentValues.put("depth2", depth2.getText()
                                    .toString());
                            localContentValues.put("depth1", depth1.getText()
                                    .toString());
                            localContentValues.put("joint", joint.getText()
                                    .toString());

                            localSQLiteDatabase.insert("jobstable", null,
                                    localContentValues);
                            localSQLiteDatabase.close();
                            Intent intent;
                            switch (flag) {
                                case 1:
                                    finish();
                                    break;
                                case 2:
                                    intent = new Intent();
                                    intent.setClass(CalculatorBasic2.this,
                                            HomeActivity.class);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    intent = new Intent();
                                    intent.setClass(CalculatorBasic2.this,
                                            JobMenuActivity.class);
                                    startActivity(intent);
                                    break;
                            }
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(),
                                    "Occured error while saving data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Do nothing.
                                Intent intent;
                                switch (flag) {
                                    case 1:
                                        finish();
                                        break;
                                    case 2:
                                        intent = new Intent();
                                        intent.setClass(CalculatorBasic2.this,
                                                HomeActivity.class);
                                        startActivity(intent);
                                        break;
                                    case 3:
                                        intent = new Intent();
                                        intent.setClass(CalculatorBasic2.this,
                                                JobMenuActivity.class);
                                        startActivity(intent);
                                        break;
                                }
                            }
                        }
                ).show();

    }

}
