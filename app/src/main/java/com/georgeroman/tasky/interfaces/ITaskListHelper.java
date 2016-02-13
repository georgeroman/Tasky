package com.georgeroman.tasky.interfaces;

public interface ITaskListHelper {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
