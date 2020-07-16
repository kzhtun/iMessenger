package com.info121.imessenger.api;

import com.info121.imessenger.models.ObjectRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    @GET("registeruser/{username},{mobile},{deviceId},{secretKey},{mobileKey}")
    Call<ObjectRes> RegisterUser(@Path("username") String username, @Path("mobile") String mobile, @Path("deviceId") String deviceId, @Path("secretKey") String secretKey, @Path("mobileKey") String mobileKey);

    @GET("unregisterdevice/{deviceId},{secretKey},{mobileKey}")
    Call<ObjectRes> UnRegisterDevice(@Path("deviceId") String deviceId, @Path("secretKey") String secretKey, @Path("mobileKey") String mobileKey);

    @GET("validateuser/{deviceId},{fcmtoken},{secretKey},{mobileKey}")
    Call<ObjectRes> ValidateUser(@Path("deviceId") String deviceId,@Path("fcmtoken") String fcmtoken, @Path("secretKey") String secretKey, @Path("mobileKey") String mobileKey);

    @GET("getUserHP/{deviceId},{secretKey},{mobileKey}")
    Call<ObjectRes> GetUserHP(@Path("deviceId") String deviceId, @Path("secretKey") String secretKey, @Path("mobileKey") String mobileKey);

    @GET("getUserProfile/{userhp},{secretkey},{mobileKey}")
    Call<ObjectRes> GetUserProfile(@Path("userhp") String userhp, @Path("secretkey") String secretkey, @Path("mobileKey") String mobileKey);

    @GET("getUserMessage/{userhp},{secretkey},{mobileKey}")
    Call<ObjectRes> GetUserMessages(@Path("userhp") String username, @Path("secretkey") String secretkey, @Path("mobileKey") String mobileKey);

    @GET("updateMessageStatusRead/{messageId},{secretkey},{mobileKey}")
    Call<ObjectRes> UpdateMessageStatus(@Path("messageId") String messageId, @Path("secretkey") String secretkey, @Path("mobileKey") String mobileKey);




}
