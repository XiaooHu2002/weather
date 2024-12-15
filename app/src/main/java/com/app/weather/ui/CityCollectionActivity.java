package com.app.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.weather.Mapp;
import com.app.weather.R;
import com.app.weather.adapter.CityCollectionRecyclerAdapter;
import com.app.weather.bean.City;
import com.app.weather.data.CityDataSource;
import com.app.weather.utils.SizeUtils;

import java.util.List;

public class CityCollectionActivity extends BaseActivity {

    private static final int REQUEST_CITY_MORE = 1;

    private Toolbar toolbar;//标题栏控件
    private RecyclerView recyclerView;//列表控件
    private TextView moreTextView;//更多城市文本控件

    private CityCollectionRecyclerAdapter recyclerAdapter;//城市收藏适配器

    private CityDataSource cityDataSource;//城市数据源

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_collection);
        initData();
        initView();
        setView();
    }

    /**
     * 后一个页面回传的处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_CITY_MORE: {
                //刷新数据
                List<City> list = getCollections();
                recyclerAdapter.setNewData(list);
            }
            break;

        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        cityDataSource = Mapp.getInstance().getCityDataSource();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        moreTextView = findViewById(R.id.more_textView);
    }

    /**
     * 设置控件
     */
    private void setView() {
        //将标题栏关联到页面
        setSupportActionBar(toolbar);
        //显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置列表的布局样式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //设置列表的间隔距离
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int count = parent.getAdapter().getItemCount();
                int index = parent.getChildAdapterPosition(view);
                if (index < count - 1) {
                    outRect.set(0, 0, 0, SizeUtils.dp2px(20));
                }
            }

        });
        //设置列表的适配器
        recyclerAdapter = new CityCollectionRecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new CityCollectionRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(City city, int position) {
                //选中收藏并返回
                Intent intent = new Intent();
                intent.putExtra("city", city.getLocationID());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onItemDeleted(City city, int position) {
                //弹窗提示是否删除收藏
                new AlertDialog.Builder(CityCollectionActivity.this).setTitle("提示").setMessage("确定要删除" + city.getLocationNameZH() + "吗?").setNegativeButton("取消", null).setPositiveButton("删除", (dialogInterface, i) -> {
                    cityDataSource.deleteCollection(city.getLocationID());
                    recyclerAdapter.removeItem(city);
                }).create().show();
            }

        });

        //将数据加入适配器
        List<City> list = getCollections();
        recyclerAdapter.setNewData(list);

        //设置查看更多城市的按钮点击事件
        moreTextView.setOnClickListener(view -> {
            Intent intent = CitiesActivity.buildIntent(CityCollectionActivity.this);
            startActivityForResult(intent, REQUEST_CITY_MORE);
        });
    }

    /**
     * 获取收藏的城市列表
     */
    private List<City> getCollections() {
        List<String> locationIds = cityDataSource.selectCollections();
        List<City> cities = cityDataSource.locationIds2Cities(locationIds);
        return cities;
    }

    /**
     * 构建意图
     */
    public static Intent buildIntent(@NonNull Context context) {
        Intent intent = new Intent(context, CityCollectionActivity.class);
        return intent;
    }

}