package com.example.sunxiaodong.dynamictabframe.DragSortAdapter;

/**
 * 移动和删除数据项操作接口
 */
public interface ItemTouchHelperAdapter {

    /**
     * @param fromPosition 移动项的起始位置
     * @param toPosition   移动项的终止位置
     * @return 返回 true 表示移动成功
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * @param position 删除项的位置
     */
    void onItemDismiss(int position);
}
