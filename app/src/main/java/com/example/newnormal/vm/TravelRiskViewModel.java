package com.example.newnormal.vm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newnormal.data.models.TravelAdvisory;
import com.example.newnormal.util.TravelAdvisoryApi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TravelRiskViewModel extends AndroidViewModel {
    private static final String TRAVEL_ADVISORY_URL = "https://www.travel-advisory.info";

    public TravelRiskViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Map<String, TravelAdvisory.CountryData.Advisory>> getTravelAdvisory() {
        final MutableLiveData<Map<String, TravelAdvisory.CountryData.Advisory>> travelAdvisoryMutableMap = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TRAVEL_ADVISORY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TravelAdvisoryApi travelAdvisoryApi = retrofit.create(TravelAdvisoryApi.class);
        Call<TravelAdvisory> call = travelAdvisoryApi.getTravelAdvisory();
        call.enqueue(new Callback<TravelAdvisory>() {
            @Override
            public void onResponse(@NonNull Call<TravelAdvisory> call, @NonNull Response<TravelAdvisory> response) {
                if (response.isSuccessful()) {
                    Log.d("Success", "Fetching data from Travel Advisory API successful!");

                    TravelAdvisory travelAdvisory = response.body();
                    if (travelAdvisory != null) {
                        Map<String, TravelAdvisory.CountryData.Advisory> travelAdvisoryMap = new HashMap<>();
                        Collection<TravelAdvisory.CountryData> countryDataCollection = travelAdvisory.getData().values();

                        for (TravelAdvisory.CountryData countryData : countryDataCollection) {
                            String countryName = countryData.getName();
                            TravelAdvisory.CountryData.Advisory advisory = countryData.getAdvisory();
                            travelAdvisoryMap.put(countryName, advisory);
                        }

                        travelAdvisoryMutableMap.setValue(travelAdvisoryMap);
                    }
                } else {
                    Log.d("Fail", "Fetching data from Travel Advisory API failed!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TravelAdvisory> call, @NonNull Throwable t) {
                Log.d("Error", "Error occurred during fetching data from Travel Advisory API!");
            }
        });

        return travelAdvisoryMutableMap;
    }
}
