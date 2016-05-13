package com.lequar.trainingplus.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.lequar.trainingplus.MainActivity;
import com.lequar.trainingplus.R;
import com.lequar.trainingplus.dummy.DummyContent;
import com.lequar.trainingplus.ui.base.BaseActivity;
import com.lequar.trainingplus.ui.quote.ArticleDetailActivity;
import com.lequar.trainingplus.ui.quote.ArticleDetailFragment;
import com.lequar.trainingplus.ui.quote.ArticleListFragment;
import com.lequar.trainingplus.ui.quote.ListActivity;

import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;
import android.content.res.Resources;
import android.content.Intent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;
import com.lequar.trainingplus.model.Utilities.GPSTracker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.lequar.trainingplus.util.LogUtil;
import android.widget.ImageView;

/**
 * Created by lequar on 10/05/16.
 */
    public class Home extends BaseActivity implements OnMapReadyCallback,ArticleListFragment.Callback {

    GPSTracker gps;
    /**
     * The amount by which to scroll the camera. Note that this amount is in raw pixels, not dp
     * (density-independent pixels).
     */

    private GoogleMap mMap;

    private boolean twoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);

        Resources res = getResources();

        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("Buscar");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Buscar",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("Entrenadores");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Entrenadores",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);



        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DummyContent loadData = new DummyContent();
        loadData.loadData(this);

        setupToolbar();

        if (isTwoPaneLayoutUsed()) {
            twoPaneMode = true;
            LogUtil.logD("TEST","TWO POANE TASDFES");
            enableActiveItemState();
        }

        if (savedInstanceState == null && twoPaneMode) {
            setupDetailFragment();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // We will provide our own zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Show Sydney
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.68, -74.06), 10));
    }

    /**
     * When the map is not ready the CameraUpdateFactory cannot be used. This should be called on
     * all entry points that call methods on the Google Maps API.
     */
    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, "no mapa", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Called when the Go To Bondi button is clicked.
     */
    public void onGoToBondi(View view) {
        if (!checkReady()) {
            return;
        }
        double latitude=0;
        double longitude=0;
        gps = new GPSTracker(Home.this);
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Mi localización - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        mMap.clear();
        CameraPosition BONDI2 =
                new CameraPosition.Builder().target(new LatLng(latitude, longitude))
                        .zoom(15.5f)
                        .bearing(300)
                        .tilt(50)
                        .build();
        changeCamera(CameraUpdateFactory.newCameraPosition(BONDI2));
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .draggable(true));

        LatLng latLng = marker.getPosition();
        double la = latLng.latitude+100;
        double lo = latLng.longitude;
        String a = String.valueOf(la);
        Toast.makeText(getBaseContext(), a, Toast.LENGTH_SHORT)
                .show();

    }


    private void changeCamera(CameraUpdate update) {
        changeCamera(update, null);
    }

    /**
     * Change the camera position by moving or animating the camera depending on the state of the
     * animate toggle button.
     */
    private void changeCamera(CameraUpdate update, CancelableCallback callback) {
                // The duration must be strictly positive so we make it at least 1.
                mMap.animateCamera(update, Math.max(1, 1), callback);
    }

    /**
     * Called when an item has been selected
     *
     * @param id the selected quote ID
     */
    @Override
    public void onItemSelected(String id) {
        if (twoPaneMode) {
            // Show the quote detail information by replacing the DetailFragment via transaction.
            ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(id);
            getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
        } else {
            // Start the detail activity in single pane mode.
            Intent detailIntent = new Intent(this, ArticleDetailActivity.class);
            detailIntent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupDetailFragment() {
        ArticleDetailFragment fragment =  ArticleDetailFragment.newInstance(DummyContent.ITEMS.get(0).id);
        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }

    /**
     * Enables the functionality that selected items are automatically highlighted.
     */
    private void enableActiveItemState() {
        ArticleListFragment fragmentById = (ArticleListFragment) getFragmentManager().findFragmentById(R.id.article_list);
        fragmentById.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /**
     * Is the container present? If so, we are using the two-pane layout.
     *
     * @return true if the two pane layout is used.
     */
    private boolean isTwoPaneLayoutUsed() {
        return findViewById(R.id.article_detail_container) != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_quotes;
    }


    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
}
