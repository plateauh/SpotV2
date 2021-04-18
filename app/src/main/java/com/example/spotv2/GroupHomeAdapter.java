package com.example.spotv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        Button inviteBtn, leaveBtn;
        database DB;
        SharedPreferences sharedpreferences;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DB = new database(context);
            sharedpreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to group
                    Intent intent = new Intent(context, MapActivity.class);
                    Log.i("Adapter","GroupID: "+ GroupsIDs[getAdapterPosition()]);
                    intent.putExtra("groupID", GroupsIDs[getAdapterPosition()]);
                    Log.i("Adapter class","GroupID: "+ GroupsIDs[getAdapterPosition()]);

                    context.startActivity(intent);
                }
            });
            idText = itemView.findViewById(R.id.groupName);
            inviteBtn = itemView.findViewById(R.id.invite);
            inviteBtn.setOnClickListener(v -> {
                Intent intent = new Intent(context, InviteFormActivity.class);
                intent.putExtra("groupID", GroupsIDs[getAdapterPosition()]);
                context.startActivity(intent);
            });
            leaveBtn = itemView.findViewById(R.id.leave);
            leaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = sharedpreferences.getString("usernameKey","");
                    boolean isDeleted = DB.deleteUserGroup(username, GroupsIDs[getAdapterPosition()]);
                    if (isDeleted){
                        Toast.makeText(context, "You left "+GroupNames[getAdapterPosition()], Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, HomePage2Activity.class);
                        context.startActivity(intent);
                    }
                    else {
                        Toast.makeText(context, "An error occurred, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}