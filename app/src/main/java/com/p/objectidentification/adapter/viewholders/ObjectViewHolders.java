package com.p.objectidentification.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.p.objectidentification.R;

public class ObjectViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView objectName;
    public ImageView objectImage;

    public ObjectViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        objectName = (TextView) itemView.findViewById(R.id.textview_object);
        objectImage = (ImageView) itemView.findViewById(R.id.imageview_object);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}