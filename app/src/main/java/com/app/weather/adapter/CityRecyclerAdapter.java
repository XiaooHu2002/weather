package com.app.weather.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.weather.Mapp;
import com.app.weather.R;
import com.app.weather.bean.City;

import java.util.ArrayList;
import java.util.List;

public class CityRecyclerAdapter extends RecyclerView.Adapter<CityRecyclerAdapter.CityViewHolder> {

    @NonNull
    private LayoutInflater inflater;

    @Nullable
    private List<City> list;
    @Nullable
    private List<String> locationIds;

    @Nullable
    private OnItemSelectListener onItemSelectListener;

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.item_recycler_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = list.get(position);

        //设置城市选择事件
        holder.cityLinearLayout.setOnClickListener(view -> {
            if (onItemSelectListener != null) {
                boolean selected = (locationIds != null && locationIds.contains(city.getLocationID()));
                onItemSelectListener.onItemSelect(city, position, !selected);
            }
        });

        //设置省市
        if (TextUtils.equals(city.getAdm1NameZH(), city.getAdm2NameZH())) {
            holder.admTextView.setText(city.getAdm1NameZH());
        } else {
            holder.admTextView.setText(city.getAdm1NameZH() + "   " + city.getAdm2NameZH());
        }

        //设置区
        holder.areaTextView.setText(city.getLocationNameZH());

        //设置选中状态
        if (locationIds != null && locationIds.contains(city.getLocationID())) {
            holder.cityLinearLayout.setBackgroundResource(R.color.cyan_200);
            holder.admTextView.setTextColor(Color.WHITE);
            holder.areaTextView.setTextColor(Color.WHITE);
            holder.selectImageView.setImageResource(R.drawable.ic_checked);
        } else {
            holder.cityLinearLayout.setBackgroundResource(R.color.white);
            holder.admTextView.setTextColor(ContextCompat.getColor(Mapp.getInstance(), R.color.cyan_200));
            holder.areaTextView.setTextColor(ContextCompat.getColor(Mapp.getInstance(), R.color.cyan_200));
            holder.selectImageView.setImageResource(R.drawable.ic_uncheck);
        }
    }

    public void setNewData(@Nullable List<City> list, @Nullable List<String> locationIds) {
        this.list = list;
        this.locationIds = locationIds;
        notifyDataSetChanged();
    }

    public void setNewData(@Nullable List<City> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * 选中
     */
    public void selectItem(@NonNull City city) {
        if (list == null) {
            return;
        }
        if (locationIds != null && locationIds.contains(city.getLocationID())) {
            return;
        }

        if (locationIds == null) {
            locationIds = new ArrayList<>();
        }
        locationIds.add(city.getLocationID());

        int index = list.indexOf(city);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    /**
     * 未选中
     */
    public void unselectItem(@NonNull City city) {
        if (list == null) {
            return;
        }
        if (locationIds == null || !locationIds.contains(city.getLocationID())) {
            return;
        }

        locationIds.remove(city.getLocationID());

        int index = list.indexOf(city);
        if (index >= 0) {
            notifyItemChanged(index);
        }
    }

    public void setOnItemSelectListener(@Nullable OnItemSelectListener listener) {
        onItemSelectListener = listener;
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cityLinearLayout;
        TextView admTextView;
        TextView areaTextView;
        ImageView selectImageView;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityLinearLayout = itemView.findViewById(R.id.city_linearLayout);
            admTextView = itemView.findViewById(R.id.adm_textView);
            areaTextView = itemView.findViewById(R.id.area_textView);
            selectImageView = itemView.findViewById(R.id.select_imageView);
        }

    }

    public interface OnItemSelectListener {

        void onItemSelect(@NonNull City city, int position, boolean selected);

    }

}