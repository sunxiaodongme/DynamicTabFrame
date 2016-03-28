package com.example.sunxiaodong.dynamictabframe.DragSortAdapter;

import android.support.v7.widget.RecyclerView;

/**
 * 拖动回调接口
 */
public interface OnStartDragListener {

    /**
     * 开始拖动回调
     *
     * @param viewHolder
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);

}
