package com.app.weather.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.app.weather.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherFutureRecyclerAdapter extends RecyclerView.Adapter<WeatherFutureRecyclerAdapter.WeatherViewHolder> {

    private LayoutInflater inflater;

    private List<WeatherDailyBean.DailyBean> list;

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.item_recycler_weather_future, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherDailyBean.DailyBean dailyBean = list.get(position);

        //设置日期
        holder.dateTextView.setText(dailyBean.getFxDate());

        //设置周
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dailyBean.getFxDate());
            String week = new SimpleDateFormat("EEEE").format(date);
            holder.weekTextView.setText(week);
        } catch (ParseException e) {
            holder.weekTextView.setText("");
        }

        //设置天气名称
        if (dailyBean.getTextDay().equals(dailyBean.getTextNight())) {
            holder.weatherNameTextView.setText(dailyBean.getTextDay());
        } else {
            holder.weatherNameTextView.setText(dailyBean.getTextDay() + "转" + dailyBean.getTextNight());
        }

        //设置最低温度
        holder.tempMinTextView.setText(dailyBean.getTempMin() + "℃");

        //设置最高温度
        holder.tempMaxTextView.setText(dailyBean.getTempMax() + "℃");
    }

    /**
     * 设置新数据
     */
    public void setNewData(List<WeatherDailyBean.DailyBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        TextView weekTextView;
        TextView weatherNameTextView;
        TextView tempMinTextView;
        TextView tempMaxTextView;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_textView);
            weekTextView = itemView.findViewById(R.id.week_textView);
            weatherNameTextView = itemView.findViewById(R.id.weatherName_textView);
            tempMinTextView = itemView.findViewById(R.id.tempMin_textView);
            tempMaxTextView = itemView.findViewById(R.id.tempMax_textView);
        }

    }

}