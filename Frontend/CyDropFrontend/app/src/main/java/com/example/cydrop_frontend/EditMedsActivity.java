package com.example.cydrop_frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditMedsActivity extends AppCompatActivity {
    private EditText msgResponse1;
    private EditText msgResponse2;
    private EditText msgResponse3;
    private static final String URL = "http://coms-3090-038.class.las.iastate.edu:8080/inventory";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meds);

        findViewById(R.id.btn_save_changes).setOnClickListener(view1 -> {
            msgResponse1 = findViewById(R.id.et_medication_name_prev);
            msgResponse2 = findViewById(R.id.et_medication_name_new);
            msgResponse3 = findViewById(R.id.et_medication_stock);
            String old_name = msgResponse1.getText().toString();
            String new_name = msgResponse2.getText().toString();
            String new_stock = msgResponse3.getText().toString();
            putRequest(URL, old_name, new_name, new_stock);
        });

        findViewById(R.id.btn_cancel).setOnClickListener(view1 -> {
            Intent intent = new Intent(EditMedsActivity.this, MedicationActivity.class);
            startActivity(intent);
        });

    }

    private void putRequest(String url, String old, String name, String stock) {
        String new_url = url + "/" + old;
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("stock", stock);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, new_url, json, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Medication Updated", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error);
            }
        }
        ){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                //                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //                params.put("param1", "value1");
                //                params.put("param2", "value2");
                return params;
            }
        };


        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }



}
