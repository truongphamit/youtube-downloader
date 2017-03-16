package org.truong.youtubedownloader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import org.truong.youtubedownloader.activities.GetLinkActivity;
import org.truong.youtubedownloader.activities.IntroActivity;
import org.truong.youtubedownloader.adapters.VideosAdapter;
import org.truong.youtubedownloader.customviews.DividerItemDecoration;
import org.truong.youtubedownloader.tools.StaticTools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_ID = 1;

    @BindView(R.id.rvVideos)
    RecyclerView rvVideos;

    @BindView(R.id.emptyView)
    View emptyView;

    private VideosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(StaticTools.verify(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_ID)) {
            StaticTools.createFolder();
            init();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_get_link:
                startActivity(new Intent(this, GetLinkActivity.class));
                return true;
            case R.id.action_open_youtube:
                StaticTools.openYouTubeApp(this);
                return true;
            case R.id.action_help:
                startActivity(new Intent(this, IntroActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Do something
                    StaticTools.createFolder();
                    init();
                } else {
                    // Do something
                }
                return;
            }
            // Other 'case'
        }
    }

    private void init() {
        rvVideos.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvVideos.addItemDecoration(itemDecoration);
        rvVideos.setHasFixedSize(true);

        adapter = new VideosAdapter(this);
        rvVideos.setAdapter(adapter);

        checkRecyclerViewIsEmpty();
    }

    private void checkRecyclerViewIsEmpty() {
        if (adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            rvVideos.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            rvVideos.setVisibility(View.VISIBLE);
        }
    }
}
