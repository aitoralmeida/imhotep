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

public class EncapsulatePoints implements Parcelable {

	private String[] streetAddress;
	private String[] title;
	private String[] latitude;
	private String[] longitude;
	
	public static final Parcelable.Creator<EncapsulatePoints> CREATOR = 
		new Parcelable.Creator<EncapsulatePoints>() {

		public EncapsulatePoints createFromParcel(Parcel source) {
			return new EncapsulatePoints(source);
		}

		public EncapsulatePoints[] newArray(int size) {
			return new EncapsulatePoints[size];
		}
	};

	public EncapsulatePoints() { 
		streetAddress 	= new String[8];
		title 			= new String[8];
		latitude		= new String[8];
		longitude		= new String[8];
	}

	public EncapsulatePoints(Parcel in) {
		super();
		this.streetAddress 	= readStreetFromParcel(in);
		this.title 			= readTitleFromParcel(in);
		this.latitude		= readLatitudeFromParcel(in);
		this.longitude		= readLongitudeFromParcel(in);
	}

	public void writeToParcel(Parcel in, int flags) {
		for (int i=0; i<8; i++){
			in.writeString(this.title[i]);
			in.writeString(this.streetAddress[i]);
			in.writeString(this.latitude[i]);
			in.writeString(this.longitude[i]);
		}
	}
	
	private String[] readTitleFromParcel(Parcel in) {
		String[] streetArray = new String[8];
		
		for (int i=0; i<this.streetAddress.length; i++){
//			streetArray[i] = this.streetAddress[i];
			streetArray[i] = in.readString();
		}
		
		return streetArray;
	}

	private String[] readStreetFromParcel(Parcel in) {
		String[] titleArray = new String[8];
		
		for (int i=0; i<this.title.length; i++){
//			titleArray[i] = this.title[i];
			titleArray[i] = in.readString();
		}
		
		return titleArray;
	}
	
	private String[] readLatitudeFromParcel(Parcel in) {
		String[] latitudeArray = new String[8];
		
		for (int i=0; i<this.latitude.length; i++){
			latitudeArray[i] = in.readString();
		}
		
		return latitudeArray;
	}
	
	private String[] readLongitudeFromParcel(Parcel in) {
		String[] longitudeArray = new String[8];
		
		for (int i=0; i<this.longitude.length; i++){
//			titleArray[i] = this.title[i];
			longitudeArray[i] = in.readString();
		}
		
		return longitudeArray;
	}

	public int describeContents() {
		return 0;
	}

	public void setPoints(String[] streetAddress, String[] title, String[] latitude,
			String[] longitude) {
		this.streetAddress 	= streetAddress;
		this.title 			= title;
		this.latitude		= latitude;
		this.longitude		= longitude;
	}
	
	/*
	public void writeToParcel(Parcel in, int flags) {
		//Add the vector size to read it later in the readFromParcel method
		//and use it in the loop
		in.writeInt(vector.size());
		
		for (Point point: vector) {
			in.writeString(point.getPhoneNumbers().getPhone());
			in.writeString(point.getCity());
			in.writeString(point.getTitle());
			in.writeString(point.getRegion());
			in.writeString(point.getTitleNoFormatting());
			in.writeString(point.getStreetAddress());
			in.writeString(point.getAddressLines());
			in.writeString(point.getCountry());
			in.writeString(point.getLatitude());
			in.writeString(point.getLongitude());
			in.writeString(point.getToHere());
			in.writeString(point.getFromHere());
		}
	}
*/
	/*
	public Vector<Point> readFromParcel(Parcel in) {
		Vector<Point> resultVector = new Vector<Point>();
		
		Point pointToAdd = null;
		final int vectorSize = in.readInt();
		
		for (int i=0; i<vectorSize; i++) {
			pointToAdd = new Point();
			
			pointToAdd.setPhoneNumber(in.readString());
			pointToAdd.setCity(in.readString());
			pointToAdd.setTitle(in.readString());
			pointToAdd.setRegion(in.readString());
			pointToAdd.setTitleNoFormatting(in.readString());
			pointToAdd.setStreetAddress(in.readString());
			pointToAdd.setAddressLines(in.readString());
			pointToAdd.setCountry(in.readString());
			pointToAdd.setLatitude(in.readString());
			pointToAdd.setLongitude(in.readString());
			pointToAdd.setToHere(in.readString());
			pointToAdd.setFromHere(in.readString());
			
			resultVector.add(pointToAdd);
		}
		return resultVector;
	}
	*/
}