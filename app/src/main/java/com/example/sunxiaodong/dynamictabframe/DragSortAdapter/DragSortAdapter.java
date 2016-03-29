package com.example.sunxiaodong.dynamictabframe.DragSortAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private OnStartDragListener mDragStartListener;

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
        TabBean tabBean = mTabBeanList.get(position);
        holder.textView.setText(tabBean.getName());
        holder.rootView.setOnClickListener(new OnItemClickListener(position, tabBean));
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

    public void addItemAtEnd(TabBean tabBean) {
        int position = mTabBeanList.size();
        mTabBeanList.add(tabBean);
        notifyItemInserted(position);
    }

    class OnItemClickListener implements View.OnClickListener {

        private int position;
        private TabBean tabBean;

        public OnItemClickListener(int position, TabBean tabBean) {
            this.position = position;
            this.tabBean = tabBean;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, position, tabBean);
            }
        }
    }

    /**
     * 列表项ViewHolder
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final View rootView;
        public final TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
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

    private OnRecyclerViewItemClickListener onItemClickListener;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position, TabBean data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
