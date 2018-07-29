package com.example.richard.bookrian;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by richard on 5/5/17.
 */

public class Book implements Parcelable {

    private long _id;
    private String m_sName ;
    private String m_sAuthor;
    private String m_sAvailable;
    private String m_sLoanDate;
    private int m_sQuantity;
    private String m_sLibrary;





    // Created constructor with no Arguments
    public Book() {
        m_sName = "NA";
        m_sAuthor = "NA";
        m_sAvailable = "NA";
        m_sQuantity = 0;
        m_sLoanDate = "NA" ;
        m_sLibrary = "NA";
    }


    //Getters and Setters
    public void setM_sLibrary(String m_sLibrary) {
        this.m_sLibrary = m_sLibrary;
    }
    public String getM_sLibrary() {
        return m_sLibrary;
    }

    public String getM_sLoanDate() {
        return m_sLoanDate;
    }

    public void setM_sLoanDate(String m_sLoanDate) {
        this.m_sLoanDate = m_sLoanDate;
    }

    public int getM_sQuantity() {
        return m_sQuantity;
    }

    public void setM_sQuantity(int m_sQuantity) {
        this.m_sQuantity = m_sQuantity;
    }

    public String getM_sName() {
        return m_sName;
    }

    public void setM_sName(String m_sName) {
        this.m_sName = m_sName;
    }

    public String getM_sAuthor() {
        return m_sAuthor;
    }

    public void setM_sAuthor(String m_sAuthor) {
        this.m_sAuthor = m_sAuthor;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getM_sAvailable() {
        return m_sAvailable;
    }

    public void setM_sAvailable(String m_sAvailable) {
        this.m_sAvailable = m_sAvailable;
    }

    public Book(long id, String m_sName, String m_sAuthor, String m_sAvailable, int m_sQuantity, String m_sLoanDate, String m_sLibrary) {
        this._id = id;
        this.m_sName = m_sName ;
        this.m_sAuthor = m_sAuthor;
        this.m_sAvailable = m_sAvailable;
        this.m_sQuantity = m_sQuantity;
        this.m_sLoanDate = m_sLoanDate;
        this.m_sLibrary = m_sLibrary;

    }

    protected Book(Parcel in) {
        _id = in.readLong();
        m_sName = in.readString();
        m_sAuthor = in.readString();
        m_sAvailable = in.readString();
        m_sQuantity = in.readInt();
        m_sLoanDate = in.readString();
        m_sLibrary = in.readString();
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
        parcel.writeString(m_sAuthor);
        parcel.writeString(m_sAvailable);
        parcel.writeInt(m_sQuantity);
        parcel.writeString(m_sLoanDate);
        parcel.writeString(m_sLibrary);
    }

}
