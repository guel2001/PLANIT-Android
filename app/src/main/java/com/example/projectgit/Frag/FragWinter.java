package com.example.projectgit.Frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.projectgit.Adapter.MyBookAdapter;
import com.example.projectgit.Data.ConstellationBook_Item;
import com.example.projectgit.EventDialogFragment.Dialog_Winter;
import com.example.projectgit.R;
import com.example.projectgit.main.MainActivity;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class FragWinter extends Fragment {

    private MyBookAdapter adapter;
    private ArrayList<ConstellationBook_Item> list = new ArrayList<>();
    private RecyclerView recyclerView;


    public static FragWinter  newInstance() {
        FragWinter  fragWinter = new FragWinter ();
        return fragWinter ;
    }
    public FragWinter(){
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_winter, container, false);
        View x = inflater.inflate(R.layout.item_cardview,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        list = ConstellationBook_Item.createContactsList(1);
        setLockImage(list);
        ArrayList<ConstellationBook_Item> Winter_constellation = (ArrayList<ConstellationBook_Item>) list.stream()
                .filter(s-> s.season.equals("winter"))
                .collect(Collectors.toList());    //별자리 Item 에서 겨울철 별자리 추출

        adapter = new MyBookAdapter(getActivity(), Winter_constellation);
        StaggeredGridLayoutManager mStgaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mStgaggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);

        ImageView imageView = x.findViewById(R.id.Constellation_lock);


        adapter.setOnItemClickListener(new    MyBookAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View v, int pos) {
                Dialog_Winter e = Dialog_Winter.getInstance();
                switch (pos) {
                    case 0:
                        e.setPos(pos);
                        Lock_Open(MainActivity.finished_cons_code,"10",e,"황소자리");
                        break;

                    case 1:
                        e.setPos(pos);
                        Lock_Open(MainActivity.finished_cons_code,"4",e,"쌍둥이자리");
                        break;

                    case 2:
                        e.setPos(pos);
                        Lock_Open(MainActivity.finished_cons_code,"2",e,"게자리");

                        break;
                }

            }

        })                       ;





        return view;
    }
    public void Lock_Open(ArrayList<String> a,String code, Dialog_Winter e,String cons_name){
        for (int i = 0; i < MainActivity.finished_cons_code.size(); i++) {

            if (MainActivity.finished_cons_code.get(i).contains(code)) {
                e.show(getChildFragmentManager(), Dialog_Winter.TAG_EVENT_DIALOG);
                break;
            }

        }
    }
    public void setLockImage(ArrayList<ConstellationBook_Item> list){
        for(int i = 0;  i < list.size();i++) {
            for (int j = 0; j < MainActivity.finished_cons_code.size(); j++) {
                if (MainActivity.finished_cons_code.get(j).equals(list.get(i).getCode())) {
                    list.get(i).setLock(0);
                }
            }
        }
    }
}
