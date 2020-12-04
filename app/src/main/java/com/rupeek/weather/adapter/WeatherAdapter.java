package com.rupeek.weather.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.rupeek.weather.R;
import com.rupeek.weather.databinding.DataListItemBinding;
import com.rupeek.weather.model.WeatherModel;

import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherDataViewHolder> {
    private final ArrayList<WeatherModel> list;
    private final Context context;

    public WeatherAdapter(Context context, ArrayList<WeatherModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_list_item, parent, false);
        return new WeatherDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDataViewHolder holder, int position) {
        Log.i("adapter", "onBindViewHolder");

        holder.dataListItemBinding.tvDate.setText(dateAsRequired(list.get(position).getTime()));
        holder.dataListItemBinding.tvTemp.setText(list.get(position).getTemp() + "° C");
        holder.dataListItemBinding.tvRain.setText(list.get(position).getRain() + "%");
        holder.dataListItemBinding.tvWind.setText(list.get(position).getWind() + " km/h");
    }

    private String dateAsRequired(String t) {
        long timeStamp = Long.parseLong(t);
        Date time = new java.util.Date((long) timeStamp * 1000);
        String[] temp = String.valueOf(time).split(" ");
        String s = temp[1] + " " + temp[2] + " " + temp[5];
        return s;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class WeatherDataViewHolder extends RecyclerView.ViewHolder {
        private DataListItemBinding dataListItemBinding;

        public WeatherDataViewHolder(@NonNull View itemView) {
            super(itemView);
            dataListItemBinding = DataBindingUtil.bind(itemView);
        }
    }
}
