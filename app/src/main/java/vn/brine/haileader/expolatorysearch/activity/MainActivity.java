package vn.brine.haileader.expolatorysearch.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import vn.brine.haileader.expolatorysearch.R;
import vn.brine.haileader.expolatorysearch.fragments.FavoriteFragment;
import vn.brine.haileader.expolatorysearch.fragments.SearchFragment;
import vn.brine.haileader.expolatorysearch.fragments.SettingFragment;
import vn.brine.haileader.expolatorysearch.fragments.TestFragment;
import vn.brine.haileader.expolatorysearch.fragments.WhatsHotFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        actionBarDrawerToggle = setupDrawerToggle();

        setupDrawerContent(navigationView);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        if (getFragmentManager().findFragmentById(R.id.container_body) == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new SearchFragment()).commit();
        }

    }

    public void setupDrawerContent(NavigationView navigationView ) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem menuItem){
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()){
            case R.id.home:
                fragmentClass = SearchFragment.class;
                break;
            case R.id.whatshot:
                fragmentClass = WhatsHotFragment.class;
                break;
            case R.id.favorites:
                fragmentClass = FavoriteFragment.class;
                break;
            case R.id.settings:
                fragmentClass = SettingFragment.class;
                break;
            case R.id.logout:
                finish();
                break;
            default:
                fragmentClass = TestFragment.class;
        }
        try {
            assert fragmentClass != null;
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());

        drawerLayout.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }
            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

}
