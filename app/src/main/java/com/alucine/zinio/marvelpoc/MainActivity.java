package com.alucine.zinio.marvelpoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.alucine.zinio.marvelpoc.adapter.CharacterAdapter;
import com.alucine.zinio.marvelpoc.helpers.DownloadCharacters;
import com.alucine.zinio.marvelpoc.helpers.EmptyRecyclerView;
import com.alucine.zinio.marvelpoc.helpers.FavoriteManager;
import com.alucine.zinio.marvelpoc.object.CharacterInfo;
import com.karumi.marvelapiclient.model.CharacterDto;
import com.karumi.marvelapiclient.model.CharactersDto;
import com.karumi.marvelapiclient.model.MarvelImage;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        DownloadCharacters.DownloadCharactersListener, CharacterAdapter.OnClickCardListener {
    private CharacterAdapter ca;
    private ArrayList<CharacterInfo> characterData = new ArrayList<>();
    private int previousTotal = 0;
    private boolean loading = true;
    private final int visibleThreshold = 5;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private MenuItem mProgressMenu;
    private Toolbar toolbar;
    private ArrayList<String> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);

        setView();

        initImageLoader();
    }

    private void setView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        final EmptyRecyclerView cardList = (EmptyRecyclerView) findViewById(R.id.cardList);
        cardList.setHasFixedSize(true);
        final LinearLayoutManager llm = new GridLayoutManager(this, 2);
        cardList.setLayoutManager(llm);
        cardList.setEmptyView(findViewById(R.id.emptyCharacters));
        cardList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = cardList.getChildCount();
                totalItemCount = llm.getItemCount();
                firstVisibleItem = llm.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loading = true;
                    requestCharacters();
                }
            }
        });

        ca = new CharacterAdapter(characterData,this);
        cardList.setAdapter(ca);

        favorites = FavoriteManager.loadFavorites(this);
    }

    private void requestCharacters(){
        mProgressMenu.setVisible(true);
        new DownloadCharacters(this,totalItemCount).execute();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mProgressMenu = menu.findItem(R.id.menu_progress);

        requestCharacters();

        return true;
    }

    @Override
    public void onDownloadCharactersLoaded(MarvelResponse<CharactersDto> response) {
        mProgressMenu.setVisible(false);
        TextView emptyCharacters = (TextView) findViewById(R.id.emptyCharacters);

        if ( response != null ) {
            if ( response.getCode() == 200 ) {
                for (CharacterDto characterDto : response.getResponse().getCharacters()) {
                    CharacterInfo characterInfo = new CharacterInfo();
                    characterInfo.title = characterDto.getName();
                    characterInfo.imageUrl = characterDto.getThumbnail().getImageUrl(MarvelImage.Size.STANDARD_XLARGE);
                    characterInfo.comicId = Integer.parseInt(characterDto.getId());
                    characterInfo.isFavourite = ( favorites == null ? false : favorites.contains(characterDto.getId()));
                    characterData.add(characterInfo);
                }
                toolbar.setSubtitle(getString(R.string.characters) + " " + characterData.size());
                ca.notifyDataSetChanged();
            } else {
                emptyCharacters.setText(R.string.error_downloading_characters);
            }
        } else {
            emptyCharacters.setText(R.string.error_downloading_characters);
        }
    }

    @Override
    public void onClickCard(int position) {
        Intent intent = new Intent(this,DetailCharacter.class);
        Bundle args = new Bundle();
        intent.putExtras(args);
        intent.putExtra(DetailCharacter.DATA,characterData.get(position));
        startActivity(intent);
    }
}
