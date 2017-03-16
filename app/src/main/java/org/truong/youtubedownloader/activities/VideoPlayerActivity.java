package org.truong.youtubedownloader.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.commit451.youtubeextractor.YouTubeExtractionResult;
import com.commit451.youtubeextractor.YouTubeExtractor;

import org.truong.youtubedownloader.R;
import org.truong.youtubedownloader.services.FloatWindowService;
import org.truong.youtubedownloader.tools.StaticTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.videoview)
    VideoView videoView;

    @BindView(R.id.btn_floating)
    ImageView btn_floating;

    private YouTubeExtractor extractor;
    private YouTubeExtractionResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);

        btn_floating.setOnClickListener(this);

        extractor = YouTubeExtractor.create();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString(Intent.EXTRA_TEXT);
            extractor.extract(StaticTools.getIdFromUrl(url)).enqueue(new Callback<YouTubeExtractionResult>() {
                @Override
                public void onResponse(Call<YouTubeExtractionResult> call, Response<YouTubeExtractionResult> response) {
                    result = response.body();

                    if (result.getHd1080VideoUri() != null) {
                        videoView.setVideoURI(result.getHd1080VideoUri());
                    } else if (result.getHd720VideoUri() != null) {
                        videoView.setVideoURI(result.getHd720VideoUri());
                    } else if (result.getSd360VideoUri() != null) {
                        videoView.setVideoURI(result.getSd360VideoUri());
                    } else if (result.getSd240VideoUri() != null) {
                        videoView.setVideoURI(result.getSd240VideoUri());
                    }
                    videoView.setMediaController(new MediaController(VideoPlayerActivity.this));
                    videoView.start();
                }

                @Override
                public void onFailure(Call<YouTubeExtractionResult> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_floating:
                Intent intent = new Intent(this, FloatWindowService.class);
                if (result != null) {
                    if (result.getHd1080VideoUri() != null) {
                        intent.putExtra(Intent.EXTRA_TEXT, result.getHd1080VideoUri().toString());
                    } else if (result.getHd720VideoUri() != null) {
                        intent.putExtra(Intent.EXTRA_TEXT, result.getHd720VideoUri().toString());
                    } else if (result.getSd360VideoUri() != null) {
                        intent.putExtra(Intent.EXTRA_TEXT, result.getSd360VideoUri().toString());
                    } else if (result.getSd240VideoUri() != null) {
                        intent.putExtra(Intent.EXTRA_TEXT, result.getSd240VideoUri().toString());
                    }

                    startService(intent);
                    finish();
                }
        }
    }
}
