package com.abvrtridentroom.roomconfiguration.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abvrtridentroom.roomconfiguration.R;

public class SlidePageFragment extends Fragment {

    private int imageResource;

    public static SlidePageFragment getInstance(int resourceID) {
        SlidePageFragment f = new SlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("image_source", resourceID);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageResource = getArguments().getInt("image_source");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FlickableImageView imageView = (FlickableImageView) view.findViewById(R.id.image);
        imageView.setImageResource(imageResource);

    }
}
