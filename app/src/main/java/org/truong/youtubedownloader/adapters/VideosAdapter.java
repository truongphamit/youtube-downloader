package org.truong.youtubedownloader.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.truong.youtubedownloader.R;
import org.truong.youtubedownloader.tools.StaticTools;

/**
 * Created by phamtruong on 2/21/17.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.Viewholder>{
    private Context context;
    private String[] files;

    public VideosAdapter(Context context) {
        this.context = context;
        files = StaticTools.getVideos();
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.item_video, parent, false);

        // Return a new holder instance
        return new VideosAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        final String file = files[position];
        Bitmap img = ThumbnailUtils.createVideoThumbnail(StaticTools.getHomePath() + file, MediaStore.Images.Thumbnails.MINI_KIND);
        holder.img_thumb.setImageBitmap(img);
        holder.tv_title.setText(file);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(StaticTools.getHomePath() + file));
                intent.setDataAndType(Uri.parse(StaticTools.getHomePath() + file), "video/mp4");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    static class Viewholder extends RecyclerView.ViewHolder {
        ImageView img_thumb;
        TextView tv_title;
        View item;

        Viewholder(View itemView) {
            super(itemView);
            img_thumb = (ImageView) itemView.findViewById(R.id.img_thumb);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            item = itemView.findViewById(R.id.item);
        }
    }
}
