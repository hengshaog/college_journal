package com.hengshao.app.college_journal.View;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.hengshao.app.college_journal.HTTP.Praise;
import com.hengshao.app.college_journal.HTTP.URL;
import com.hengshao.app.college_journal.HTTP.User;
import com.hengshao.app.college_journal.Model.Datauser;
import com.hengshao.app.college_journal.Model.Postpraise;
import com.hengshao.app.college_journal.Model.ReturnGetUser;
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

public class Nearby_Fragment extends Fragment {
    private View view;
    private SwipeRefreshLayout swip;
    private RecyclerView recyvler;
    private BaseAdapter__ mAdapter;
    private List<Map> data;
    private ExecutorService fix;
    private String authority = "nothing";
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
                    swip.setRefreshing(false);
                    Toast.makeText(getContext(),"服务器错误，请稍后再试！",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.nearby_viewpager, container, false);
        swip = (SwipeRefreshLayout) view.findViewById(R.id.nearby_swip);
        recyvler = (RecyclerView) view.findViewById(R.id.nearby_recycler);
        fix = Executors.newFixedThreadPool(1);
        data = new ArrayList<Map>();
        Guge();
        mAdapter = new BaseAdapter__(getContext(),data);
        recyvler.setAdapter(mAdapter);
        RecyclerView.LayoutManager linearlayout = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyvler.setLayoutManager(linearlayout);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Guge();
                initdate();
            }
        });
        swip.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimaryDark);
        return view;
    }

    private void Guge() {
        SharedPreferences db = getContext().getSharedPreferences("LOGIN",getContext().MODE_APPEND);
        authority = db.getString("telephone","nothing");
        if (authority.equals("nothing")){

        }else {
            authority = "normal";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fix.shutdown();
    }

    public void initdate() {
        fix.execute(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                User service = retrofit.create(User.class);
                retrofit2.Call<ReturnGetUser> call = service.GetUser(authority);
                try {
                    Response<ReturnGetUser> response = call.execute();
                    Message msg = new Message();
                    if (response.code() == 200){
                        data = new ArrayList<Map>();
                        int count ;
                        if (response.body().getData() == null){
                            count = 0;
                        }else {
                            count = response.body().getData().length;
                        }
                        for (int i = 0; i < count; i++) {
                            Datauser[] d = response.body().getData();
                            Map map = new HashMap();
                            map.put("id",d[i].getId());
                            map.put("school", d[i].getSchool());
                            map.put("nickname", d[i].getNickname());
                            map.put("signature", d[i].getSignature());
                            map.put("praise", d[i].getPraise());
                            map.put("telephone", d[i].getTelephone());
                            data.add(map);
                            msg.what = 1;
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

//    public  void initdate(){
//        data = new ArrayList<Map>();
//        for (int i = 0; i < 20; i++) {
//            Map map = new HashMap();
//            map.put("school","大托普");
//            map.put("usernickname","恒少");
//            map.put("signature","签名狠毒o");
//            map.put("praisem","100");
//            map.put("usertelephone","18208142446");
//            data.add(map);
//        }
//    }

    public class BaseAdapter__ extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        Context context;
        List<Map> data;
        LayoutInflater minflater;

        public BaseAdapter__(Context context, List<Map> data) {
            this.context = context;
            this.data = data;
            this.minflater = LayoutInflater.from(context);
        }

        public void setData(List<Map> data){
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = minflater.inflate(R.layout.nearby_item, parent, false);
            RecyclerView.ViewHolder viewHolder = new Holder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            ((Holder) holder).school.setText((String) data.get(position).get("school"));
            ((Holder) holder).nickname.setText((String) data.get(position).get("nickname"));
            ((Holder) holder).signature.setText((String) data.get(position).get("signature"));
            ((Holder) holder).praise_count.setText((String) data.get(position).get("praise"));
            ((Holder) holder).telephone.setText((String) data.get(position).get("telephone"));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView school;
        TextView nickname;
        TextView signature;
        TextView praise_count;
        ImageView praise_img;
        TextView telephone;
        Postpraise postpraise;
        int i = 1;

        public Holder(View view) {
            super(view);
            school = (TextView) view.findViewById(R.id.school);
            nickname = (TextView) view.findViewById(R.id.nickname);
            signature = (TextView) view.findViewById(R.id.signature);
            praise_count = (TextView) view.findViewById(R.id.praise_count);
            praise_img = (ImageView) view.findViewById(R.id.praise_img);
            telephone = (TextView) view.findViewById(R.id.telephone);

            praise_img.setOnClickListener(this);
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
                        Sendnetwork(count);
                        praise_img.setImageDrawable(getResources().getDrawable(R.drawable.zan_press));
                        praise_count.setText(String.valueOf(count));
                    } else {
                        i--;
                        count--;
                        Sendnetwork(count);
                        praise_img.setImageDrawable(getResources().getDrawable(R.drawable.zan));
                        praise_count.setText(String.valueOf(count));
                    }
                    break;
            }
        }

        private void Sendnetwork(int count) {
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
                    Call<JsonObject> call = service.Postpraise(postpraise,"user");
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
