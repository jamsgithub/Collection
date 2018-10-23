package com.myself.collection.joggle;

import com.myself.collection.dao.IpModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ligang
 * Created 2017-8-4 11:20
 *
 * @dese todo
 * <p>
 * UpUser by Administrator
 * UpDate 2017-8-4 11:20 @dese todo
 */

public interface IpService {
    @GET("getIpInfo.php")
    Call<IpModel> getIpMsg(@Query("ip") String ip);
}
