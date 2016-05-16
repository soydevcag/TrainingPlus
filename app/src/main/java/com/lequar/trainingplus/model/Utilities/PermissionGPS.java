package com.lequar.trainingplus.model.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by lequar on 6/05/16.
 */
public class PermissionGPS extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_CODE_CAMERA = 100;
    private static final int PERMISSION_REQUEST_CODE_WRITE_STORAGE = 112;
    Context context;
    Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

        public void permissionGPS(){
            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getContext(),getActivity())) {
                fetchLocationData();
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSION_REQUEST_CODE_LOCATION,getContext(),getActivity());
            }
        }

        public void permissionCamera(){
            if (checkPermission(Manifest.permission.CAMERA, getContext(),getActivity())) {
                fetchLocationData();
            } else {
                requestPermission(Manifest.permission.CAMERA,PERMISSION_REQUEST_CODE_CAMERA,getContext(),getActivity());
            }
        }

        public void permissionWrite(){
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getContext(),getActivity())) {
                fetchLocationData();
            } else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,PERMISSION_REQUEST_CODE_WRITE_STORAGE,getContext(),getActivity());
            }
        }

    public void requestPermission(String strPermission, int perCode, Context _c, Activity _a){

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a,strPermission)){
            Toast.makeText(getContext(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(_a,new String[]{strPermission},perCode);
        }
    }

    public static boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fetchLocationData();

                } else {

                    Toast.makeText(getContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

                }
                break;

        }
    }
    private void fetchLocationData()
    {
        //code to use the granted permission (location)
    }
}
