package com.example.yasmina;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class ShowAdapter extends ArrayAdapter<Content> {
    ImageView imageView;
    TextView textView,timeText,dateText;
    String ifTime;
    public ShowAdapter(@NonNull Context context, int resource, List<Content>object) {
        super(context, resource,object);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.section_layout, parent, false);
        }
        textView=(TextView)convertView.findViewById(R.id.nameTextView);
        imageView=(ImageView) convertView.findViewById(R.id.photoImageView);
        timeText=(TextView)convertView.findViewById(R.id.timeTextView);
        dateText=(TextView)convertView.findViewById(R.id.dateTextView);

        Content c1=getItem(position);
        Glide.with(getContext()).load(c1.getImg()).into(imageView);
        textView.setText(c1.getText());

        String fullDate=c1.getTime();

        if (fullDate !=null) {
            String[] divDate = fullDate.split("T");

            dateText.setText(divDate[0]);
            String check=divDate[1].substring(0,2);
            if (Integer.parseInt(check) > 12)
                ifTime="pm";
            else
                ifTime="am";
            timeText.setText(divDate[1].substring(0,5)+" "+ifTime);

        }

        return convertView;
    }
}
