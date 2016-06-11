package net.noratek.smartvoxxwear.rest.service;

import net.noratek.smartvoxx.common.model.ConferenceApiModel;

import java.util.List;

import retrofit.http.GET;
import retrofit2.Call;

public interface CfpApi {
    @GET("/cfpdevoxx/cfp.json")
    Call<List<ConferenceApiModel>> conferences();
}