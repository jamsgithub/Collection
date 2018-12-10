package com.myself.collection.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.myself.collection.R;
import com.myself.collection.dao.IpModel;
import com.myself.collection.joggle.IpService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author Jams
 * @date 2018-10-16 11:17
 * @des
 *
 * @UpAuthor
 * @UpDate
 * @UpDes
 */
public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("encrypt");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                try {
                    URL url = new URL("http://www.jianshu.com/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("charset" , "utf-8");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode < 200 || responseCode > 300) {
                        return;
                    }
                    InputStream is = connection.getInputStream();
                    InputStreamReader ir = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(ir);
                    char[] bytes = new char[2048];
                    int length = 0;
                    while ((length = reader.read(bytes)) != -1) {
                        String string = new String(bytes);
                        result = result + string;
                    }

                    String s = result;
                    Log.i("TAG" , s);
                    is.close();
                    ir.close();
                    reader.close();
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getData(View view) {
        String url = "http://ip.taobao.com/service/";
        //Retrofit2底层基于okHttp实现的
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpService ipService = retrofit.create(IpService.class);
        Call<IpModel> call = ipService.getIpMsg("59.108.54.37");
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String rs = response.body().getData().toString();
                Log.i("onResponse", rs);

            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });

    }
    private native boolean isEquals(String str);
}
