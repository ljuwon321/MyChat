package com.ljuwon.mychat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private Context context;
    private List<ChatData> listItem;
    private LayoutInflater layoutInflater;

    public ChatAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.listItem = new ArrayList<>();
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public ChatAdapter(Activity activity, ArrayList<ChatData> listItem) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.listItem = listItem;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);

        return new ChatHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder originHolder, int position) {
        ChatHolder holder = (ChatHolder) originHolder;
        ChatData data = listItem.get(position);

        holder.nick.setText(data.getUserName());
        holder.time.setText(data.getTime());
        Glide.with(context).load(data.getImage_url()).into(holder.image);

        Log.d("MyChat/Adapter", "START: " + data.getType());

        try {
            if(data.getType().equals(ChatData.MESSAGE)) {
                holder.message.setVisibility(View.VISIBLE);
                holder.messageImage.setVisibility(View.GONE);
                holder.message.setText(data.getMessage());
                Log.d("MyChat/Adapter", "text");
            } else if(data.getType().equals(ChatData.IMAGE)) {
                holder.message.setVisibility(View.GONE);
                holder.messageImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(data.getMessage()).into(holder.messageImage);
                Log.d("MyChat/Adapter", "image");
            }
            Log.d("MyChat/Adapter", "END");
        } catch (Exception e) {
            e.printStackTrace();
            holder.messageImage.setVisibility(View.GONE);
            holder.message.setText(data.getMessage());
            Log.d("MyChat/Adapter", "error");
        }
    }

    @Override
    public int getItemCount() {
        try {
            return listItem.size();
        } catch(Exception e) {
            return 0;
        }
    }

    public void addItem(ChatData item) {
        if(item == null) {
            Log.w("MyChat/chatList", "item is null!");
            return;
        }
        listItem.add(item);
        this.notifyDataSetChanged();
    }

    public void setItems(ArrayList<ChatData> list) {
        listItem = list;
        this.notifyDataSetChanged();
    }

    private class ChatHolder extends RecyclerView.ViewHolder {
        public TextView nick;
        public TextView message;
        public TextView time;
        public ImageView messageImage;
        public CircleImageView image;

        public ChatHolder(View itemView) {
            super(itemView);

            nick = (TextView) itemView.findViewById(R.id.nick);
            message = (TextView) itemView.findViewById(R.id.message);
            time = (TextView) itemView.findViewById(R.id.ctime);
            messageImage = (ImageView) itemView.findViewById(R.id.image);
            image = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
        }
    }
}
