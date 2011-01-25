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
 * Author: Aitor Almeida <aitor.almeida@deusto.es>
 *         Pablo Orduña <pablo.orduna@deusto.es>
 *         Eduardo Castillejo <eduardo.castillejo@deusto.es>
 *
 */
package piramide.multimodal.client.tester.capabilityTests;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class TestConfig implements Parcelable {
	
	private HashMap<String, String> configuration;
	 
    public TestConfig() {
    	this.configuration = new HashMap<String, String>();
    }
 
    public TestConfig(Parcel in) {
    	this.configuration = new HashMap<String, String>();
        readFromParcel(in);
    }
 
    public static final Parcelable.Creator<TestConfig> CREATOR = new Parcelable.Creator<TestConfig> () {
        public TestConfig createFromParcel(Parcel in) {
            return new TestConfig(in);
        }
 
        public TestConfig[] newArray(int size) {
            return new TestConfig[size];
        }
    };
 
    @Override
    public int describeContents() {
        return 0;
    }
 
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.configuration.size());
        for (String s: this.configuration.keySet()) {
            dest.writeString(s);
            dest.writeString(this.configuration.get(s));
        }
    }
 
    public void readFromParcel(Parcel in) {
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
        	this.configuration.put(in.readString(), in.readString());
        }
    }
 
    public String get(String key) {
        return this.configuration.get(key);
    }
 
    public void put(String key, String value) {
    	this.configuration.put(key, value);
    }


}
