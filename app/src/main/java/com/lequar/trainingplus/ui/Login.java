package com.lequar.trainingplus.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import com.lequar.trainingplus.R;
import com.lequar.trainingplus.ui.base.BaseActivity;
import com.lequar.trainingplus.ui.quote.ListActivity;

import android.support.v7.app.ActionBar;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toolbar;

public class Login extends BaseActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando...");

        Button btnRegister = (Button)findViewById(R.id.inBtn);
        //--- BOTON QUE REDIRECCIONA A OTRA ACTIVIDAD--//
        assert btnRegister != null;
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Intent actionRegister = new Intent(Login.this, Home.class);
                actionRegister.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(actionRegister);
                finish();
            }
        });

    }

    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
}
