package com.app.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.weather.Mapp;
import com.app.weather.R;
import com.app.weather.adapter.CityRecyclerAdapter;
import com.app.weather.bean.City;
import com.app.weather.data.CityDataSource;
import com.app.weather.utils.SizeUtils;

import java.util.List;

public class CitiesActivity extends BaseActivity {

    private Toolbar toolbar;//标题栏控件
    private RecyclerView recyclerView;//列表控件

    private CityRecyclerAdapter recyclerAdapter;//城市适配器

    private CityDataSource cityDataSource;//城市数据源

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        initData();
        initView();
        setView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cities, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint("搜索城市...");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keywords) {
                if (cityDataSource != null && recyclerAdapter != null) {
                    List<City> cities = cityDataSource.selectList(keywords);
                    recyclerAdapter.setNewData(cities);
                }
                return true;
            }

        });
        return super.onCreateOptionsMenu(menu);
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
        //设置列表的分割线
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            private Paint dividerPaint;
            private int dividerSize = SizeUtils.dp2px(2);

            @Override
            public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDrawOver(canvas, parent, state);
                LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
                int firstPosition = layoutManager.findFirstVisibleItemPosition();

                int start = parent.getPaddingStart();
                int end = parent.getWidth() - parent.getPaddingEnd();
                int count = parent.getChildCount();
                int totalCount = parent.getAdapter().getItemCount();

                for (int i = 0; i < count; i++) {
                    View child = parent.getChildAt(i);
                    int finalIndex = firstPosition + i;

                    if (finalIndex < totalCount - 1) {
                        canvas.save();
                        canvas.translate(0, child.getTop() + child.getHeight());
                        canvas.drawRect(start, 0, end, dividerSize, getDividerPaint());
                        canvas.restore();
                    }
                }
            }

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int count = parent.getAdapter().getItemCount();
                int index = parent.getChildAdapterPosition(view);
                if (index < count - 1) {
                    outRect.set(0, 0, 0, dividerSize);
                }
            }

            private Paint getDividerPaint() {
                if (dividerPaint == null) {
                    dividerPaint = new Paint();
                    dividerPaint.setColor(ContextCompat.getColor(CitiesActivity.this, R.color.cyan_200));
                    dividerPaint.setStyle(Paint.Style.FILL);
                }
                return dividerPaint;
            }

        });
        //设置列表的适配器
        recyclerAdapter = new CityRecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemSelectListener((city, position, selected) -> {
            if (selected) {//新增收藏
                cityDataSource.saveCollection(city.getLocationID());
                recyclerAdapter.selectItem(city);

            } else {//移除收藏
                cityDataSource.deleteCollection(city.getLocationID());
                recyclerAdapter.unselectItem(city);
            }
        });

        //设置城市列表数据
        List<City> cities = cityDataSource.selectList(null);
        List<String> locationIds = cityDataSource.selectCollections();
        recyclerAdapter.setNewData(cities, locationIds);
    }

    /**
     * 构建意图
     */
    public static Intent buildIntent(@NonNull Context context) {
        Intent intent = new Intent(context, CitiesActivity.class);
        return intent;
    }

}