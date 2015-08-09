package com.example.test.advancedskill;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by test on 8/9/2015.
 */
public class ParcelableTest implements Parcelable{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static final Creator<ParcelableTest> CREATOR = new Creator<ParcelableTest>() {
        @Override
        public ParcelableTest createFromParcel(Parcel in) {
            ParcelableTest parcelableTest = new ParcelableTest();
            parcelableTest.name = in.readString();
            parcelableTest.age = in.readInt();
            return parcelableTest;
        }

        @Override
        public ParcelableTest[] newArray(int size) {
            return new ParcelableTest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }
}
