package com.example.projectgit.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectgit.Constellation.SetConstellation;
import com.example.projectgit.Data.Constellation;
import com.example.projectgit.Data.ItemClickListener;
import com.example.projectgit.R;
import com.example.projectgit.main.MainActivity;

import java.util.ArrayList;

public class RcvAdapter extends RecyclerView.Adapter<RcvAdapter.ItemRowHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Constellation> al_const;
    private ItemClickListener<Constellation> itemClickListener;
    private Context context_1;


    public RcvAdapter(ArrayList<Constellation> al_const,Context context_1){
        this.al_const = al_const;
        this.context_1 = context_1;
    }

    public void setOnClickListener(SetConstellation setConstellation) {
        this.mListener = setConstellation;

    }

    public void remove(int position){
        try {
            al_const.remove(position);
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
    public void setOnItemClickListener(ItemClickListener<Constellation> itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 1번 밖에 실행이 안됨, 세트를 만들어 주기 위한 친구
       //View view = inflater.inflate(R.layout.item_rcv, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv, parent, false);
        return new RcvAdapter.ItemRowHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemRowHolder holder, int position) {
        // 일해야하는 친구

        Constellation cons = al_const.get(position);
        holder.content.setText(cons.getContent());
        holder.image.setImageResource(cons.getImage());


        if (mListener != null){
            final int pos = position;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (al_const.get(position).getImage()) {
                    case R.drawable.book_aquarius_check:
                    case R.drawable.book_aries_check:
                    case R.drawable.book_cancer_check:
                    case R.drawable.book_capricorn_check:
                    case R.drawable.book_gemini_check:
                    case R.drawable.book_leo_check:
                    case R.drawable.book_libra_check:
                    case R.drawable.book_pisces_check:
                    case R.drawable.book_sagittarius_check:
                    case R.drawable.book_scorpius_check:
                    case R.drawable.book_taurus_check:
                    case R.drawable.book_virgo_check:
                        Toast.makeText(context_1 , "이미 완성한 별자리입니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.drawable.book_aquarius:
                    case R.drawable.book_aries:
                    case R.drawable.book_cancer:
                    case R.drawable.book_capricorn:
                    case R.drawable.book_gemini:
                    case R.drawable.book_leo:
                    case R.drawable.book_libra:
                    case R.drawable.book_pisces:
                    case R.drawable.book_sagittarius:
                    case R.drawable.book_scorpius:
                    case R.drawable.book_taurus:
                    case R.drawable.book_virgo:
                        mListener.onItemClicked(pos);
                        itemClickListener.onItemClick(position, al_const.get(position));
                        break;
                }
            }
            });
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onContentClicked(pos);
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
        return al_const.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }
    public class ItemRowHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView content;
        ImageView image;


        public ItemRowHolder(@NonNull View itemView){
            super(itemView);

            content = itemView.findViewById(R.id.item_rcv_tv);
            image = itemView.findViewById(R.id.item_rcv_iv);
            /*
            iv = itemView.findViewById(R.id.item_rcv_iv);
            tv = itemView.findViewById(R.id.item_rcv_tv);
             */
        }

    }


}

