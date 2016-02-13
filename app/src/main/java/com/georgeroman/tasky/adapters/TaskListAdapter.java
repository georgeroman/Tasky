package com.georgeroman.tasky.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.georgeroman.tasky.R;
import com.georgeroman.tasky.activities.TaskListActivity;
import com.georgeroman.tasky.interfaces.ITaskListHelper;

import java.util.Collections;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ItemViewHolder>
                             implements ITaskListHelper {
    private List<String> mItems;

    public TaskListAdapter(List<String> items) {
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.item_task, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
        String task = mItems.get(position);
        viewHolder.mTask.setText(task);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItems, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        TaskListActivity.showUndo(position, mItems.get(position));

        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void onItemAdd(int position, String item) {
        if (position == 0) {
            TaskListActivity.scrollToPosition(position);
        }

        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public List<String> getItems() {
        return mItems;
    }

    public static final class ItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTask;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mTask = (TextView)itemView.findViewById(R.id.text_task);
        }
    }
}
