package nadeem.animei.Anime;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashSet;
import java.util.Set;

import nadeem.animei.R;

public class AnimeActivity extends AppCompatActivity {

    private String link;
    private String title;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_anime);

        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        title = intent.getStringExtra("title");
        setTitle(title);

        Toolbar toolbar = findViewById(R.id.anime_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            SharedPreferences preferences = getSharedPreferences("saved", Context.MODE_PRIVATE);
            Set<String> set = preferences.getStringSet("saved", new HashSet<String>());
            if (set.contains(link)) {
                set.remove(link);
                menu.getItem(0).setChecked(false);
            } else {
                set.add(link);
                menu.getItem(0).setChecked(true);
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet("saved", set).apply();
            editor.commit();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.anime_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_save);
        SharedPreferences preferences = getSharedPreferences("saved", Context.MODE_PRIVATE);
        Set<String> set = preferences.getStringSet("saved", new HashSet<String>());
        if (set.contains(link))
            item.setChecked(true);
        else
            item.setChecked(false);
        return super.onPrepareOptionsMenu(menu);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new InformationFragment();
                case 1:
                    return new EpisodesFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Information";
                case 1:
                    return "Episodes";
            }
            return null;
        }
    }
}
