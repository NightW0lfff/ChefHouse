package com.example.HomeChef;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryViewHolder> {
    private Context context;
    private List<GroceryItem> dataList;

    public GroceryListAdapter(Context context, List<GroceryItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recycler_item, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        holder.recTitle.setText(dataList.get(position).getTitle());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddGroceryItem.class);

                intent.putExtra("ListTitle", dataList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("ListItems", dataList.get(holder.getAdapterPosition()).getItems());
                intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("ItemKey", dataList.get(holder.getAdapterPosition()).getItemKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class GroceryViewHolder extends RecyclerView.ViewHolder{

    TextView recTitle;
    CardView recCard;

    public GroceryViewHolder(@NonNull View itemView){
        super(itemView);

        recTitle = itemView.findViewById(R.id.recListTitle);
        recCard = itemView.findViewById(R.id.recListCard);
    }
}


