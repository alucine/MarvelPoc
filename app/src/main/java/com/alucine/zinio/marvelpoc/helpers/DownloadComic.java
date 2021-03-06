package com.alucine.zinio.marvelpoc.helpers;

import android.os.AsyncTask;

import com.karumi.marvelapiclient.ComicApiClient;
import com.karumi.marvelapiclient.MarvelApiConfig;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.model.CharactersDto;
import com.karumi.marvelapiclient.model.ComicsDto;
import com.karumi.marvelapiclient.model.ComicsQuery;
import com.karumi.marvelapiclient.model.MarvelResponse;

public class DownloadComic extends AsyncTask<Void, Void, MarvelResponse<ComicsDto>> {
    private final String pubKey = "96b9207b3d700250cf6215e95c1522fa";
    private final String priKey = "ff05a4d55ac07ddfc8590dec657a5e1156a5ad93";
    private DownloadComicssListener listener;
    private int comicID;

    public DownloadComic(DownloadComicssListener listener, int comicID) {
        this.listener = listener;
        this.comicID = comicID;
    }

    protected MarvelResponse<ComicsDto> doInBackground(Void... urls) {
        MarvelApiConfig marvelApiConfig =
                new MarvelApiConfig.Builder(pubKey, priKey).debug().build();

        ComicApiClient comicApiClient = new ComicApiClient(marvelApiConfig);
        ComicsQuery query = ComicsQuery.Builder.create().addCharacter(comicID).build();
        try {
            MarvelResponse<ComicsDto> all = comicApiClient.getAll(query);
            return all;
        } catch (MarvelApiException exception) {
            return null;
        }
    }

    protected void onPostExecute(MarvelResponse<ComicsDto> response) {
        listener.onDownloadComicsLoaded(response);
    }

    public interface DownloadComicssListener {
        public void onDownloadComicsLoaded(MarvelResponse<ComicsDto> response);
    }
}