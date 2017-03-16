package org.truong.youtubedownloader.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.commit451.youtubeextractor.YouTubeExtractionResult;
import com.commit451.youtubeextractor.YouTubeExtractor;

import org.truong.youtubedownloader.MainActivity;
import org.truong.youtubedownloader.R;
import org.truong.youtubedownloader.models.Video;
import org.truong.youtubedownloader.tools.StaticTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetLinkActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSIONS_REQUEST_ID = 1;

    @BindView(R.id.edt_url)
    EditText edt_url;

    @BindView(R.id.img_youtube)
    ImageView img_youtube;

    @BindView(R.id.tv_download)
    TextView tv_download;

    @BindView(R.id.img_thumb)
    ImageView img_thumb;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.layout_result)
    View layout_result;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.tv_1080)
    TextView tv_1080;

    @BindView(R.id.tv_720)
    TextView tv_720;

    @BindView(R.id.tv_360)
    TextView tv_360;

    @BindView(R.id.tv_240)
    TextView tv_240;

    private YouTubeExtractor extractor;

    private Video video_1080;
    private Video video_720;
    private Video video_360;
    private Video video_240;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_link);
        ButterKnife.bind(this);

        extractor = YouTubeExtractor.create();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString(Intent.EXTRA_TEXT);
            edt_url.setText(url);
        }

        img_youtube.setOnClickListener(this);
        tv_download.setOnClickListener(this);

        tv_1080.setOnClickListener(this);
        tv_720.setOnClickListener(this);
        tv_360.setOnClickListener(this);
        tv_240.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_youtube:
                StaticTools.openYouTubeApp(this);
                break;
            case R.id.tv_download:
                checkPermission();
                break;
            case R.id.tv_1080:
                startDownload(video_1080);
                break;
            case R.id.tv_720:
                startDownload(video_720);
                break;
            case R.id.tv_360:
                startDownload(video_360);
                break;
            case R.id.tv_240:
                startDownload(video_240);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    StaticTools.createFolder();
                    beginDownload();
                } else {
                    // Do something
                }
                return;
            }
            // Other 'case'
        }
    }

    private void checkPermission() {
        if (StaticTools.verify(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_ID)) {
            StaticTools.createFolder();
            beginDownload();
        }
    }

    private void beginDownload() {
        final String url = edt_url.getText().toString();
        new AsyncTask<Void, String, Void>() {
            private String title;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                title = StaticTools.getTitleQuietly(url);
                publishProgress(title);
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                if (values[0] != null) {
                    tv_title.setText(values[0]);
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                extractor.extract(StaticTools.getIdFromUrl(edt_url.getText().toString())).enqueue(new Callback<YouTubeExtractionResult>() {
                    @Override
                    public void onResponse(Call<YouTubeExtractionResult> call, Response<YouTubeExtractionResult> response) {
                        layout_result.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        YouTubeExtractionResult result = response.body();
                        Uri uriThumb = result.getBestAvailableQualityThumbUri();
                        if (uriThumb != null) {
                            Glide.with(GetLinkActivity.this).load(uriThumb).override(500, 500).into(img_thumb);
                        }

                        if (result.getHd1080VideoUri() == null) {
                            tv_1080.setVisibility(View.GONE);
                        } else {
                            video_1080 = new Video(title, result.getHd1080VideoUri().toString());
                        }

                        if (result.getHd720VideoUri() == null) {
                            tv_720.setVisibility(View.GONE);
                        } else {
                            video_720 = new Video(title, result.getHd720VideoUri().toString());
                        }

                        if (result.getSd360VideoUri() == null) {
                            tv_360.setVisibility(View.GONE);
                        } else {
                            video_360 = new Video(title, result.getSd360VideoUri().toString());
                        }

                        if (result.getSd240VideoUri() == null) {
                            tv_240.setVisibility(View.GONE);
                        } else {
                            video_240 = new Video(title, result.getSd240VideoUri().toString());
                        }

                    }

                    @Override
                    public void onFailure(Call<YouTubeExtractionResult> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Snackbar snackbar = Snackbar
                                .make(findViewById(android.R.id.content), "Please check the url and try again!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        }.execute();
    }

    private void startDownload(Video video) {
        if (video != null) {
            download(video);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void download(Video video) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(video.getUrl()));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
        request.setDestinationInExternalPublicDir(StaticTools.HOME_PATH, video.getTitle() + ".mp4");
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE); //CATEGORY.OPENABLE
        Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                Toast.LENGTH_LONG).show();
    }
}
