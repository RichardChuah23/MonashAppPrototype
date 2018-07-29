package com.example.richard.bookrian;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by richard on 5/5/17.
 */

public class User implements Parcelable {


    private long _id;
    private String m_sName ;
    private String m_sEmail;
    private int m_sloanQtyAllow;

    // Created constructor with no Arguments
    public User() {
        m_sName = "NA";
        m_sEmail = "NA";
        m_sloanQtyAllow = 0 ;

    }


    public User(long id, String m_sName, String m_sEmail, int m_sloanQtyAllow) {
        this._id = id;
        this.m_sName = m_sName;
        this.m_sEmail = m_sEmail ;
        this.m_sloanQtyAllow = m_sloanQtyAllow;


    }
    public int getM_sloanQtyAllow() {
        return m_sloanQtyAllow;
    }

    public void setM_sloanQtyAllow(int m_sloanQtyAllow) {
        this.m_sloanQtyAllow = m_sloanQtyAllow;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getM_sName() {
        return m_sName;
    }

    public void setM_sName(String m_sName) {
        this.m_sName = m_sName;
    }

    public String getM_sEmail() {
        return m_sEmail;
    }

    public void setM_sEmail(String m_sEmail) {
        this.m_sEmail = m_sEmail;
    }

    protected User(Parcel in) {
        _id = in.readLong();
        m_sName = in.readString();
        m_sEmail = in.readString();
        m_sloanQtyAllow = in.readInt();


    }


    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeString(m_sName);
        parcel.writeString(m_sEmail);
        parcel.writeInt(m_sloanQtyAllow);

    }

}
