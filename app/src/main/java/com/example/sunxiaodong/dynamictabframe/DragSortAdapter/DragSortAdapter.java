package com.example.sunxiaodong.dynamictabframe.DragSortAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sunxiaodong.dynamictabframe.R;
import com.example.sunxiaodong.dynamictabframe.TabBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sunxiaodong on 2016/3/28.
 */
public class DragSortAdapter extends RecyclerView.Adapter<DragSortAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private List<TabBean> mTabBeanList = new ArrayList<>();

    private Context mContext;
    private final OnStartDragListener mDragStartListener;

    public DragSortAdapter(Context context, OnStartDragListener dragStartListener) {
        mContext = context;
        mDragStartListener = dragStartListener;
    }

    public void update(List<TabBean> tabBeanList) {
        if (tabBeanList == null || tabBeanList.isEmpty()) {
            return;
        }
        mTabBeanList.clear();
        mTabBeanList.addAll(tabBeanList);
        notifyDataSetChanged();
    }

    public void addAll(List<TabBean> tabBeanList) {
        if (tabBeanList == null || tabBeanList.isEmpty()) {
            return;
        }
        mTabBeanList.addAll(tabBeanList);
        notifyDataSetChanged();
    }

    public List<TabBean> getData() {
        return mTabBeanList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drag_sort_adapter_item_layout, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.textView.setText(mTabBeanList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mTabBeanList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mTabBeanList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mTabBeanList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 列表项ViewHolder
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

    }
}
