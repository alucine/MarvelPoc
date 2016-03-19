package com.alucine.zinio.marvelpoc.object;

import android.os.Parcel;
import android.os.Parcelable;

public class CharacterInfo implements Parcelable {
    public String imageUrl;
    public String title;
    public int comicId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeInt(this.comicId);
    }

    public CharacterInfo() {
    }

    protected CharacterInfo(Parcel in) {
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.comicId = in.readInt();
    }

    public static final Creator<CharacterInfo> CREATOR = new Creator<CharacterInfo>() {
        public CharacterInfo createFromParcel(Parcel source) {
            return new CharacterInfo(source);
        }

        public CharacterInfo[] newArray(int size) {
            return new CharacterInfo[size];
        }
    };
}
