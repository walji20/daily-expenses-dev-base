package com.daily.expenses;

/**
 * @author Christian Zöller, Perry Wolf
 *
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Record {
	private int id;
	private String title;
	private String description;
	private double amount;
	/* income or expense */
	private int bookingType;
	/* period booking or single record */
	private int periodType;
	/* according category */
	private int categoryType;
	/* date of creation in unix time format */
	private long unixDate;
	/* state of the payment */
	private boolean payState;

	protected Record(Parcel in) {
		id = in.readInt();
		title = in.readString();
		description = in.readString();
		amount = in.readDouble();
		bookingType = in.readInt();
		periodType = in.readInt();
		categoryType = in.readInt();
		unixDate = in.readLong();
		payState = in.readByte() != 0x00;
	}

	/**
	 * 
	 */
	public Record() {
		// TODO Auto-generated constructor stub
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeDouble(amount);
		dest.writeInt(bookingType);
		dest.writeInt(periodType);
		dest.writeInt(categoryType);
		dest.writeLong(unixDate);
		dest.writeByte((byte) (payState ? 0x01 : 0x00));
	}

	public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
		public Record createFromParcel(Parcel in) {
			return new Record(in);
		}

		public Record[] newArray(int size) {
			return new Record[size];
		}
	};

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the bookingType
	 */
	public int getBookingType() {
		return bookingType;
	}

	/**
	 * @param bookingType
	 *            the bookingType to set
	 */
	public void setBookingType(int bookingType) {
		this.bookingType = bookingType;
	}

	/**
	 * @return the periodType
	 */
	public int getPeriodType() {
		return periodType;
	}

	/**
	 * @param periodType
	 *            the periodType to set
	 */
	public void setPeriodType(int periodType) {
		this.periodType = periodType;
	}

	/**
	 * @return the categoryType
	 */
	public int getCategoryType() {
		return categoryType;
	}

	/**
	 * @param categoryType
	 *            the categoryType to set
	 */
	public void setCategoryType(int categoryType) {
		this.categoryType = categoryType;
	}

	/**
	 * @return the unixDate
	 */
	public long getUnixDate() {
		return unixDate;
	}

	/**
	 * @param unixDate
	 *            the unixDate to set
	 */
	public void setUnixDate(long unixDate) {
		this.unixDate = unixDate;
	}

	/**
	 * @return the payState
	 */
	public boolean isPayState() {
		return payState;
	}

	/**
	 * @param payState
	 *            the payState to set
	 */
	public void setPayState(boolean payState) {
		this.payState = payState;
	}
}
