package com.realab.Validation;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;

import java.security.InvalidParameterException;
import java.util.Map;

public class ErrorList implements Parcelable {
    private ArrayMap<String, String> errorList = new ArrayMap<>();

    public ErrorList(ArrayMap<String, String> errorList) {
        this.errorList = errorList;
    }

    public ArrayMap<String, String> getErrorList() {
        return errorList;
    }

    public String getReasonFor(String key) throws InvalidParameterException {
        if (errorList.containsKey(key)) {
            return errorList.get(key);
        }
        throw new InvalidParameterException(key + "dosn't exist");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.errorList.size());
        for (Map.Entry<String, String> entry : this.errorList.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    protected ErrorList(Parcel in) {
        int errorListSize = in.readInt();
        this.errorList = new ArrayMap<>(errorListSize);
        for (int i = 0; i < errorListSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.errorList.put(key, value);
        }
    }

    public static final Creator<ErrorList> CREATOR = new Creator<ErrorList>() {
        @Override
        public ErrorList createFromParcel(Parcel source) {
            return new ErrorList(source);
        }

        @Override
        public ErrorList[] newArray(int size) {
            return new ErrorList[size];
        }
    };
}
