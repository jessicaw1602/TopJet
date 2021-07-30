package com.example.topjet.Maps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.topjet.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

// Code Adapted from: https://www.youtube.com/watch?v=DhYofrJPzlI
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private View view;
    private Context context;

    TextView tvPlaceName, tvRatingAndAddr;

    // Constructors
    public CustomInfoWindowAdapter() {}

    public CustomInfoWindowAdapter(Context context) {
        this.view = LayoutInflater.from(context).inflate(R.layout.map_custom_marker, null);
        this.context = context;
    }

    // Now we want to set the text to the view
    private void renderWindowInfo(View view, Marker marker){
        tvPlaceName = view.findViewById(R.id.tvPlaceName);
        tvRatingAndAddr = view.findViewById(R.id.tvRatingAndAddr);

        String title = marker.getTitle();
        String snippet = marker.getSnippet();

        if (!title.equals("") && !snippet.equals("")){
            tvPlaceName.setText(marker.getTitle());
            tvRatingAndAddr.setText(marker.getSnippet());
        }
    }


    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowInfo(view, marker);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowInfo(view, marker);
        return view;
    }
}
