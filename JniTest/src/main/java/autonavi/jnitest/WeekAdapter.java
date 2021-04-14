package autonavi.jnitest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class WeekAdapter extends ArrayAdapter {
    int resourceId;

    public WeekAdapter(@NonNull Context context, int resource, List<Main3Activity.Week> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    static int idx = 0;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        idx++;
        Log.e("WeekAdapter", "getView:" + idx);
        Main3Activity.Week fruit = (Main3Activity.Week) getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView fruitImage = (ImageView) view.findViewById(R.id.fruit_image);//获取该布局内的图片视图
        TextView fruitName = (TextView) view.findViewById(R.id.fruit_name);//获取该布局内的文本视图
        fruitImage.setImageResource(fruit.id);//为图片视图设置图片资源
        fruitName.setText(fruit.name);//为文本视图设置文本内容
        return view;
    }

}
