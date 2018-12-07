package com.p.objectidentification.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.p.objectidentification.R;
import com.p.objectidentification.adapter.viewholders.ObjectViewHolders;
import com.p.objectidentification.imagelabeler.ImageLabeler;
import com.p.objectidentification.model.ItemObject;
import com.p.objectidentification.utility.FirebaseImageUtility;

import java.util.List;

public class ObjectRecyclerViewAdapter extends RecyclerView.Adapter<ObjectViewHolders> {

    private List<ItemObject> itemList;
    private Context context;
    Bitmap bitmap;

    public ObjectRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    /**
     * @param itemList
     */
    public void setItems(List<ItemObject> itemList) {
        this.itemList = itemList;
    }


    @Override
    public ObjectViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null);
        ObjectViewHolders rcv = new ObjectViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ObjectViewHolders holder, final int position) {
        holder.objectName.setText(itemList.get(position).getName());

        final Bitmap bitmap = FirebaseImageUtility.decodeSampledBitmapFromResource(itemList.get(position).getImagePath());
        holder.objectImage.setImageBitmap(bitmap);

        if (itemList.get(position).getName().isEmpty())
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {

                    startDetector(position, holder, BitmapFactory.decodeFile(itemList.get(position).getImagePath()));
                }
            });

    }


    /**
     *
     * @param position
     * @param objectViewHolders
     * @param bitmap
     */
    private void startDetector(final int position, final ObjectViewHolders objectViewHolders, Bitmap bitmap) {

        FirebaseVisionImage image = FirebaseImageUtility.imageFromBitmap(bitmap);
        ImageLabeler imageLabeler = ImageLabeler.getInstance();
        imageLabeler.setSuccessListener(new OnSuccessListener<List<FirebaseVisionLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionLabel> labels) {
                // Task completed successfully
                // [START_EXCLUDE]
                // [START get_labels]

                for (FirebaseVisionLabel label : labels) {
                    String text = label.getLabel();
                    String entityId = label.getEntityId();
                    objectViewHolders.objectName.append(text);
                    itemList.get(position).setName(text);
                    float confidence = label.getConfidence();
                    break;
                }
                // [END get_labels]
                // [END_EXCLUDE]
            }
        });
        imageLabeler.setFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Task failed with an exception
                // ...
            }
        });
        imageLabeler.labelImages(image, false, 0);


    }


    @Override
    public int getItemCount() {
        if (itemList != null) {
            return this.itemList.size();
        } else {
            return 0;
        }
    }
}