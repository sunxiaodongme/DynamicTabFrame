package com.example.sunxiaodong.dynamictabframe.DragSortAdapter;

import android.content.Context;
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

    private static final String TAG = DragSortAdapter.class.getSimpleName();
    private static final String SXD = "sxd";

    private List<TabBean> mTabBeanList = new ArrayList<>();

    private Context mContext;
    private OnStartDragListener mDragStartListener;

    public DragSortAdapter(Context context) {
        mContext = context;
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
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        TabBean tabBean = mTabBeanList.get(position);
        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mDragStartListener != null) {
                    mDragStartListener.onStartDrag(holder);
                }
                return true;
            }
        });
        if (tabBean.isDefault()) {
            holder.textView.setEnabled(false);
        } else {

            holder.textView.setEnabled(true);
        }
        holder.textView.setText(tabBean.getName());
        holder.textView.setOnClickListener(new OnItemClickListener(position, tabBean));
    }

    @Override
    public int getItemCount() {
        return mTabBeanList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        TabBean tabBean = mTabBeanList.get(toPosition);
        if (tabBean.isDefault()) {
            return true;
        }
        if (toPosition > fromPosition) {
            //从前往后移动列表项
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mTabBeanList, i, i + 1);
            }
        } else {
            //从后往前移动列表项
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mTabBeanList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mTabBeanList.remove(position);
        notifyDataSetChanged();
    }

    public void addItemAtEnd(TabBean tabBean) {
        mTabBeanList.add(tabBean);
        notifyDataSetChanged();
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
//            itemView.setBackgroundColor(Color.LTGRAY);
            textView.setSelected(true);
        }

        @Override
        public void onItemClear() {
//            itemView.setBackgroundColor(0);
            textView.setSelected(false);
        }

    }

    public void setOnStartDragListener(OnStartDragListener onStartDragListener) {
        mDragStartListener = onStartDragListener;
    }

    //============================================设置列表项点击事件===============================start

    private OnRecyclerViewItemClickListener onItemClickListener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position, TabBean data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 每一项的点击事件
     */
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

    //============================================设置列表项点击事件===============================end

}
