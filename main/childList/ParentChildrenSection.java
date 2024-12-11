package com.creativeapps.schoolbusdriver.ui.activity.main.childList;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.creativeapps.schoolbusdriver.R;
import com.creativeapps.schoolbusdriver.data.network.models.Child;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;


final class ParentChildrenSection extends Section  {

    private final String title;
    public final List<Child> mChildrenList;
    private final ItemClickListener mItemClickListner;

    ParentChildrenSection(@NonNull final String title, @NonNull final List<Child> childrenList,
                          ItemClickListener itemClickListner) {

        super(SectionParameters.builder()
                .itemResourceId(R.layout.fragment_child_list_item)
                .headerResourceId(R.layout.fragment_child_list_header)
                .build());

        this.title = title;
        this.mItemClickListner = itemClickListner;
        this.mChildrenList = new ArrayList<>();
        this.mChildrenList.addAll(childrenList);
    }

    @Override
    public int getContentItemsTotal() {
        return mChildrenList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(final View view) {
        return new ChildItemViewHolder(view, mItemClickListner);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ChildItemViewHolder itemHolder = (ChildItemViewHolder) holder;

        final Child child = mChildrenList.get(position);

        itemHolder.mChildName.setText(child.getchildName());
        //itemHolder.imgItem.setImageResource(contact.profileImage);


        itemHolder.mAbsent = this.getStatus(child);

        if (itemHolder.mAbsent != null){
            if (itemHolder.mAbsent != 0) {
                itemHolder.mView.findViewById(R.id.check_in).setEnabled(false);
                itemHolder.mView.findViewById(R.id.check_out).setEnabled(false);
                itemHolder.mView.setBackgroundColor(Color.parseColor("#dddddd"));
                itemHolder.mChildAbsence.setText("Absent");
            }
        }

    }

    public Integer getStatus(Child child){
        if(child.getChild_AbsentTill()!=null) {
            try {
                String pattern = "yyyy-MM-dd HH:mm:ss";
                DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
                Date today = Calendar.getInstance().getTime();
                Date absent_till = df.parse(child.getChild_AbsentTill());
                if(!today.after(absent_till)){
                    return  1;
                }
            }
            catch (Exception e)
            {
                Log.d("getStatus", "getStatus: " + e.getMessage());
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(final View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.mParentNameTextView.setText(title);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, int check_in_out /*3: check_in, 4: check_out*/);
    }
}
