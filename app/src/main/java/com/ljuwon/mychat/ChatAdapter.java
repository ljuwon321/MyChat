package com.ljuwon.mychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 주원 on 2017-01-18.
 */
public class ChatAdapter extends BaseAdapter {
    private Context mContext;
    private List<ChatData> listItem = null;
    LayoutInflater mLayoutInflater;
    ViewHolder holder;

    public ChatAdapter(Context context, ArrayList<ChatData> listItem) {
        mContext = context;
        this.listItem = listItem;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return listItem.size();
    }

    public ChatData getItem(int i) {
        return listItem.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    class ViewHolder {
        public TextView nick;
        public TextView message;
        public CircleImageView ci;
        public TextView ctime;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_item, null);

            holder.nick = (TextView) convertView.findViewById(R.id.nick);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.ci = (CircleImageView) convertView.findViewById(R.id.messengerImageView);
            holder.ctime = (TextView) convertView.findViewById(R.id.ctime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChatData mData = listItem.get(position);

        holder.nick.setText(mData.getUserName());
        holder.message.setText(mData.getMessage());
        holder.ctime.setText(mData.getTime());
        Glide.with(mContext).load(mData.getImage_url()).into(holder.ci);

        return convertView;
    }
}
