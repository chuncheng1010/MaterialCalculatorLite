package com.project.material;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.material.helper.DatabaseHelper;
import com.project.material.util.CalculateVolumeUtils;
import com.project.material.util.CommonUtils;

public class JobsListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    public ArrayList<HashMap<String, String>> data;
    // public ImageLoader imageLoader;
    HashMap<String, String> arraydata;
    View view_sel;
    String value;
    String method;
    String state;
    String shape;
    String titleid;
    String option;
    int position1;
    private Activity activity;

    public JobsListAdapter(Activity a, ArrayList<HashMap<String, String>> d, String state) {
        activity = a;
        data = d;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.state = state;
        // imageLoader=new ImageLoader(activity.getApplicationContext());
        // isCheckedConfrim = new boolean[data.size()];
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public void removeItem(int position) {
        data.remove(position);
    }

    public void setSelectedIndex(int ind) {
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public void setData(ArrayList<HashMap<String, String>> d) {
        data = d;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.job_list_item, parent,false);

            AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenDensity = metrics.densityDpi;
            WindowManager wm = (WindowManager) activity.getBaseContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics1 = new DisplayMetrics();
            display.getMetrics(metrics1);
            int width = metrics1.widthPixels;
            int height = metrics1.heightPixels;
            if (width > 700) {
                params.height = (int) (100 * CommonUtils.mScaleYFactor);
                vi.setLayoutParams(params);

                holder = new ViewHolder();
                holder.title = (TextView) vi.findViewById(R.id.texttitle);
                holder.title.setTextSize(40 * CommonUtils.mFontScaleFactor);
            }
            else
            {
                params.height = (int) (35 * CommonUtils.mScaleYFactor);
                vi.setLayoutParams(params);

                holder = new ViewHolder();
                holder.title = (TextView) vi.findViewById(R.id.texttitle);
                holder.title.setTextSize(20 * CommonUtils.mFontScaleFactor);
            }
            holder.deletetitle = (ImageView) vi.findViewById(R.id.deletetitle);
            holder.viewtitle = (ImageView) vi.findViewById(R.id.viewselect);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        arraydata = new HashMap<String, String>();
        arraydata = data.get(position);
        // checkstate.setChecked(isCheckedConfrim[position]);
        // Setting all values in listview
        holder.title.setText(arraydata.get(JobsActivity.KEY_TITLE));
        holder.viewtitle.setImageResource(R.drawable.right);
        holder.deletetitle.setImageResource(R.drawable.delete);
        holder.viewtitle.setTag(position);
        holder.deletetitle.setTag(position);
        holder.viewtitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                position1 = Integer.parseInt(v.getTag().toString());
                HashMap<String, String> arraydata1 = new HashMap<String, String>();
                arraydata1 = data.get(position1);
                value = arraydata1.get(JobsActivity.KEY_VALUE);
                method = arraydata1.get(JobsActivity.KEY_METHOD);
                option = arraydata1.get(JobsActivity.KEY_OPTION);
                shape = arraydata1.get(JobsActivity.KEY_SHAPE);
                if (TextUtils.isEmpty(shape))
                    shape = String.valueOf(CalculateVolumeUtils.CUBE);
                Intent intent;
                if (state.equals("personal")) {

                    intent = new Intent(v.getContext(),
                            PersonalCalculator.class);
                    intent.putExtra("method", method);
                    intent.putExtra("spinnerMaterial", value);
                    intent.putExtra("option", option);
                    intent.putExtra("value", arraydata1.get(JobsActivity.KEY_ID));
                    intent.putExtra("shape", shape);
                    activity.startActivity(intent);
                    activity.finish();
                    activity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                } else {
                    /*
                    intent = new Intent(v.getContext(),
                            BusinessCalculationsActivity.class);
                    intent.putExtra("value", arraydata1.get(JobsActivity.KEY_ID));
                    intent.putExtra("method", method);
                    intent.putExtra("shape", shape);
                    intent.putExtra("spinnerMaterial", value);
                    activity.startActivity(intent);
                    activity.finish();
                    activity.overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
                    */
                }
            }

        });
        holder.deletetitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                position1 = Integer.parseInt(v.getTag().toString());
                HashMap<String, String> arraydata1 = new HashMap<String, String>();
                arraydata1 = data.get(position1);
                titleid = arraydata1.get(JobsActivity.KEY_ID);
                deleteTitle();
            }
        });

        return vi;
    }

    public void deleteTitle() {
        new AlertDialog.Builder(activity)
                .setTitle("")
                .setMessage("Would you like to delete this job?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                SQLiteDatabase localSQLiteDatabase = null;
                                try {
                                    localSQLiteDatabase = new DatabaseHelper(
                                            activity, activity.getString(
                                            R.string.app_name), null, 1
                                    )
                                            .getWritableDatabase();

                                    localSQLiteDatabase.delete("jobstable",
                                            "id=?", new String[]{titleid});

                                } catch (Exception e) {
                                    Toast.makeText(activity,
                                            "Occurred error while saving data",
                                            Toast.LENGTH_SHORT).show();
                                } finally {
                                    localSQLiteDatabase.close();
                                }
                                notifyDataSetChanged();
                                data.remove(position1);
                            }
                        }
                )
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.

                    }
                }).show();
    }

    class ViewHolder {
        public TextView title;
        public ImageView viewtitle;
        public ImageView deletetitle;

    }
}