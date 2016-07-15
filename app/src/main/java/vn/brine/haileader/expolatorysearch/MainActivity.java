package vn.brine.haileader.expolatorysearch;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import vn.brine.haileader.expolatorysearch.fragments.FavoriteFragment;
import vn.brine.haileader.expolatorysearch.fragments.HomeFragment;
import vn.brine.haileader.expolatorysearch.fragments.NewFragment;
import vn.brine.haileader.expolatorysearch.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private static final int HOME_FRAGMENT = 0;
    private static final int WHATS_HOT = 1;
    private static final int FAVORITE_FRAGMENT = 2;
    private static final int SETTING_FRAGMENT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        initNavigationDrawer();

        displayView(HOME_FRAGMENT);
    }

    public void initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                    case R.id.home:
                        displayView(HOME_FRAGMENT);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.whatshot:
                        displayView(WHATS_HOT);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.favorites:
                        displayView(FAVORITE_FRAGMENT);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        displayView(SETTING_FRAGMENT);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        finish();

                }
                return true;
            }
        });
//        View header = navigationView.getHeaderView(0);
//        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }
            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void displayView(int position){
        Fragment fragment = null;
        Class fragmentClass = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case HOME_FRAGMENT:
                fragmentClass = HomeFragment.class;
                title = getString(R.string.title_home);
                break;
            case WHATS_HOT:
                fragmentClass = NewFragment.class;
                title = getString(R.string.whats_hot);
                break;
            case FAVORITE_FRAGMENT:
                fragmentClass = FavoriteFragment.class;
                title = getString(R.string.title_favorites);
                break;
            case SETTING_FRAGMENT:
                fragmentClass = SettingFragment.class;
                title = getString(R.string.title_settings);
                break;
            default:
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }
    }

}
