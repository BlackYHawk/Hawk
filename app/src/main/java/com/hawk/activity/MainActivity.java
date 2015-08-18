package com.hawk.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.hawk.activity.twiter.TwiterAddActivity;
import com.hawk.adapter.TwiterAdapter;
import com.hawk.application.AppContext;
import com.hawk.data.manager.TwiterDBManager;
import com.hawk.data.model.Twiter;
import com.hawk.fragment.DrawerMenu;
import com.hawk.itemanimator.CustomItemAnimator;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private FrameLayout drawerContainer;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
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
        setContentView(R.layout.activity_main);

        init();
        initView();
    }

    private void init() {

        drawerMenu = new DrawerMenu();
        drawView = findViewById(R.id.drawer_menu);
        getSupportFragmentManager().beginTransaction().add(R.id.drawer_menu, drawerMenu, DrawerFlag).commit();

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
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        recyclerView = (RecyclerView)findViewById(R.id.recycleView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new CustomItemAnimator());

        twiterAdapter = new TwiterAdapter(this, R.layout.item_twiter_list, twiters);
        recyclerView.setAdapter(twiterAdapter);

        swipeRefreshLayout.setColorSchemeColors(R.color.theme_accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new InitalDataTask().execute();
            }
        });

        new InitalDataTask().execute();

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        action_addTwiter = (FloatingActionButton) findViewById(R.id.action_add_twiter);
        action_addTwiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, TwiterAddActivity.class));

            }
        });

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
            twiterAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<Twiter> loadTwiters = loadTwitersData(0);

            if(loadTwiters != null && loadTwiters.size() > 0)
            {
                twiters.addAll(loadTwiters);
            }

            Twiter twiter = new Twiter("First", "I am Hawk!", "2014");
            twiters.add(twiter);

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
    protected void onDestroy() {
        super.onDestroy();


        AppContext.getRefWatcher(this).watch(this);
    }
}
