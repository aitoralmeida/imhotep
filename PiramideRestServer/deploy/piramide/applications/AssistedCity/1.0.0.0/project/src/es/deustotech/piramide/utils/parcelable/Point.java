/*
 * Copyright (C) 2010 PIRAmIDE-SP3 authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Eduardo Castillejo <eduardo.castillejo@deusto.es>
 *
 */

package es.deustotech.piramide.utils.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents each result point returned by the
 * Google Maps API callback 
 *
 */
public class Point implements Parcelable {
	
	private String 			title;
	private String 			streetAddress;
	private String 			latitude;
	private String 			longitude;
	/*
	private PhoneNumbers	phoneNumbers;
	private String 			city;
	private String 			region;
	private String 			titleNoFormatting;
	private String	 		addressLines;
	private String 			country;
	private String			toHere;
	private String			fromHere;
	*/
	public Point() {
		super();
//		this.phoneNumbers = new PhoneNumbers();
	}

	public Point(String title, String streetAddress, String latitude,
			String longitude/*PhoneNumbers phoneNumbers, String city, String title,
			String region, String titleNoFormatting, String streetAddress,
			String addressLines, String country, String latitude,
			String longitude, String toHere, String fromHere*/) {
		super();
		this.title 				= title;
		this.streetAddress 		= streetAddress;
		this.latitude 			= latitude;
		this.longitude 			= longitude;
		/*
		this.phoneNumbers 		= phoneNumbers;
		this.city 				= city;
		this.region 			= region;
		this.titleNoFormatting 	= titleNoFormatting;
		this.addressLines 		= addressLines;
		this.country 			= country;
		this.fromHere 			= fromHere;
		this.toHere 			= toHere;
		*/
	}

	public Point(Parcel in) {
		super();
		readFromParcel(in);
	}

	private void readFromParcel(Parcel in) {
		setStreetAddress(in.readString());
		setTitle(in.readString());
		setLatitude(in.readString());
		setLongitude(in.readString());
	}
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getStreetAddress());
		dest.writeString(this.getTitle());
		dest.writeString(this.getLatitude());
		dest.writeString(this.getLongitude());
	}
	
	public static final Parcelable.Creator<Point> CREATOR = new 
	Parcelable.Creator<Point>() {
	
    public Point createFromParcel(Parcel in) {
        return new Point(in);
    }

    public Point[] newArray(int size) {
        return new Point[size];
    }
};
/*
	public PhoneNumbers getPhoneNumbers() {
		return this.phoneNumbers;
	}

	public void setPhoneNumbers(PhoneNumbers phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
*/
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
/*
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getTitleNoFormatting() {
		return titleNoFormatting;
	}

	public void setTitleNoFormatting(String titleNoFormatting) {
		this.titleNoFormatting = titleNoFormatting;
	}
*/
	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
/*
	public String getAddressLines() {
		return addressLines;
	}

	public void setAddressLines(String addressLines) {
		this.addressLines = addressLines;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
*/
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/*
	public String getToHere() {
		return toHere;
	}

	public void setToHere(String toHere) {
		this.toHere = toHere;
	}

	public String getFromHere() {
		return fromHere;
	}

	public void setFromHere(String fromHere) {
		this.fromHere = fromHere;
	}

	public void setPhoneNumber(String phone) {
		this.phoneNumbers.setPhone(phone);
	}
	*/
}