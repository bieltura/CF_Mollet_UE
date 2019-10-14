package com.app.bieltv3.cfmollet;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HimneFragment extends Fragment {

    MediaPlayer himne;
    FloatingActionButton play;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_himne, container, false);

        play = (FloatingActionButton) view.findViewById(R.id.playHimne);

        if (himne != null) {
            himne.release();
        }

        himne = assignarhimne();
        himne.seekTo(0);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resume(himne.isPlaying());
            }
        });

        return view;
    }

    private MediaPlayer assignarhimne() {
        return MediaPlayer.create(getContext(), R.raw.himne);
    }

    @Override
    public void onPause() {
        if (himne.isPlaying())
            resume(himne.isPlaying());
        super.onPause();
    }

    public void resume(boolean isPlaying) {
        if (isPlaying) {
            himne.pause();
            play.setImageResource(android.R.drawable.ic_media_play);
        }
        else {
            himne.start();
            play.setImageResource(android.R.drawable.ic_media_pause);
        }
    }
}
