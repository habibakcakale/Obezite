package com.hbb.ege.obezite;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by habib on 27.09.2013.
 */
public class FoodItemViewer extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_fooditemviewer);

        //Get WebView to show content
        WebView foodWebView = (WebView) findViewById(R.id.foodWebView);
        //Support zoom and background for transparent
        foodWebView.getSettings().setSupportZoom(true);
        foodWebView.getSettings().setBuiltInZoomControls(true);
        foodWebView.setBackgroundColor(0);
        //Fix for blinking
        foodWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        foodWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        foodWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        int id = getIntent().getExtras().getInt("id");
        String content = "<html><head><title>Example</title>"
                + "<meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" />"
                + "<style type=\"text/css\" rel=\"stylesheet\">" + getString(R.string.style) + "</style>"
                + "</head><body>"
                + "<h1>" + Manager.getManager(this).getFoodList().get(id).getTitle() + "</h1>";
        content += Manager.getManager(this).getFoodList().get(id).getContent();
        content += "</body></html>";
        foodWebView.loadDataWithBaseURL("file:///android_asset/",content, "text/html","UTF-8", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}