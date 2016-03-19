package com.alucine.zinio.marvelpoc;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alucine.zinio.marvelpoc.helpers.DownloadComic;
import com.alucine.zinio.marvelpoc.object.CharacterInfo;
import com.karumi.marvelapiclient.model.CharacterResourceDto;
import com.karumi.marvelapiclient.model.ComicsDto;
import com.karumi.marvelapiclient.model.MarvelImage;
import com.karumi.marvelapiclient.model.MarvelResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.List;
import java.util.Random;

public class DetailCharacter extends AppCompatActivity implements DownloadComic.DownloadComicssListener {
    public final static String DATA = "DATA";
    private MenuItem mProgressMenu;
    private int comicID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_character);
        setView();
    }

    private void setView() {
        Bundle args = getIntent().getExtras();
        CharacterInfo data = args.getParcelable(DATA);
        getSupportActionBar().setTitle(data.title);
        comicID = data.comicId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mProgressMenu = menu.findItem(R.id.menu_progress);

        requestComics();
        return true;
    }

    private void requestComics() {
        mProgressMenu.setVisible(true);
        new DownloadComic(this,comicID).execute();
    }

    @Override
    public void onDownloadComicsLoaded(MarvelResponse<ComicsDto> response) {
        mProgressMenu.setVisible(false);

        findViewById(R.id.loading).setVisibility(View.INVISIBLE);

        TextView numberValue = (TextView) findViewById(R.id.numberValue);
        TextView loading = (TextView) findViewById(R.id.loading);
        TextView selectedValue = (TextView) findViewById(R.id.selectedValue);
        TextView descriptionValue = (TextView) findViewById(R.id.descriptionValue);
        TextView charactersValue = (TextView) findViewById(R.id.charactersValue);
        ImageView image = (ImageView) findViewById(R.id.image);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .considerExifParams(true)
                .build();

        if ( response != null ) {
            if ( response.getCode() == 200 ) {
                int count = response.getResponse().getCount();
                numberValue.setText("" + count);

                if (count > 0) {
                    Random rnd = new Random();
                    int selected = rnd.nextInt(count);

                    selectedValue.setText("" + selected);
                    descriptionValue.setText(response.getResponse().getComics().get(selected).getDescription());

                    String characters = "";
                    for (CharacterResourceDto characterResourceDto : response.getResponse().getComics().get(selected).getCharacters().getItems()) {
                        characters = characters + characterResourceDto.getName() + " , ";
                    }
                    charactersValue.setText(characters);

                    List<MarvelImage> images = response.getResponse().getComics().get(0).getImages();

                    if ( images.size() > 0 ){
                        int imgSelected = rnd.nextInt(images.size());

                        ImageLoader.getInstance().displayImage(images.get(imgSelected).getImageUrl(MarvelImage.Size.FULLSIZE), image,options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                progressBar.setVisibility(View.VISIBLE);
                            }
                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            }
                        });
                    }

                } else  {
                    selectedValue.setText("-");
                    descriptionValue.setText(R.string.no_available);
                    charactersValue.setText(R.string.no_available);
                }
            } else {
                loading.setText(R.string.error_downloading_comics);
            }
        } else {
            loading.setText(R.string.error_downloading_comics);
        }
    }
}
