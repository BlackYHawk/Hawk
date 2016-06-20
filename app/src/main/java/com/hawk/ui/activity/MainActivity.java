package com.hawk.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hawk.adapter.twiter.TwiterAdapter;
import com.hawk.ui.activity.album.AlbumActivity;
import com.hawk.ui.activity.map.BaiduMapActivity;
import com.hawk.ui.activity.robot.RobotActivity;
import com.hawk.ui.activity.twiter.TwiterAddActivity;
import com.hawk.util.IntentUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private final Handler mDrawerActionHandler = new Handler();
    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private int mNavItemId;

    private FrameLayout drawerContainer;
    private NavigationView navigationView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView recyclerView;
    private TwiterAdapter twiterAdapter;
    private TwiterDBManager twiterDBManager;
    private List<Twiter> twiters;

    private FloatingActionButton action_addTwiter;

    public static final String DrawerFlag = "DrawerFlag";
    private DrawerMenu drawerMenu;
    private View drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_main);

        init();
        initView();
    }

    private void init() {

//        drawerMenu = new DrawerMenu();
//        drawView = findViewById(R.id.drawer_menu);
//        getSupportFragmentManager().beginTransaction().add(R.id.drawer_menu, drawerMenu, DrawerFlag).commit();

        twiterDBManager = TwiterDBManager.getInstance(this);
        twiters = new ArrayList<Twiter>();
    }

    private void initView() {

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        this.setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        drawerContainer = (FrameLayout) findViewById(R.id.drawer_container);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        recyclerView = (ListView)findViewById(R.id.recycleView);

        twiterAdapter = new TwiterAdapter(this, twiters);
        recyclerView.setAdapter(twiterAdapter);


        swipeRefreshLayout.setColorSchemeColors(R.color.theme_accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new InitalDataTask().execute();
            }
        });

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "hh", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, R.string.menu_delete);
            }
        });


        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        action_addTwiter = (FloatingActionButton) findViewById(R.id.action_add_twiter);
        action_addTwiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, TwiterAddActivity.class));

            }
        });

        new InitalDataTask().execute();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0: {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                deleteTwiter(adapterContextMenuInfo.position);
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void navigate(final int itemId) {
        // perform the actual navigation logic, updating the main content fragment etc
        switch (itemId) {
            case R.id.nav_menu_weibo :

                break;
            case R.id.nav_menu_photo :
                startActivity(new Intent(this, AlbumActivity.class));
                break;
            case R.id.nav_menu_robot :
                startActivity(new Intent(this, RobotActivity.class));
                break;
            case R.id.nav_menu_map :
                startActivity(new Intent(this, BaiduMapActivity.class));
                break;
            case R.id.nav_menu_contact :
                IntentUtil.doStartApplicationWithPackageName(this, "com.hawk.contact");
                break;
            case R.id.nav_menu_socket :

                break;
            case R.id.nav_menu_setting :

                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();

        // allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening
        drawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(mNavItemId);
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private class InitalDataTask extends AsyncTask<Void, Void, Void> {

        public InitalDataTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            twiters.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<Twiter> loadTwiters = loadTwitersData(0);

            if(loadTwiters != null && loadTwiters.size() > 0)
            {
                twiters.addAll(loadTwiters);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            twiterAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private List<Twiter> loadTwitersData(final int offset)
    {

        List<Twiter> twiters = twiterDBManager.getTwiters();

        return twiters;
    }

    private void deleteTwiter(final int position) {

        Twiter twiter = twiters.get(position);

        twiterDBManager.deleteTwiter(twiter);
        twiters.remove(position);
        twiterAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
