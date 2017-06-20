package com.hengshao.app.college_journal.View;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hengshao.app.college_journal.HTTP.Info;
import com.hengshao.app.college_journal.HTTP.Praise;
import com.hengshao.app.college_journal.HTTP.URL;
import com.hengshao.app.college_journal.Model.Data;
import com.hengshao.app.college_journal.Model.Postpraise;
import com.hengshao.app.college_journal.Model.ReturnGetInfo;
import com.hengshao.app.college_journal.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Heng on 17/5/27.
 */

public class Obtain_Fragment extends Fragment {
    private View view;
    private RecyclerView obtain_recycler;
    private BaseAdapter_ mAdapter;
    private List<Map> data;
    private SwipeRefreshLayout swip;
    private ExecutorService fix;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    swip.setRefreshing(false);
                    mAdapter.setData(data);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"数据更新！",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    swip.setRefreshing(false);
                    Toast.makeText(getContext(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getContext(),"服务器错误，请稍后再试！",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.obtain_viewpager, container, false);
        obtain_recycler = (RecyclerView) view.findViewById(R.id.obtain_recycler);
        swip = (SwipeRefreshLayout) view.findViewById(R.id.obtain_swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initdata();
            }
        });
        fix = Executors.newFixedThreadPool(1);
        swip.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimaryDark);
        data = new ArrayList<Map>();
        initdata();
        mAdapter = new BaseAdapter_(getContext(), data);
        obtain_recycler.setAdapter(mAdapter);
        RecyclerView.LayoutManager linearlayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        obtain_recycler.setLayoutManager(linearlayout);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fix.shutdown();
    }

    private void initdata() {
        fix.execute(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Info service = retrofit.create(Info.class);
                retrofit2.Call<ReturnGetInfo> call = service.Getinfo();
                try {
                    Response<ReturnGetInfo> response = call.execute();
                    Message msg = new Message();
                    if (response.code() == 200){
                        msg.what = 1;
                        data = new ArrayList<Map>();
                        for (int i = 0; i < response.body().getCount(); i++) {
                            Data[] d = response.body().getData();
                            Map map = new HashMap();
                            map.put("id",d[i].getId());
                            map.put("title",d[i].getTitle());
                            map.put("school",d[i].getSchool());
                            map.put("createtime",d[i].getCreatetime());
                            map.put("userid",d[i].getUserid());
                            map.put("usernickname",d[i].getUsernickname());
                            map.put("usertelephone",d[i].getUsertelephone());
                            map.put("content",d[i].getContent());
                            map.put("praise",d[i].getPraise());
                            data.add(map);
                        }
                    }else  if (response.code() == 403){
                        msg.what = 2;
                        msg.obj = response.errorBody().string();
                    }else {
                        msg.what = 3;
                    }
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public class BaseAdapter_ extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        List<Map> data;
        LayoutInflater minflater;


        public BaseAdapter_(Context context, List<Map> data) {
            this.context = context;
            this.data = data;
            this.minflater = LayoutInflater.from(context);
        }

        public void setData(List<Map> data) {
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = minflater.inflate(R.layout.obtain_item, parent, false);
            RecyclerView.ViewHolder viewHolder = new Holder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((Holder) holder).title.setText((String) data.get(position).get("title"));
            ((Holder) holder).school.setText((String) data.get(position).get("school"));
            ((Holder) holder).nickname.setText((String) data.get(position).get("usernickname"));
            ((Holder) holder).content.setText((String) data.get(position).get("content"));
            ((Holder) holder).praise_count.setText((String) data.get(position).get("praise"));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView school;
        TextView nickname;
        TextView content;
        TextView praise_count;
        ImageView praise_img;
        Postpraise postpraise;
        int i = 1;
        int b = 1;

        public Holder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            school = (TextView) view.findViewById(R.id.school);
            nickname = (TextView) view.findViewById(R.id.nickname);
            content = (TextView) view.findViewById(R.id.content);
            praise_count = (TextView) view.findViewById(R.id.praise_count);
            praise_img = (ImageView) view.findViewById(R.id.praise_img);

            praise_img.setOnClickListener(this);
            title.setOnClickListener(this);
            content.setOnClickListener(this);
            postpraise = new Postpraise();
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.praise_img:
                    int count = Integer.parseInt(praise_count.getText().toString());
                    if (i == 1) {
                        i++;
                        count++;
                        Sendnetword(count);
                    } else {
                        i--;
                        count--;
                        Sendnetword(count);
                        praise_img.setImageDrawable(getResources().getDrawable(R.drawable.zan));
                        praise_count.setText(String.valueOf(count));
                    }
                    break;
                case R.id.content:
                    if (b == 1) {
                        b++;
                        content.setMaxLines(500);
                    } else {
                        b--;
                        content.setMaxLines(5);
                    }
                    break;
                case R.id.title:
                    if (b == 1) {
                        b++;
                        content.setMaxLines(500);
                    } else {
                        b--;
                        content.setMaxLines(5);
                    }
                    break;
            }
        }

        private void Sendnetword(int count) {
            praise_img.setImageDrawable(getResources().getDrawable(R.drawable.zan_press));
            praise_count.setText(String.valueOf(count));
            final int finalCount = count;
            fix.execute(new Runnable() {
                @Override
                public void run() {
                    postpraise.setId((Integer) data.get(getAdapterPosition()).get("id"));
                    postpraise.setPraise(finalCount+"");
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL.url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Praise service = retrofit.create(Praise.class);
                    Call<JsonObject> call = service.Postpraise(postpraise,"information");
                    try {
                        Response<JsonObject> response = call.execute();
                        Message msg = new Message();
                        if (response.code() != 403){
                            msg.what = 3;
                            handler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
