package com.example.richard.bookrian;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by richard on 5/5/17.
 */

public class Library implements Parcelable {

    /**
    public static final String TABLE_NAME = "libraries";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "lib_name";
    public static final String COLUMN_LOCATION = "lib_location";

    // Table create statement
    public static final String CREATE_STATEMENT = "CREATE TABLE "
            + TABLE_NAME + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_LOCATION + " TEXT NOT NULL" +
            ")";
    **/


    private long _id;
    private String lib_name ;
    private String lib_location;
    private String lib_phone;
    private String lib_add1;
    private String lib_add2;
    private String lib_add3;




    public Library(long id, String lib_name, String lib_location, String lib_phone, String lib_add1, String lib_add2, String lib_add3) {
        this._id = id;
        this.lib_name = lib_name ;
        this.lib_location = lib_location;
        this.lib_phone = lib_phone;
        this.lib_add1 = lib_add1;
        this.lib_add2 = lib_add2;
        this.lib_add3 = lib_add3;

    }

    protected Library(Parcel in) {
        _id = in.readLong();
        lib_name = in.readString();
        lib_location = in.readString();
        lib_phone = in.readString();
        lib_add1 = in.readString();
        lib_add2 = in.readString();
        lib_add3 = in.readString();
    }


    public static final Creator<Library> CREATOR = new Creator<Library>() {
        @Override
        public Library createFromParcel(Parcel in) {
            return new Library(in);
        }

        @Override
        public Library[] newArray(int size) {
            return new Library[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeString(lib_name);
        parcel.writeString(lib_location);
        parcel.writeString(lib_phone);
        parcel.writeString(lib_add1);
        parcel.writeString(lib_add2);
        parcel.writeString(lib_add3);

    }

    public long get_id() { return _id; }

    public void set_id(long _id) { this._id = _id; }

    public String getM_sName() {
        return lib_name;
    }

    public void setM_sName(String m_sName) {
        this.lib_name = m_sName;
    }

    public String getM_sLocation() {
        return lib_location;
    }

    public void setM_sLocation(String m_sCountry) {
        this.lib_location = m_sCountry;
    }

    public String getLib_phone() {
        return lib_phone;
    }

    public void setLib_phone(String lib_phone) {
        this.lib_phone = lib_phone;
    }

    public String getLib_add1() {
        return lib_add1;
    }

    public void setLib_add1(String lib_add1) {
        this.lib_add1 = lib_add1;
    }

    public String getLib_add2() {
        return lib_add2;
    }

    public void setLib_add2(String lib_add2) {
        this.lib_add2 = lib_add2;
    }

    public String getLib_add3() {
        return lib_add3;
    }

    public void setLib_add3(String lib_add3) {
        this.lib_add3 = lib_add3;
    }


}
