package in.gitsquare.demo;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.gitsquare.demo.Adapter.UserAdapter;
import in.gitsquare.demo.Model.User;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    RecyclerView rec_list;
    Button btn_filter;
    UserAdapter userAdapter;
    ArrayList<User> arr_user;
    User user;
    public FormBody.Builder formBody;
    ProgressBar progressBar;
    SwipyRefreshLayout mSwipyRefreshLayout;

    public void init() {
        rec_list = (RecyclerView) findViewById(R.id.rec_list);
        btn_filter = (Button) findViewById(R.id.btn_filter);
        formBody = new FormBody.Builder();
        arr_user = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.mSwipyRefreshLayout);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


        if (isNetworkConnected()) {
            UserAsync userAsync = new UserAsync(formBody.build());
            userAsync.execute("https://api.github.com/repos/square/retrofit/contributors");
        }

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Collections.sort(arr_user, new Comparator<User>() {
                    @Override
                    public int compare(User p1, User p2) {
                        return Integer.parseInt(p1.getUserContribution()) - Integer.parseInt(p2.getUserContribution()); // Ascending
                    }
                });
                userAdapter.notifyDataSetChanged();
            }
        });

        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (isNetworkConnected()) {
                    UserAsync userAsync = new UserAsync(formBody.build());
                    userAsync.execute("https://api.github.com/repos/square/retrofit/contributors");
                }

            }
        });

    /*     final UserModel  viewModel = ViewModelProviders.of(MainActivity.this).get(UserModel.class);
        viewModel.getUserList();
        viewModel.getUserList().observe(MainActivity.this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> userList) {



            }
        });*/
    }


    public class UserAsync extends AsyncTask<String, Integer, String> {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();

        RequestBody requestBody;

        public UserAsync(RequestBody requestBody) {
            this.requestBody = requestBody;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            Request.Builder builder = new Request.Builder();
            builder.get();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.e("Response", "" + response);
            arr_user.clear();
            progressBar.setVisibility(View.GONE);

            if(mSwipyRefreshLayout.isRefreshing()){
                mSwipyRefreshLayout.setRefreshing(false);
            }

            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; jsonArray.length() > 0; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String userName = jsonObject.getString("login");
                        String userImage = jsonObject.getString("avatar_url");
                        String userRepo = jsonObject.getString("repos_url");
                        String userContri = jsonObject.getString("contributions");

                        user = new User(userImage, userName, userRepo, userContri);
                        arr_user.add(user);
                        userAdapter = new UserAdapter(arr_user, MainActivity.this);
                        userAdapter.notifyDataSetChanged();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        rec_list.setLayoutManager(linearLayoutManager);
                        rec_list.setAdapter(userAdapter);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this, "Response is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

   /* public class UserModel extends ViewModel {
        public MutableLiveData<List<User>> userList;


        public UserModel() {
            userList = new MutableLiveData<>();
            // trigger user load.
        }


        LiveData<List<User>> getUserList() {
            if (userList == null) {

                userList.setValue(arr_user);
                Log.d("Rachuuuc", "" + arr_user.size());
            }
            return userList;
        }



        @Override
        protected void onCleared() {
            super.onCleared();
            Log.d(TAG, "on cleared called");
        }


    }
*/

}
