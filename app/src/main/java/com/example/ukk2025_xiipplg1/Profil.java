package com.example.ukk2025_xiipplg1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Profil extends AppCompatActivity {

    private Button btnLogMain;

    private TextView name, email;
    private String userId;

    private ImageView log;

    private static final String API_URL = "http://172.16.0.106/ukk2025/profil.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        btnLogMain = findViewById(R.id.btnLogMain);
        name = findViewById(R.id.txNama);
        email = findViewById(R.id.txEmail);
        log=findViewById(R.id.logt);

        SharedPreferences loginPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        userId = loginPrefs.getString("idL", null);

        if (userId == null) {
            SharedPreferences regisPrefs = getSharedPreferences("RegisPrefs", MODE_PRIVATE);
            userId = regisPrefs.getString("idR", null);
        }

        if (userId != null) {
            fetchUserData(userId);

            log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

            btnLogMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Profil.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

        private void fetchUserData(String id) {
            String url = API_URL + "?user_id=" + id;

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String Name = response.getString("name");
                                String Email = response.getString("email");

                                name.setText(Name);
                                email.setText(Email);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(Profil.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(request);
        }

        private void logout() {
            SharedPreferences loginPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences regisPrefs = getSharedPreferences("RegisPrefs", MODE_PRIVATE);

            SharedPreferences.Editor loginEditor = loginPrefs.edit();
            SharedPreferences.Editor regisEditor = regisPrefs.edit();

            loginEditor.clear();
            regisEditor.clear();

            loginEditor.apply();
            regisEditor.apply();

            Intent intent = new Intent(Profil.this, Login.class);
            startActivity(intent);
            finish();
        }
}