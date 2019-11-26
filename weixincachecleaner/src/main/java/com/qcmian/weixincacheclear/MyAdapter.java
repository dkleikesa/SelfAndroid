package com.qcmian.weixincacheclear;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyAdapter extends RecyclerView.Adapter {

    public List<ItemBean> mList;
    boolean showNum = false;
    public OnItemClickListener onItemClickListener;

    Set<String> mSelectList = new HashSet<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mList == null) {
            return;
        }
        ItemBean bean = mList.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Bitmap bitmap = BitmapFactory.decodeFile(bean.filePath);
        if (bitmap != null) {
            myViewHolder.imageView.setImageBitmap(bitmap);
        } else {
            myViewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
        }
        myViewHolder.imageView.setTag(bean.md5);
        myViewHolder.checkBox.setTag(bean.md5);
        if (mSelectList.contains(bean.md5)) {
            myViewHolder.checkBox.setChecked(true);
        } else {
            myViewHolder.checkBox.setChecked(false);
        }
        if (bean.num > 0 && (this.showNum)) {
            myViewHolder.textView.setText(String.valueOf(bean.num));
        } else {
            myViewHolder.textView.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, (String) v.getTag());
                    }
                }
            });
            textView = itemView.findViewById(R.id.num);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onCheckedChanged(buttonView, isChecked, (String) buttonView.getTag());
                    }
                    String md5 = (String) buttonView.getTag();
                    if (isChecked) {
                        if (!mSelectList.contains(md5)) {
                            mSelectList.add(md5);
                        }
                    } else {
                        if (mSelectList.contains(md5)) {
                            mSelectList.remove(md5);
                        }
                    }
                }
            });
        }
    }


    public void updateData(List<ItemBean> list, boolean showNum) {
        mList = list;
        this.showNum = showNum;
        notifyDataSetChanged();
    }


    public static interface OnItemClickListener {
        void onItemClick(View view, String md5);

        void onCheckedChanged(View view, boolean isChecked, String md5);
    }
}
