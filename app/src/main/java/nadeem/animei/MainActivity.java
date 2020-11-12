package nadeem.animei;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import nadeem.animei.Library.Library.LibraryFragment;
import nadeem.animei.Library.Library.SavedFragment;
import nadeem.animei.Library.Search.SearchActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new LibraryFragment();
        Bundle args = new Bundle();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String startScreen = preferences.getString("start_screen", "-1");

        switch (startScreen) {
            case "-1":
                args.putString("link", Utilities.BASE + Utilities.POPULAR);
                args.putString("library", "Popular");
                break;
            case "0":
                fragmentManager.beginTransaction().replace(R.id.content_frame, new SavedFragment()).commit();
                return;
        }
        navigationView.getMenu().getItem(Integer.parseInt(startScreen) + 1).setChecked(true);
        fragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        Fragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        switch (id) {
            case R.id.nav_popular:
                args.putString("link", Utilities.BASE + Utilities.POPULAR);
                args.putString("library", "Popular");
                break;
            case R.id.nav_saved:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new SavedFragment()).commit();
                break;
            case R.id.nav_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;
        }
        fragment.setArguments(args);
        if (!args.isEmpty()) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        return true;
    }
}
