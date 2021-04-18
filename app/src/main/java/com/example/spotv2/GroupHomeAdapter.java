package com.example.spotv2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupHomeAdapter extends RecyclerView.Adapter<GroupHomeAdapter.MyViewHolder> {


    int GroupsIDs[];
    String[] GroupNames;

    Context context;


    public GroupHomeAdapter(Context ct, int ids[], String[] names){
        context= ct;
        GroupsIDs = ids;
        GroupNames = names;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.group_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.idText.setText(GroupNames[position]);
    }

    @Override
    public int getItemCount() {
        return GroupsIDs.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idText;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to group
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra("groupID", GroupsIDs[getAdapterPosition()]);

                    context.startActivity(intent);
                }
            });
            idText = itemView.findViewById(R.id.groupName);
        }
    }


}