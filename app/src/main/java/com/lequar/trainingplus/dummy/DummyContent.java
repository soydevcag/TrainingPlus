package com.lequar.trainingplus.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lequar.trainingplus.R;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;
import android.util.Log;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.VolleyError;
import com.lequar.trainingplus.model.Utilities.Urls;
import android.content.Context;
import android.widget.ImageView;

import org.json.JSONException;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.lequar.trainingplus.model.Utilities.CustomVolleyRequestImg;

/**
 * Just dummy content. Nothing special.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class DummyContent {

    Urls urls = new Urls();
    /**
     * An array of sample items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample items. Key: sample ID; Value: Item.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<>(5);


    public void loadData(Context context){

        //Datos Usuarios JSON
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = urls.getUsers();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                try {
                    Log.d("JsonArray",response.toString());
                    JSONObject jresponse = response.getJSONObject("content");
                    JSONArray resultado = jresponse.getJSONArray("result");
                    for(int i=0;i<resultado.length();i++){
                        String name = resultado.getJSONObject(i).getString("name");
                        String user = resultado.getJSONObject(i).getString("user");
                        String city = resultado.getJSONObject(i).getString("city");
                        String phone = resultado.getJSONObject(i).getString("phone");
                        addItem(new DummyItem(String.valueOf(i), R.drawable.p4, "Entrenador "+i, name, user+" - "+city+" - "+phone));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });

        queue.add(jsObjRequest);
    }
    static {

        //addItem(new DummyItem("11", R.drawable.p5, "Entrenador #11", "Steve Jobs","Deciding what not do do is as important as deciding what to do."));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class DummyItem {
        public final String id;
        public final int photoId;
        public final String title;
        public final String author;
        public final String content;

        public DummyItem(String id, int photoId, String title, String author, String content) {
            this.id = id;
            this.photoId = photoId;
            this.title = title;
            this.author = author;
            this.content = content;
        }
    }
}
