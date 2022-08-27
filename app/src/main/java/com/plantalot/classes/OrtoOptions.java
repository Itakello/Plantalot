package com.plantalot.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class OrtoOptions implements Parcelable {
    protected OrtoOptions(Parcel in) {
    }

    public static final Creator<OrtoOptions> CREATOR = new Creator<OrtoOptions>() {
        @Override
        public OrtoOptions createFromParcel(Parcel in) {
            return new OrtoOptions(in);
        }

        @Override
        public OrtoOptions[] newArray(int size) {
            return new OrtoOptions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
