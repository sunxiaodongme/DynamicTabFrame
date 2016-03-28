package com.example.sunxiaodong.dynamictabframe.DragSortAdapter;

public interface ItemTouchHelperViewHolder {

    /**
     * 数据项被移动或删除时调用
     */
    void onItemSelected();


    /**
     * 完成移动或删除时调用
     */
    void onItemClear();
}
