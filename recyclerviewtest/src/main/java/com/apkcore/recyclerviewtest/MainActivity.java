package com.apkcore.recyclerviewtest;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RvAdapter.OnItemClickListener {
    private RecyclerView rv;
    private List<Integer> datas;
    private RecyclerView.ItemDecoration itemDecoration;
    private RvAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tb_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        rv = (RecyclerView) findViewById(R.id.rvToDoList);
        initData();
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            Resources res = getResources();
            datas.add(res.getIdentifier("ic_launcher", "mipmap", getPackageName()));
        }
        /**
         *用来确定每一个item如何进行排列摆放
         * LinearLayoutManager 相当于ListView的效果
         GridLayoutManager相当于GridView的效果
         StaggeredGridLayoutManager 瀑布流
         */
        /**第一步：设置布局管理器**/
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        /**第二步：添加分割线**/
        itemDecoration = new DividerItemDecorationCommon(this, DividerItemDecoration.VERTICAL_LIST, ContextCompat.getColor(this, R.color.colorAccent), 1);
        rv.addItemDecoration(itemDecoration);
        /**第三步：设置适配器**/
        rvAdapter = new RvAdapter(this, datas);
        rv.setAdapter(rvAdapter);
        rvAdapter.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /**竖向的ListView**/
            case R.id.id_action_listview:
                rv.setBackgroundColor(Color.TRANSPARENT);
                rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                rvAdapter.setType(0);
                rv.removeItemDecoration(itemDecoration);
                rv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                itemDecoration = new DividerItemDecorationCommon(this, DividerItemDecoration.VERTICAL_LIST, ContextCompat.getColor(this, R.color.colorAccent), 1);
                rv.addItemDecoration(itemDecoration);
                break;
            /**横向的ListView**/
            case R.id.id_action_horizontalListView:
                rvAdapter.setType(1);
                rv.removeItemDecoration(itemDecoration);
                rv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                itemDecoration = new DividerItemDecorationCommon(this, DividerItemDecoration.HORIZONTAL_LIST, ContextCompat.getColor(this, R.color.colorAccent), 1);
                rv.addItemDecoration(itemDecoration);
                break;

            /**竖向的GridView**/
            case R.id.id_action_gridview:
                rvAdapter.setType(1);
                rv.setBackgroundColor(Color.TRANSPARENT);
                rv.removeItemDecoration(itemDecoration);
                rv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                rv.setLayoutManager(new GridLayoutManager(this, 5));
                itemDecoration =new DividerGridItemDecoration(this);
                rv.addItemDecoration(itemDecoration);
                break;
            /**横向的GridView**/
            case R.id.id_action_horizontalGridView:
                rvAdapter.setType(1);
                rv.setBackgroundColor(Color.TRANSPARENT);
                rv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                rv.removeItemDecoration(itemDecoration);
                rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
                itemDecoration =new DividerGridItemDecoration(this);
                rv.addItemDecoration(itemDecoration);
                break;
//            /**竖向的瀑布流**/
//            case R.id.id_action_staggeredgridview:
//                rvAdapter.setType(3);
//                rv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//                rv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//                rv.removeItemDecoration(itemDecoration);
//                rv.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
//                break;
//            /**添加一个数据**/
//            case R.id.id_action_add:
//                rvAdapter.notifyItemInserted(1);
//                break;
//            /**删除一个数据**/
//            case R.id.id_action_delete:
//                rvAdapter.notifyItemRemoved(1);
//                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onItemClickListener(int position, Integer data) {
        Toast.makeText(MainActivity.this, "点击了" + (position + 1), Toast.LENGTH_SHORT).show();
    }

    //dp转px
    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //px转dp
    private int px2dp(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
