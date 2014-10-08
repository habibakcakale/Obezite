package com.hbb.ege.obezite;

import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class ItemViewerActivity extends FragmentActivity {

    ViewPager pager;
    ItemViewerPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemviewer);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("source").equals("SourceFragment")) {
            viewPagerAdapter = new ItemViewerPagerAdapter(
                    getSupportFragmentManager(), Manager.getManager(
                    getBaseContext()).getSearchResult());
        } else {
            viewPagerAdapter = new ItemViewerPagerAdapter(
                    getSupportFragmentManager(), Manager.getManager(
                    getBaseContext()).getIndexList());
        }

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(viewPagerAdapter);

        pager.setCurrentItem(bundle.getInt("id"));
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

    public static class ItemViewerPagerAdapter extends FragmentPagerAdapter {
        List<Entry> list;

        public ItemViewerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public ItemViewerPagerAdapter(FragmentManager fm, List<Entry> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment object = new ObjectFragment();
            Bundle bundle = new Bundle();
            bundle.putString("content", list.get(i).getContent());
            object.setArguments(bundle);
            return object;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getTitle();
        }
    }

    public static class ObjectFragment extends Fragment {
        public ObjectFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_itemviewer,
                    container, false);
            Bundle bundle = getArguments();
            //Create an HTML Document
            String content = "<html><head><title>Example</title>"
                    + "<meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" />"
                    + "<style type=\"text/css\" rel=\"stylesheet\">" + getString(R.string.style) + "</style>"
                    + "</head><body>";
            content += bundle.getString("content");
            content += "</body></html>";
            //Show Content
            WebView webView = (WebView) view.findViewById(R.id.webView1);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.setBackgroundColor(0);
            webView.loadDataWithBaseURL("file:///android_asset/",content, "text/html", "UTF-8", null);
            return view;
        }
    }
}
