package com.alucine.zinio.marvelpoc.helpers;

import android.os.AsyncTask;

import com.karumi.marvelapiclient.CharacterApiClient;
import com.karumi.marvelapiclient.MarvelApiConfig;
import com.karumi.marvelapiclient.MarvelApiException;
import com.karumi.marvelapiclient.model.CharactersDto;
import com.karumi.marvelapiclient.model.CharactersQuery;
import com.karumi.marvelapiclient.model.MarvelResponse;

public class DownloadCharacters extends AsyncTask<Void, Void, MarvelResponse<CharactersDto>> {
    private final String pubKey = "96b9207b3d700250cf6215e95c1522fa";
    private final String priKey = "ff05a4d55ac07ddfc8590dec657a5e1156a5ad93";
    private DownloadCharactersListener listener;
    private int offset;

    public DownloadCharacters(DownloadCharactersListener listener,int offset) {
        this.listener = listener;
        this.offset = offset;
    }

    protected MarvelResponse<CharactersDto> doInBackground(Void... urls) {
        MarvelApiConfig marvelApiConfig =
                new MarvelApiConfig.Builder(pubKey, priKey).debug().build();

        CharacterApiClient characterApiClient = new CharacterApiClient(marvelApiConfig);
        CharactersQuery spider = CharactersQuery.Builder.create().withOffset(offset).withLimit(10).build();
        try {
            MarvelResponse<CharactersDto> all = characterApiClient.getAll(spider);
            return all;
        } catch (MarvelApiException exception) {
            return null;
        }
    }

    protected void onPostExecute(MarvelResponse<CharactersDto> response) {
            listener.onDownloadCharactersLoaded(response);
    }

    public interface DownloadCharactersListener {
        public void onDownloadCharactersLoaded(MarvelResponse<CharactersDto> response);
    }
}