package com.georgeroman.tasky.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.georgeroman.tasky.R;
import com.georgeroman.tasky.adapters.TaskListAdapter;
import com.georgeroman.tasky.fragments.NewTaskDialog;
import com.georgeroman.tasky.utils.TaskListDbHelper;
import com.georgeroman.tasky.utils.TaskListHelperCallback;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    private static CoordinatorLayout sCoordinatorLayout;
    private static RecyclerView sRecyclerView;
    private static TaskListAdapter sAdapter;

    private TaskListDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_task_list);
        setSupportActionBar(toolbar);

        sCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);

        mDbHelper = new TaskListDbHelper(this);
        List<String> items = mDbHelper.getAllTasks();

        sRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sRecyclerView.setItemAnimator(new DefaultItemAnimator());

        sAdapter = new TaskListAdapter(items);
        sRecyclerView.setAdapter(sAdapter);

        TaskListHelperCallback callback = new TaskListHelperCallback(sAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(sRecyclerView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mDbHelper.deleteAllTasks();
        mDbHelper.insertAllTasks(sAdapter.getItems());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.menu_add) {
            NewTaskDialog newTaskDialog = new NewTaskDialog();
            newTaskDialog.show(getSupportFragmentManager(), null);
        }

        return super.onOptionsItemSelected(menuItem);
    }

    public static void addItem(int position, String item) {
        sAdapter.onItemAdd(position, item);
    }

    public static void showUndo(final int position, final String item) {
        Snackbar snackbar = Snackbar.make(sCoordinatorLayout, R.string.title_snackbar_undo, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.undo, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            sAdapter.onItemAdd(position, item);
                                        }
                                    });
        snackbar.show();
    }

    public static void scrollToPosition(int position) {
        sRecyclerView.smoothScrollToPosition(position);
    }
}
