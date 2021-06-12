package com.example.projectgit.Frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.projectgit.Adapter.MyBookAdapter;
import com.example.projectgit.Data.ConstellationBook_Item;
import com.example.projectgit.EventDialogFragment.Dialog_Summer;
import com.example.projectgit.R;
import com.example.projectgit.main.MainActivity;



import java.util.ArrayList;
import java.util.stream.Collectors;



public class FragSummer extends Fragment {

    private MyBookAdapter adapter;
    private ArrayList<ConstellationBook_Item> list = new ArrayList<>();
    private RecyclerView recyclerView;

    public static FragSummer newInstance() {
        FragSummer fragSummer = new FragSummer();
        return fragSummer;
    }
    public FragSummer(){
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_summer, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        list = ConstellationBook_Item.createContactsList(1);
        setLockImage(list);
        ArrayList<ConstellationBook_Item> Summer_constellation = (ArrayList<ConstellationBook_Item>) list.stream()
                .filter(s-> s.season.equals("summer"))
                .collect(Collectors.toList());    //별자리 Item 에서 여름철 별자리 추출

        adapter = new MyBookAdapter(getActivity(), Summer_constellation);
        StaggeredGridLayoutManager mStgaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mStgaggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new    MyBookAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View v, int pos) {
                Dialog_Summer e = Dialog_Summer.getInstance();
                switch (pos) {
                    case 0:
                        e.setPos(pos);
                        Lock_Open(MainActivity.finished_cons_code,"9",e,"전갈자리");
                        break;

                    case 1:
                        e.setPos(pos);
                        Lock_Open(MainActivity.finished_cons_code,"8",e,"궁수자리");
                        break;

                    case 2:
                        e.setPos(pos);
                        Lock_Open(MainActivity.finished_cons_code,"3",e,"염소자리");

                        break;
                }

            }

        });


        return view;
    }
    public void Lock_Open(ArrayList<String> a, String code, Dialog_Summer e, String cons_name){
        for (int i = 0; i < MainActivity.finished_cons_code.size(); i++) {

            if (MainActivity.finished_cons_code.get(i).contains(code)) {
                e.show(getChildFragmentManager(), Dialog_Summer.TAG_EVENT_DIALOG);
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
