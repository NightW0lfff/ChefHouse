package com.example.housechefv03;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataClass> dataList, favoriteList;
    DatabaseReference databaseReference;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.favoriteList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recDesc.setText(dataList.get(position).getDataDescription());

        boolean isFavorite = dataList.get(position).getDataFavorite();
        if (isFavorite) {
            Glide.with(context).load(R.drawable.baseline_star_24).into(holder.recIcon);
        } else {
            Glide.with(context).load(R.drawable.baseline_star_border_24).into(holder.recIcon);
        }

        holder.recIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean newFavoriteStatus = !dataList.get(holder.getAdapterPosition()).getDataFavorite();
                dataList.get(holder.getAdapterPosition()).setDataFavorite(newFavoriteStatus);
                notifyItemChanged(holder.getAdapterPosition());

                databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");

                String key = dataList.get(holder.getAdapterPosition()).getKey();
                databaseReference.child(key).child("dataFavorite").setValue(newFavoriteStatus);
            }
        });

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDescription());
                intent.putExtra("Ingredient", dataList.get(holder.getAdapterPosition()).getDataIngredient());
                intent.putExtra("Instruction", dataList.get(holder.getAdapterPosition()).getDataInstruction());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }

    public void setSearchDataList(ArrayList<DataClass> searchList){
        dataList.clear();
        favoriteList.clear();

        for (DataClass item : searchList) {
            dataList.add(item);
            if (item.getDataFavorite()) {
                favoriteList.add(item);
            }
        }

        notifyDataSetChanged();
    }

    public void showFavoriteRecipes() {
        dataList.clear();
        dataList.addAll(favoriteList);
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage, recIcon;
    TextView recTitle, recDesc, recIngredient, recInstruction;
    CardView recCard;


    public MyViewHolder(@NonNull View itemView){
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recTitle = itemView.findViewById(R.id.recTitle);
        recIcon = itemView.findViewById(R.id.starIcon);

    }
}
