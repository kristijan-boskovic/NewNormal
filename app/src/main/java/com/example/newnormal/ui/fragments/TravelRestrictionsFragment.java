package com.example.newnormal.ui.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.newnormal.R;
import com.example.newnormal.data.models.TravelAdvisory;
import com.example.newnormal.ui.activities.MainActivity;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;

import java.util.HashMap;
import java.util.Map;

public class TravelRestrictionsFragment extends Fragment {
    private static final String MAPBOX_STYLE_URI = "mapbox://styles/kb49394/ckoaaq2pj53td17nx976frxca";
    private MapView mapView;
    private Map<String, TravelAdvisory.CountryData.Advisory> travelAdvisoryMapbox = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.setTitle(R.string.travel_risk_map);

        Mapbox.getInstance(activity, getString(R.string.mapbox_access_token));

        View view = inflater.inflate(R.layout.fragment_travel_restrictions, container, false);

        LiveData<Map<String, TravelAdvisory.CountryData.Advisory>> travelAdvisoryMutableMap = activity.getTravelAdvisoryMutableMap();
        travelAdvisoryMutableMap.observe(getViewLifecycleOwner(), new Observer<Map<String, TravelAdvisory.CountryData.Advisory>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable Map<String, TravelAdvisory.CountryData.Advisory> travelAdvisoryMap) {
//                newsAdapter.setNews(newsList);
                travelAdvisoryMapbox = travelAdvisoryMap;
            }
        });

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.setStyle(new Style.Builder().fromUri(MAPBOX_STYLE_URI), style -> {
                // Custom map style has been loaded and map is now ready
                for (Map.Entry<String, TravelAdvisory.CountryData.Advisory> entry : travelAdvisoryMapbox.entrySet()) {
                    String countryName = entry.getKey();
                    TravelAdvisory.CountryData.Advisory advisory = entry.getValue();
                    double travelRiskScore = Double.parseDouble(advisory.getScore());

                    Layer layer = style.getLayer(countryName);
                    if (layer != null) {
                        if (travelRiskScore >= 0 && travelRiskScore <= 2.5) {
                            layer.setProperties(PropertyFactory.fillColor(Color.parseColor("#2bff00")), PropertyFactory.fillOpacity(0.5f)); // Low risk (green)
                        }
                        else if (travelRiskScore > 2.5 && travelRiskScore <= 3.5) {
                            layer.setProperties(PropertyFactory.fillColor(Color.parseColor("#fffb00")), PropertyFactory.fillOpacity(0.5f)); // Medium risk (yellow)
                        }
                        else if (travelRiskScore > 3.5 && travelRiskScore <= 4.5) {
                            layer.setProperties(PropertyFactory.fillColor(Color.parseColor("#ff7700")), PropertyFactory.fillOpacity(0.5f)); // High risk (orange)
                        }
                        else if (travelRiskScore > 4.5 && travelRiskScore <= 5) {
                            layer.setProperties(PropertyFactory.fillColor(Color.parseColor("#ff0000")), PropertyFactory.fillOpacity(0.5f)); // Extreme risk (red)
                        }
                        else {
                            layer.setProperties(PropertyFactory.fillColor(Color.parseColor("#ababab")), PropertyFactory.fillOpacity(0.5f)); // Unknown (grey)
                        }
                    }
                }
            });
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }
}