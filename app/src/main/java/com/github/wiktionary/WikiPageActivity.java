package com.github.wiktionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WikiPageActivity extends AppCompatActivity {

    String json;
    WebView pageContent = findViewById(R.id.page_content);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_page);

        Toolbar toolbar = findViewById(R.id.toolbar_page);
        setSupportActionBar(toolbar);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://en.wiktionary.org/w/api.php?action=parse&mobileformat=true";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                json = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                json = null;
            }
        });

        queue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String content;

        JsonParser parser = new JsonParser();
        JsonObject result;

        if (json != null) {
            result = parser.parse(json).getAsJsonObject();
            content = result.get("text").getAsString();
            String encodedContent = Base64.encodeToString(content.getBytes(), Base64.NO_PADDING);
            pageContent.loadData(encodedContent, "text/html", "base64");
        }
    }
}
