package com.example.projectgit.Adapter;

import android.content.ClipData;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectgit.Data.DataSpring;
import com.example.projectgit.Data.ItemClickListener;
import com.example.projectgit.Frag.FragSpring;
import com.example.projectgit.R;

import java.util.ArrayList;

public class SpringAdapter extends RecyclerView.Adapter<SpringAdapter.ItemViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DataSpring> al_spring;
    private ItemClickListener<DataSpring> itemClickListener;
    private Object FragSpring;

    public SpringAdapter(ArrayList<DataSpring> al_spring){
        /*this.context = context;
        this.inflater = LayoutInflater.from(context);*/
        this.al_spring = al_spring;

    }


    public void setOnClickListener(FragSpring fragSpring) {
        this.mListener = (MyRecyclerViewClickListener) FragSpring;
    }

    public void remove(int position){
        try {
            al_spring.remove(position);
            notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public interface MyRecyclerViewClickListener{
        void onItemClicked(int position);
        void onTitleClicked(int position);
        void onContentClicked(int position);
        void onItemLongClicked(int position);
        void onImageViewClicked(int position);
    }
    private MyRecyclerViewClickListener mListener;
    public void setOnItemClickListener(ItemClickListener<DataSpring> itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_spring, parent, false);

        return new SpringAdapter.ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DataSpring cons = al_spring.get(position);
        holder.image.setImageResource(cons.getImage());


        if (mListener != null){
            final int pos = position;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(pos);
                    itemClickListener.onItemClick(position, al_spring.get(position));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onItemLongClicked(holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return al_spring.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{


        ImageView image;


        public ItemViewHolder(@NonNull View itemView){
            super(itemView);

            image = itemView.findViewById(R.id.item_rcv_iv);
        }

    }


}
