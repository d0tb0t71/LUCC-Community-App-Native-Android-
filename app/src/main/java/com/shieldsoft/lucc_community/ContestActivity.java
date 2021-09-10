package com.shieldsoft.lucc_community;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shieldsoft.lucc_community.adapters.CustomAdapter;
import com.shieldsoft.lucc_community.models.ContestModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ContestActivity extends AppCompatActivity {

    ArrayList<ContestModel> contestModels = new ArrayList<>();
    RecyclerView recyclerView;
    boolean isWithIn24Hours = false, isTomorrow = false;

    public static String convertUtc2Local(String utcTime) {
        String time = "";
        if (utcTime != null) {
            SimpleDateFormat utcFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date gpsUTCDate = null;//from  ww  w.j  a va 2 s  . c  o  m
            try {
                gpsUTCDate = utcFormatter.parse(utcTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat localFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            localFormatter.setTimeZone(TimeZone.getDefault());
            assert gpsUTCDate != null;
            time = localFormatter.format(gpsUTCDate.getTime());
        }
        return time;
    }

    String getDuration(long duration) {

        final long dy = TimeUnit.SECONDS.toDays(duration);
        final long hr = TimeUnit.SECONDS.toHours(duration)
                - TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays(duration));
        final long min = TimeUnit.SECONDS.toMinutes(duration)
                - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(duration));

        return String.format("%d Days %d Hours %d Minutes", dy, hr, min);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);
        Intent intent = getIntent();
        recyclerView = findViewById(R.id.ContestView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String siteName = intent.getStringExtra(ContestArena.NAME_KEY);


        String url;

        switch (siteName) {
            case "CodeForces":
                url = "https://www.kontests.net/api/v1/codeforces";
                break;
            case "CodeChef":
                url = "https://www.kontests.net/api/v1/code_chef";
                break;
            case "TopCoder":
                url = "https://www.kontests.net/api/v1/top_coder";
                break;
            case "AtCoder":
                url = "https://www.kontests.net/api/v1/at_coder";
                break;
            case "CSAcademy":
                url = "https://www.kontests.net/api/v1/cs_academy";
                break;
            case "HackerRank":
                url = "https://www.kontests.net/api/v1/hacker_rank";
                break;
            case "HackerEarth":
                url = "https://www.kontests.net/api/v1/hacker_earth";
                break;
            case "LeetCode":
                url = "https://www.kontests.net/api/v1/leet_code";
                break;
            case "AllContest":
                url = "https://www.kontests.net/api/v1/all";
                break;
            case "Contest Within 24 Hour":
                url = "https://www.kontests.net/api/v1/all";
                isWithIn24Hours = true;
                break;
            default:
                url = "https://www.kontests.net/api/v1/codeforces";
                break;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String startTime = convertUtc2Local(jsonObject.getString("start_time"));

                            if (isWithIn24Hours) {

                                Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
                                Date currentTime = Calendar.getInstance().getTime();
                                long difference_In_Time = date1.getTime() - currentTime.getTime();
                                long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60));
                                System.out.println(difference_In_Hours);
                                if (difference_In_Hours > 24 || difference_In_Hours < 0.0) {
                                    continue;
                                }

                            }


                            String name = jsonObject.getString("name");
                            String url = jsonObject.getString("url");
                            String duration = getDuration((long) Double.parseDouble(jsonObject.getString("duration")));
                            contestModels.add(new ContestModel(name, url, startTime, duration));


                        }

                    }
                    Context context =  getApplicationContext();
                    CustomAdapter customAdapter = new CustomAdapter(contestModels,context);
                    recyclerView.setAdapter(customAdapter);


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error);

            }
        }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}