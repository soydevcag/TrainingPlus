package com.lequar.trainingplus.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import java.util.Map;
import java.util.HashMap;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.lequar.trainingplus.ui.base.BaseActivity;
import com.lequar.trainingplus.model.Utilities.Urls;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.os.CountDownTimer;
import org.json.JSONException;
import org.json.JSONObject;
import com.lequar.trainingplus.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends BaseActivity {
    Urls urls = new Urls();
    Context context = this;
    String message = null;
    boolean valid = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        Button btnRegister = (Button) findViewById(R.id.registerBtn);
        //--- BOTON QUE REDIRECCIONA A OTRA ACTIVIDAD--//
        assert btnRegister != null;
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final RequestQueue queue = Volley.newRequestQueue(context);
                final EditText name = (EditText) findViewById(R.id.name);
                assert name != null;
                final EditText lastName = (EditText) findViewById(R.id.lastName);
                assert lastName != null;
                final EditText phone = (EditText) findViewById(R.id.phone);
                assert phone != null;
                final EditText email = (EditText) findViewById(R.id.email);
                assert email != null;
                final EditText pass = (EditText) findViewById(R.id.pass);
                assert pass != null;
                if (name.getText().toString().length() == 0) {
                    name.setError("Ingresa tu nombre");
                    valid = false;
                } else {
                    valid = true;
                }
                if (lastName.getText().toString().length() == 0) {
                    lastName.setError("Ingresa tu apellido");
                    valid = false;
                } else {
                    valid = true;
                }
                if (phone.getText().toString().length() == 0) {
                    phone.setError("Ingresa tu número");
                    valid = false;
                } else {
                    valid = true;
                }
                if (email.getText().toString().length() == 0) {
                    email.setError("Ingresa tu correo electrónico");
                    valid = false;
                } else {
                    valid = true;
                }
                if (pass.getText().toString().length() == 0) {
                    pass.setError("Ingresa un contraseña");
                    valid = false;
                } else {
                    valid = true;
                }

                if (valid) {
                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Registrando...");
                    progressDialog.show();
                    String url = urls.getRegisterUser();
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject objt = jsonObject.getJSONObject("content");
                                        message = objt.getString("status");
                                        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    new CountDownTimer(3000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            name.setText("");
                                            lastName.setText("");
                                            pass.setText("");
                                            email.setText("");
                                            phone.setText("");
                                        }

                                        @Override
                                        public void onFinish() {
                                            Intent actionRegister = new Intent(Register.this, Login.class);
                                            startActivity(actionRegister);
                                        }
                                    }.start();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    // error
                                    //Log.d("Error.Response", response);
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", name.getText().toString());
                            params.put("user", lastName.getText().toString());
                            params.put("password", pass.getText().toString());
                            params.put("city", email.getText().toString());
                            params.put("phone", phone.getText().toString());

                            return params;
                        }
                    };
                    queue.add(postRequest);
                }
            }
        });
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }
}