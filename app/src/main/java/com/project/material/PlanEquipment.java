package com.project.material;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PlanEquipment extends Activity {
    TextView labor, method;
    ImageView back, home, folder;
    EditText compactorvalue, cuttingsawvalue, plantother1value, toolsvalue,
            fuelvalue;
    EditText equipother1value, equipother2value, plantother2value;
    EditText compactorlabel, cuttingsawlabel, plantother1label,
            plantother2label, toolslabel, fuellabel;
    EditText equipother1label, equipother2label;
    SharedPreferences shared;
    SharedPreferences.Editor edit;
    int flag = 0;

    String compactorvaluestr, compactorlabelstr, cuttingsawvaluestr,
            cuttingsawlabelstr;
    String plantother1valuestr, plantother1labelstr, plantother2valuestr,
            plantother2labelstr;
    String toolsvaluestr, toolslabelstr, fuelvaluestr, fuellabelstr;
    String equipother1valuestr, equipother1labelstr, equipother2valuestr,
            equipother2labelstr;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Auto-generated method stub
        setContentView(R.layout.plantequipment);
        labor = (TextView) findViewById(R.id.labour);
        method = (TextView) findViewById(R.id.method);
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
                /*
				 * Intent intent = new Intent(); intent.setClass(v.getContext(),
				 * HomeActivity.class); startActivity(intent);
				 */
                flag = 2;
                datasaveDialog();
            }
        });
        folder = (ImageView) findViewById(R.id.imageView3);
        folder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				/*
				 * Intent intent = new Intent(); intent.setClass(v.getContext(),
				 * JobMenuActivity.class); startActivity(intent);
				 */
                flag = 3;
                datasaveDialog();
            }
        });

        String mystring = new String("Equipment Costs");
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        labor.setText(content);
        mystring = "Plant Costs";
        content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        method.setText(content);

        compactorvalue = (EditText) findViewById(R.id.compactorval);
        cuttingsawvalue = (EditText) findViewById(R.id.cuttingsawvalue);
        plantother1value = (EditText) findViewById(R.id.plantother1value);
        plantother2value = (EditText) findViewById(R.id.plantother2value);

        compactorlabel = (EditText) findViewById(R.id.compactorlabel);
        cuttingsawlabel = (EditText) findViewById(R.id.cuttingsawlabel);
        plantother1label = (EditText) findViewById(R.id.plantother1label);
        plantother2label = (EditText) findViewById(R.id.plantother2label);

        toolsvalue = (EditText) findViewById(R.id.toolsvalue);
        fuelvalue = (EditText) findViewById(R.id.fuelvalue);
        equipother1value = (EditText) findViewById(R.id.equipother1value);
        equipother2value = (EditText) findViewById(R.id.equipother2value);

        toolslabel = (EditText) findViewById(R.id.toolslabel);
        fuellabel = (EditText) findViewById(R.id.fuellabel);
        equipother1label = (EditText) findViewById(R.id.equipother1label);
        equipother2label = (EditText) findViewById(R.id.equipother2label);

        shared = getSharedPreferences("calculator", MODE_PRIVATE);
        edit = shared.edit();

        getPreferenceValue();
        valueSetting();
        compactorvalue.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                // pavers,roadbase,beddingsand,jointsand,cementedge
                valueSetting(compactorvalue, hasFocus, 1000, "$50.00", 2);
            }
        });
        cuttingsawvalue.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                // pavers,roadbase,beddingsand,jointsand,cementedge
                valueSetting(cuttingsawvalue, hasFocus, 1000, "$0.00", 2);
            }
        });
        plantother1value.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                // pavers,roadbase,beddingsand,jointsand,cementedge
                valueSetting(plantother1value, hasFocus, 1000, "$0.00", 2);
            }
        });
        plantother2value.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                // pavers,roadbase,beddingsand,jointsand,cementedge
                valueSetting(plantother2value, hasFocus, 1000, "$0.00", 2);
            }
        });
        toolsvalue.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                // pavers,roadbase,beddingsand,jointsand,cementedge
                valueSetting(toolsvalue, hasFocus, 1000, "$50.00", 2);
            }
        });
        fuelvalue.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                // pavers,roadbase,beddingsand,jointsand,cementedge
                valueSetting(fuelvalue, hasFocus, 1000, "$0.00", 2);
            }
        });
        equipother1value.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                // pavers,roadbase,beddingsand,jointsand,cementedge
                valueSetting(equipother1value, hasFocus, 1000, "$0.00", 2);
            }
        });
        equipother2value.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                // pavers,roadbase,beddingsand,jointsand,cementedge
                valueSetting(equipother2value, hasFocus, 1000, "$0.00", 2);
            }
        });

    }

    public void labelSetting(EditText e, boolean state, int max) {
        String str;
        if (max > 0) {
            if (e.getText().toString().length() > max) {
                str = e.getText().toString().substring(0, max);
                e.setText(str);
            }
        }

    }

    public void valueSetting(EditText e, boolean state, double max, String def,
                             int num) {
        String s;
        if (state == true) {
            s = e.getText().toString();
            s = s.replace('$', 'a');
            s = s.replaceAll("a", "");
            e.setText(s);
            return;
        } else {

            s = e.getText().toString().replace('$', 'a');
            s = s.replaceAll("a", "");
            if (s.equals("")) {
                e.setText(def);
                return;
            }

            if (Double.parseDouble(s) > max) {
                e.setText(def);
                return;
            } else {
                e.setText(String.format("$%.2f", Double.parseDouble(s)));
                return;
            }
        }

    }

    public void getPreferenceValue() {

        compactorvaluestr = shared.getString("compactorvalue", "");
        compactorlabelstr = shared.getString("compactorlabel", "");
        cuttingsawvaluestr = shared.getString("cuttingsawvalue", "");
        cuttingsawlabelstr = shared.getString("cuttingsawlabel", "");
        plantother1valuestr = shared.getString("plantother1value", "");
        plantother1labelstr = shared.getString("plantother1label", "");
        plantother2valuestr = shared.getString("plantother2value", "");
        plantother2labelstr = shared.getString("plantother2label", "");

        toolsvaluestr = shared.getString("toolsvalue", "");
        toolslabelstr = shared.getString("toolslabel", "");
        fuelvaluestr = shared.getString("fuelvalue", "");
        fuellabelstr = shared.getString("fuellabel", "");
        equipother1valuestr = shared.getString("equipother1value", "");
        equipother1labelstr = shared.getString("equipother1label", "");
        equipother2valuestr = shared.getString("equipother2value", "");
        equipother2labelstr = shared.getString("equipother2label", "");

    }

    public void valueSetting() {
        // compactor
        if (compactorvaluestr.equals(""))
            compactorvalue.setText("$50.00");
        else
            compactorvalue.setText("$" + compactorvaluestr);
        if (compactorlabelstr.equals(""))
            compactorlabel.setText("Plant");
        else
            compactorlabel.setText(compactorlabelstr);

        // cuttingsaw
        if (cuttingsawvaluestr.equals(""))
            cuttingsawvalue.setText("$0.00");
        else
            cuttingsawvalue.setText("$" + cuttingsawvaluestr);
        if (cuttingsawlabelstr.equals(""))
            cuttingsawlabel.setText("Other");
        else
            cuttingsawlabel.setText(cuttingsawlabelstr);

        // plantother1
        if (plantother1valuestr.equals(""))
            plantother1value.setText("$0.00");
        else
            plantother1value.setText("$" + plantother1valuestr);
        if (plantother1labelstr.equals(""))
            plantother1label.setText("Other");
        else
            plantother1label.setText(plantother1labelstr);

        // plantother2
        if (plantother2valuestr.equals(""))
            plantother2value.setText("$0.00");
        else
            plantother2value.setText("$" + plantother2valuestr);
        if (plantother2labelstr.equals(""))
            plantother2label.setText("Other");
        else
            plantother2label.setText(plantother2labelstr);

        // tools
        if (toolsvaluestr.equals(""))
            toolsvalue.setText("$50.00");
        else
            toolsvalue.setText("$" + toolsvaluestr);
        if (toolslabelstr.equals(""))
            toolslabel.setText("Equipment");
        else
            toolslabel.setText(toolslabelstr);

        // fuel
        if (fuelvaluestr.equals(""))
            fuelvalue.setText("$0.00");
        else
            fuelvalue.setText("$" + fuelvaluestr);
        if (fuellabelstr.equals(""))
            fuellabel.setText("Other");
        else
            fuellabel.setText(fuellabelstr);

        // equipother1
        if (equipother1valuestr.equals(""))
            equipother1value.setText("$0.00");
        else
            equipother1value.setText("$" + equipother1valuestr);
        if (equipother1labelstr.equals(""))
            equipother1label.setText("Other");
        else
            equipother1label.setText(equipother1labelstr);

        // equipother2
        if (equipother2valuestr.equals(""))
            equipother2value.setText("$0.00");
        else
            equipother2value.setText("$" + equipother2valuestr);
        if (equipother2labelstr.equals(""))
            equipother2label.setText("Other");
        else
            equipother2label.setText(equipother2labelstr);
    }

    public void datasaveDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Save")
                .setMessage("Would you like to save these settings?")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

								/*
								 * compactorvalue,cuttingsawvalue,plantother1value
								 * ,toolsvalue,fuelvalue; EditText
								 * equipother1value
								 * ,equipother2value,plantother2value; EditText
								 * compactorlabel
								 * ,cuttingsawlabel,plantother1label
								 * ,plantother2label,toolslabel,fuellabel;
								 * EditText equipother1label,equipother2label;
								 */
                                dataSave(compactorvalue, "compactorvalue");
                                dataSave(cuttingsawvalue, "cuttingsawvalue");
                                dataSave(plantother1value, "plantother1value");
                                dataSave(plantother2value, "plantother2value");
                                dataSave(toolsvalue, "toolsvalue");
                                dataSave(fuelvalue, "fuelvalue");
                                dataSave(equipother1value, "equipother1value");
                                dataSave(equipother2value, "equipother2value");

                                dataSave(compactorlabel, "compactorlabel");
                                dataSave(cuttingsawlabel, "cuttingsawlabel");
                                dataSave(plantother1label, "plantother1label");
                                dataSave(plantother2label, "plantother2label");
                                dataSave(toolslabel, "toolslabel");

                                dataSave(fuellabel, "fuellabel");
                                dataSave(equipother1label, "equipother1label");
                                dataSave(equipother2label, "equipother2label");
                                Intent intent;
                                switch (flag) {
                                    case 1:
                                        intent = new Intent();
                                        intent.setClass(PlanEquipment.this,
                                                SettingsMain.class);
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        intent = new Intent();
                                        intent.setClass(PlanEquipment.this,
                                                HomeActivity.class);
                                        startActivity(intent);
                                        break;
                                    case 3:
                                        intent = new Intent();
                                        intent.setClass(PlanEquipment.this,
                                                JobMenuActivity.class);
                                        startActivity(intent);
                                        break;
                                }
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
                                intent = new Intent();
                                intent.setClass(PlanEquipment.this,
                                        SettingsMain.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent();
                                intent.setClass(PlanEquipment.this,
                                        HomeActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent();
                                intent.setClass(PlanEquipment.this,
                                        JobMenuActivity.class);
                                startActivity(intent);
                                break;
                        }

                    }
                }).show();
    }

    public boolean dataSave(EditText e, String key) {
        String s = e.getText().toString();
        s = e.getText().toString().replace('$', '#');
        s = s.replaceAll("#", "");
        edit.putString(key, s);
        edit.commit();
        return true;

    }
}
