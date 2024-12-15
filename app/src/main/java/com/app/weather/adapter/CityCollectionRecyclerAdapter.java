package com.app.weather.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.weather.R;
import com.app.weather.bean.City;

import java.util.List;

public class CityCollectionRecyclerAdapter extends RecyclerView.Adapter<CityCollectionRecyclerAdapter.CityCollectionViewHolder> {

    private LayoutInflater inflater;

    private List<City> list;

    private OnItemClickListener onItemClickListener;

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @NonNull
    @Override
    public CityCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.item_recycler_city_collection, parent, false);
        return new CityCollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityCollectionViewHolder holder, int position) {
        City city = list.get(position);

        //设置城市点击事件
        holder.cityLinearLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(city, position);
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

        //设置删除点击事件
        holder.deleteImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemDeleted(city, position);
            }
        });
    }

    /**
     * 设置新数据
     */
    public void setNewData(List<City> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * 移除数据
     */
    public void removeItem(City city) {
        if (list != null && city != null) {
            int index = list.indexOf(city);
            if (index >= 0) {
                list.remove(city);
                notifyItemRemoved(index);
            }
        }
    }

    /**
     * 设置子项点击事件
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class CityCollectionViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cityLinearLayout;
        TextView admTextView;
        TextView areaTextView;
        ImageView deleteImageView;

        public CityCollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            cityLinearLayout = itemView.findViewById(R.id.city_linearLayout);
            admTextView = itemView.findViewById(R.id.adm_textView);
            areaTextView = itemView.findViewById(R.id.area_textView);
            deleteImageView = itemView.findViewById(R.id.delete_imageView);
        }

    }

    /**
     * 子项点击事件
     */
    public interface OnItemClickListener {

        /**
         * 点击子项
         */
        void onItemClick(@NonNull City city, int position);

        /**
         * 点击子项的删除图标
         */
        void onItemDeleted(@NonNull City city, int position);

    }

}