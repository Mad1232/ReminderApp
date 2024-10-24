package com.example.cydrop_frontend;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AdminInventoryFragment extends Fragment {
    private static final String URL_JSON_ARRAY = "http://coms-3090-038.class.las.iastate.edu:8080/inventory";

    TextView dataText;
    View regularView;
    View addInventoryView;

    EditText inventoryName;
    EditText inventoryQuantity;

    public AdminInventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_inventory, container, false);
        dataText = view.findViewById(R.id.data_text);

        regularView = view.findViewById(R.id.adminInventoryRegularLayout);
        addInventoryView = view.findViewById(R.id.adminInventoryAddLayout);

        inventoryName = view.findViewById(R.id.addInventoryItemName);
        inventoryQuantity = view.findViewById(R.id.addInventoryItemQuantity);

        Button submit = view.findViewById(R.id.adminInventorySubmitButton);
        submit.setOnClickListener(view1 -> {
            PostNewInventory();
        });

        Button logout = view.findViewById(R.id.adminLogoutButton);
        logout.setOnClickListener(view1 -> {
            Intent intent = new Intent(MainActivity.class.toString());
            startActivity(intent);
        });

        Button addInventoryButton = view.findViewById(R.id.deletePetButton);
        addInventoryButton.setOnClickListener(view2 -> {
            ToggleAddPetOverlay(true);
        });

        Button closeOverlay = view.findViewById(R.id.adminCloseOverlayButton);
        closeOverlay.setOnClickListener(view3 -> {
            ToggleAddPetOverlay(false);
        });

        ToggleAddPetOverlay(false);

        GetJSONData();
        return view;
    }

    private void ToggleAddPetOverlay(boolean addOverlay){
        if (addOverlay){
            regularView.setVisibility(View.INVISIBLE);
            addInventoryView.setVisibility(View.VISIBLE);
        } else {
            regularView.setVisibility(View.VISIBLE);
            addInventoryView.setVisibility(View.INVISIBLE);
        }
    }

    private void GetJSONData() {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON_ARRAY,
                null, // Pass null as the request body since it's a GET request
                response -> {
                    dataText.setText(""); // reset text field
                    try {
                        for (int i = 0; i < response.length(); i++){
                            JSONObject obj = response.getJSONObject(i);

                            String newLine = "Medication name: " + obj.getString("name") + "\n"
                                    + "Medication quantity: " + obj.getString("stock") + "\n";


                            dataText.setText(dataText.getText() + newLine + "\n");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> dataText.setText("Volley error: " + error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(getContext().getApplicationContext()).addToRequestQueue(jsonArrReq);
    }


    void PostNewInventory(){

        JSONObject inventoryObj = new JSONObject();

        try{
            inventoryObj.put("name", inventoryName.getText().toString());
            inventoryObj.put("stock", Integer.parseInt(inventoryQuantity.getText().toString()));
        } catch (JSONException e){
            // Unable to create json object
            return;
        }

        JsonObjectRequest postReq = new JsonObjectRequest(
                Request.Method.POST,
                "http://coms-3090-038.class.las.iastate.edu:8080/inventory",
                inventoryObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Victory! i think
                        GetJSONData();
                        ToggleAddPetOverlay(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Oopsie
                    }
                }
        );

        VolleySingleton.getInstance(getContext().getApplicationContext()).addToRequestQueue(postReq);
    }
}