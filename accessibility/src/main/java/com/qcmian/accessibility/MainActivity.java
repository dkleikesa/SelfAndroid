package com.qcmian.accessibility;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Model model;
    int newIndex = -1;
    FloatingButton floatingButton;
    List<FloatingButton> floatingButtonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.edit_layout, null);
        layout.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                floatingButton.remove();

                for (FloatingButton floatingButton : floatingButtonList) {
                    model.list.get(newIndex).list.add(floatingButton.getCenter());
                    floatingButton.remove();
                }
                floatingButtonList.clear();
                saveModel();
                newIndex = -1;
            }
        });

        layout.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View layout = inflater.inflate(R.layout.tag_layout, null);
                TextView tv = layout.findViewById(R.id.num);
                tv.setText(String.valueOf(floatingButtonList.size() + 1));
                FloatingButton button = new FloatingButton(layout, MainActivity.this);
                button.show();
                floatingButtonList.add(button);
            }
        });
        layout.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatingButtonList.size() > 0) {
                    FloatingButton button = floatingButtonList.remove(floatingButtonList.size() - 1);
                    button.remove();
                }
            }
        });
        floatingButton = new FloatingButton(layout, this);


        readModel();
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return (createItemView(i));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.button.setTag(i);
            holder.checkBox.setTag(i);
            holder.editText.setTag(i);
            holder.isBottom = (i == (model.list.size()));
            if (holder.isBottom) {
                holder.editText.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.GONE);
                holder.button.setText("+");
            } else {
                holder.editText.setVisibility(View.VISIBLE);
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.button.setText("-");
                holder.editText.setEnabled(i == newIndex);
                holder.checkBox.setChecked(model.index.contains(i));
                holder.editText.setText(model.list.get(i).name);
            }
        }

        @Override
        public int getItemCount() {
            return model.list.size() + 1;
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {

        boolean isBottom = false;
        EditText editText;
        Button button;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    ViewHolder createItemView(final int i) {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        final ViewHolder holder = new ViewHolder(linearLayout);

        final EditText editText = new EditText(this);
        editText.setEnabled(i == newIndex);
        linearLayout.addView(editText);
        editText.setWidth(400);
        editText.addTextChangedListener(new TextWatcher() {
            EditText edit = editText;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                model.list.get((int) edit.getTag()).name = s.toString();
            }
        });
        Button button = new Button(this);
        button.setText("-");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isBottom) {
                    if (newIndex >= 0) {
                        return;
                    }
                    Model.Item item = new Model.Item();
                    item.list = new ArrayList<>();
                    model.list.add(item);
                    newIndex = model.list.size() - 1;
                    adapter.notifyDataSetChanged();
                    floatingButton.show();
                } else {
                    model.list.remove((int) v.getTag());
                    newIndex = -1;
                    model.index.remove(v.getTag());
                    adapter.notifyDataSetChanged();
                    saveModel();
                }
            }
        });
        linearLayout.addView(button);
        CheckBox checkBox = new CheckBox(this);
        checkBox.setChecked(model.index.contains(i));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer integer =(Integer) v.getTag();
               if( model.index.contains(integer)){
                   model.index.remove(integer);
               }else {
                   model.index.add(integer);
               }

                adapter.notifyDataSetChanged();
                saveModel();
            }
        });
        button.setTag(i);
        checkBox.setTag(i);
        editText.setTag(i);
        linearLayout.addView(checkBox);


        holder.isBottom = (i == (model.list.size()));
        holder.editText = editText;
        holder.button = button;
        holder.checkBox = checkBox;
        if (holder.isBottom) {
            editText.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            button.setText("+");
        }
        return holder;
    }

    void saveModel() {
        SharedPreferences sp = getSharedPreferences("MAIN_SP", MODE_PRIVATE);
        if (model != null) {
            sp.edit().putString("model", GSONProvider.get().toJson(model).toString()).commit();
        }

    }

    void readModel() {
        SharedPreferences sp = getSharedPreferences("MAIN_SP", MODE_PRIVATE);
        String str = sp.getString("model", "");
        if (!TextUtils.isEmpty(str)) {
            model = GSONProvider.get().fromJson(str, Model.class);
        }
        if (model == null) {
            model = new Model();
            model.list = new ArrayList<>();
            model.index= new ArrayList<>();
        }
    }
}
