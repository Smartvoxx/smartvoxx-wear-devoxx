package net.noratek.smartvoxxwear.connection;

import net.noratek.smartvoxx.common.model.ConferenceApiModel;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

public interface CfpApi {

    @GET("/cfpdevoxx/cfp.json")
    void getConferences(Callback<List<ConferenceApiModel>> callback);

}