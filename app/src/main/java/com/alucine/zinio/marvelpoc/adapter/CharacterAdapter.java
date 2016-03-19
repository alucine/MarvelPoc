package com.alucine.zinio.marvelpoc.adapter;

import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alucine.zinio.marvelpoc.R;
import com.alucine.zinio.marvelpoc.object.CharacterInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {
    private ArrayList<CharacterInfo> contactList;
    private DisplayImageOptions options;
    private OnClickCardListener listener;

    public CharacterAdapter(ArrayList<CharacterInfo> contactList,OnClickCardListener listener) {
        this.contactList = contactList;
        this.listener = listener;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .considerExifParams(true)
                .build();
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder characterViewHolder, final int i) {
        final CharacterInfo ci = contactList.get(i);
        ImageLoader.getInstance().displayImage(ci.imageUrl, characterViewHolder.imageCharacter,options);
        characterViewHolder.titleCharacter.setText(ci.title);
        characterViewHolder.btCharacterFavorite.setBackgroundResource(ci.isFavourite ? R.drawable.ic_favorite : R.drawable.ic_unfavorite);
        characterViewHolder.btCharacterFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ci.isFavourite = !ci.isFavourite;
                notifyDataSetChanged();
            }
        });
        characterViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickCard(i);
            }
        });
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                                        inflate(R.layout.cardview_row, viewGroup, false);

        return new CharacterViewHolder(itemView);
    }

    public static class CharacterViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageCharacter;
        protected TextView titleCharacter;
        protected CardView card_view;
        protected ImageButton btCharacterFavorite;

        public CharacterViewHolder(View v) {
            super(v);
            imageCharacter =  (ImageView) v.findViewById(R.id.imageCharacter);
            titleCharacter = (TextView)  v.findViewById(R.id.titleCharacter);
            card_view = (CardView) v.findViewById(R.id.card_view);
            btCharacterFavorite = (ImageButton) v.findViewById(R.id.btCharacterFavorite);
        }
    }

    public interface OnClickCardListener {
        public void onClickCard(int position);
    }
}
