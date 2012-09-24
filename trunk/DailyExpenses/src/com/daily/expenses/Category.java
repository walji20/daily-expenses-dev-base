package com.daily.expenses;

/**
 * @author Christian Zöller, Perry Wolf
 *
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Category {
	private int id;
	private int parentId;
	private String title;
	private String description;
	private String resourceIcon;
	
	protected Category(Parcel in) {
	    id = in.readInt();
	    parentId = in.readInt();
	    title = in.readString();
	    description = in.readString();
	    resourceIcon = in.readString();
	}

	public int describeContents() {
	    return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
	    dest.writeInt(id);
	    dest.writeInt(parentId);
	    dest.writeString(title);
	    dest.writeString(description);
	    dest.writeString(resourceIcon);
	}

	public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
	    public Category createFromParcel(Parcel in) {
	        return new Category(in);
	    }

	    public Category[] newArray(int size) {
	        return new Category[size];
	    }
	};
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the parentId
	 */
	public int getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
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
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the resourceIcon
	 */
	public String getResourceIcon() {
		return resourceIcon;
	}
	/**
	 * @param resourceIcon the resourceIcon to set
	 */
	public void setResourceIcon(String resourceIcon) {
		this.resourceIcon = resourceIcon;
	}
}
