package com.hbb.ege.obezite;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;

public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up splash screen.
        ImageButton imgBtnSplashScreen = (ImageButton) findViewById(R.id.imgButtonWelcomeMessage);
        imgBtnSplashScreen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ScrollView splashScreenLayout = (ScrollView) findViewById(R.id.splashScreenScroll);
                splashScreenLayout.removeAllViews();
            }
        });
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    /**
     * Index fragment representing a section index.
     */
    public static class IndexFragment extends Fragment {

        public IndexFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View indexView = inflater.inflate(R.layout.fragment_index,
                    container, false);
            ArrayAdapter<Entry> adapter = new ArrayAdapter<Entry>(
                    getActivity(), android.R.layout.simple_list_item_1, Manager
                    .getManager(getActivity()).getIndexList());

            ListView listView = (ListView) indexView
                    .findViewById(R.id.indexList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> view, View arg1,
                                        int arg2, long arg3) {

                    Intent intent = new Intent(getActivity(),
                            ItemViewerActivity.class);
                    intent.putExtra("id", arg2);
                    intent.putExtra("source", "IndexFragment");
                    startActivity(intent);
                }
            });
            return indexView;
        }
    }

    public static class SearchFragment extends Fragment {
        Button btn;

        public SearchFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View searchView = inflater.inflate(R.layout.fragment_search,
                    container, false);

            btn = (Button) searchView.findViewById(R.id.search_button);
            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    ListView listView = (ListView) searchView
                            .findViewById(R.id.searchList);
                    EditText edt_Search = (EditText) searchView
                            .findViewById(R.id.edt_search);
                    RadioGroup radioGroup = (RadioGroup) searchView
                            .findViewById(R.id.search_options);

                    String selection = " title LIKE ? ";
                    if (radioGroup.getCheckedRadioButtonId() == R.id.search_in_descriptions) {
                        selection = " content LIKE ? ";
                    }
                    String[] selectionArgs = {"%" + edt_Search.getText()
                            + "%"};
                    String[] columns = {"id", "title", "content"};
                    ArrayList<Entry> list = Manager.getManager(getActivity())
                            .search(selection, selectionArgs, columns);

                    ArrayAdapter<Entry> adapter = new ArrayAdapter<Entry>(
                            getActivity(), android.R.layout.simple_list_item_1,
                            list);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> view, View arg1,
                                                int arg2, long arg3) {

                            Intent intent = new Intent(getActivity(),
                                    ItemViewerActivity.class);
                            intent.putExtra("id", arg2);
                            intent.putExtra("source", "SourceFragment");
                            startActivity(intent);
                        }
                    });
                }
            });
            return searchView;

        }
    }

    public static class AboutFragment extends Fragment {
        public AboutFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View aboutView = inflater.inflate(R.layout.fragment_about,
                    container, false);
            ViewPager viewPager = (ViewPager)aboutView.findViewById(R.id.view_pager);
            ImagePagerAdapter adapter = new ImagePagerAdapter(getActivity());
            viewPager.setAdapter(adapter);

            return aboutView;
        }
    }

    public static class FoodFragment extends Fragment {
        public FoodFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View foodView = inflater.inflate(R.layout.fragment_foodlist, container, false);
            ArrayAdapter<Food> foodAdapter = new ArrayAdapter<Food>(
                    getActivity(), android.R.layout.simple_list_item_1,
                    Manager.getManager(getActivity()).getFoodList());
            ListView foodIndexList = (ListView) foodView.findViewById(R.id.lvFoodIndex);
            foodIndexList.setAdapter(foodAdapter);
            foodIndexList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), FoodItemViewer.class);
                    intent.putExtra("id", i);
                    startActivity(intent);
                }
            });
            return foodView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new IndexFragment();
                    break;
                case 1:
                    fragment = new SearchFragment();
                    break;
                case 2:
                    fragment = new FoodFragment();
                    break;
                case 3:
                    fragment = new AboutFragment();
                    break;
                default:
                    fragment = new IndexFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.index).toUpperCase(l);
                case 1:
                    return getString(R.string.search).toUpperCase(l);
                case 2:
                    return getString(R.string.food).toUpperCase(l);
                case 3:
                    return getString(R.string.about).toUpperCase(l);
            }
            return null;
        }
    }
}
