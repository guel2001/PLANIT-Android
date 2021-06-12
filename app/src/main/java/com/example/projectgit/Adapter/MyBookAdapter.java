package com.example.projectgit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectgit.Data.ConstellationBook_Item;
import com.example.projectgit.R;

import java.util.ArrayList;
import java.util.List;

public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.Holder> {

    private Context context;
    private List<ConstellationBook_Item> list = new ArrayList<>();

    public MyBookAdapter(Context context, List<ConstellationBook_Item> list) {
        this.context = context;
        this.list = list;
    }

    // ViewHolder 생성
    // row layout을 화면에 뿌려주고 holder에 연결
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    /*
     * Todo 만들어진 ViewHolder에 data 삽입 ListView의 getView와 동일
     *
     * */
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 각 위치에 문자열 세팅
        int itemposition = position;
        holder.imageView.setImageResource(list.get(itemposition).image);
        holder.textView.setText(list.get(itemposition).imagetitle);
        holder.season.setText(list.get(itemposition).season);
        holder.lockview.setImageResource(list.get(itemposition).lock);
        holder.code.setText(list.get(itemposition).code);
    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        try {
            return list.size();
        } catch (Exception ex){return 0;}

    }

    public class Holder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public TextView season; //계절 구분 view는 invisible
        public TextView code;
        public ImageView lockview;
        public Holder(View view){


            super(view);

            this.imageView = (ImageView) view.findViewById(R.id.imageView2);
            this.textView = (TextView) view.findViewById(R.id.imagetitle);
            this.season=(TextView) view.findViewById(R.id.seasonText);
            this.lockview= (ImageView) view.findViewById(R.id.Constellation_lock);
            this.code = (TextView) view.findViewById(R.id.codeText);
            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    int pos = getAdapterPosition();
                    // 리스너 객체의 메서드 호출
                    if (pos != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemClick(v, pos);
                    }

                }
            });
        }
    }


    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}