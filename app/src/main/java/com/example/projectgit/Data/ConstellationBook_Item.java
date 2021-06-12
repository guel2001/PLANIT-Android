package com.example.projectgit.Data;

import com.example.projectgit.R;

import java.util.ArrayList;
/*별자리 북 객체 */
public class ConstellationBook_Item {
    public int image;
    public String imagetitle;
    public String season;
    public int lock;
    public String code;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }


    public ConstellationBook_Item(int image, String imagetitle, String season,int lock,String code) {
        this.image=image;
        this.imagetitle=imagetitle;
        this.season=season;
        this.lock = lock;
        this.code=code;

    }
    // 입력받은 숫자의 리스트생성
    public static ArrayList<ConstellationBook_Item> createContactsList(int numContacts) {
        ArrayList<ConstellationBook_Item> contacts = new ArrayList<ConstellationBook_Item>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new ConstellationBook_Item(R.drawable.book_virgo,"처녀자리","spring",R.drawable.ic_lock_white_24dp,"11"));
            contacts.add(new ConstellationBook_Item(R.drawable.book_leo,"사자자리","spring",R.drawable.ic_lock_white_24dp,"5"));
            contacts.add(new ConstellationBook_Item(R.drawable.book_libra,"천칭자리","spring",R.drawable.ic_lock_white_24dp,"6"));

            contacts.add(new ConstellationBook_Item(R.drawable.book_scorpius,"전갈자리","summer",R.drawable.ic_lock_white_24dp,"9"));
            contacts.add(new ConstellationBook_Item(R.drawable.book_sagittarius,"궁수자리","summer",R.drawable.ic_lock_white_24dp,"8"));
            contacts.add(new ConstellationBook_Item(R.drawable.book_capricorn,"염소자리","summer",R.drawable.ic_lock_white_24dp,"3"));

            contacts.add(new ConstellationBook_Item(R.drawable.book_aquarius,"물병자리","fall",R.drawable.ic_lock_white_24dp,"0"));
            contacts.add(new ConstellationBook_Item(R.drawable.book_pisces,"물고기자리","fall",R.drawable.ic_lock_white_24dp,"7"));
            contacts.add(new ConstellationBook_Item(R.drawable.book_aries,"양자리","fall",R.drawable.ic_lock_white_24dp,"1"));

            contacts.add(new ConstellationBook_Item(R.drawable.book_taurus,"황소자리","winter",R.drawable.ic_lock_white_24dp,"10"));
            contacts.add(new ConstellationBook_Item(R.drawable.book_gemini,"쌍둥이자리","winter",R.drawable.ic_lock_white_24dp,"4"));
            contacts.add(new ConstellationBook_Item(R.drawable.book_cancer,"게자리","winter",R.drawable.ic_lock_white_24dp,"2"));

        }

        return contacts;
    }

}