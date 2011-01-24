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
package piramide.multimodal.downloader.client;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class Profiles {
	private final Map<String, Map<String, Object>> profiles = new HashMap<String, Map<String,Object>>();
	
	public Profiles(Context context){
		final String regular = context.getString(R.string.regular_user);
		final Map<String, Object> regularProfile = generateRegularProfile();
		this.profiles.put(regular, regularProfile);
		
		final String blind = context.getString(R.string.blind_user);
		final Map<String, Object> blindProfile = generateBlindProfile();
		this.profiles.put(blind, blindProfile);
	}
	
	public Map<String, Object> getProfile(String profile){
		return this.profiles.get(profile);
	}

	private Map<String, Object> generateRegularProfile() {
		final Map<String, Object> profile = new HashMap<String, Object>();
		profile.put("piramide.user.name", "Mr. Regular");
		
		// piramide.devices
		profile.put("piramide.devices.screen.height",      Integer.valueOf(800));
		profile.put("piramide.devices.screen.width",       Integer.valueOf(480));
		profile.put("piramide.devices.modelname",          "HTC Desire");
		profile.put("piramide.devices.os",                 "Android");
		profile.put("piramide.devices.os.version",         "2.2");
		profile.put("piramide.devices.capabilities.video", Boolean.TRUE);
		profile.put("piramide.devices.capabilities.audio", Boolean.TRUE);
		profile.put("piramide.devices.capabilities.gps",   Boolean.TRUE);
		profile.put("piramide.devices.capabilities.flash", Boolean.TRUE);
		
		// piramide.users
		profile.put("piramide.user.capabilities.problems",                Boolean.TRUE);
		profile.put("piramide.user.capabilities.problems.sight",          Boolean.TRUE);
		profile.put("piramide.user.capabilities.problems.sight.diopters", Integer.valueOf(0));
		profile.put("piramide.user.capabilities.problems.hearing",        Boolean.FALSE);
		profile.put("piramide.user.capabilities.problems.smell",          Boolean.FALSE);
		profile.put("piramide.user.capabilities.problems.touch",          Boolean.FALSE);
		profile.put("piramide.user.capabilities.problems.problems",       Boolean.FALSE);
		
		return profile;
	}
	
	private Map<String, Object> generateBlindProfile() {
		final Map<String, Object> profile = new HashMap<String, Object>();
		profile.put("piramide.user.name", "Mr. Blind");
		
		// piramide.devices
		profile.put("piramide.devices.screen.height",      Integer.valueOf(800));
		profile.put("piramide.devices.screen.width",       Integer.valueOf(480));
		profile.put("piramide.devices.modelname",          "HTC Desire");
		profile.put("piramide.devices.os",                 "Android");
		profile.put("piramide.devices.os.version",         "2.2");
		profile.put("piramide.devices.capabilities.video", Boolean.TRUE);
		profile.put("piramide.devices.capabilities.audio", Boolean.TRUE);
		profile.put("piramide.devices.capabilities.gps",   Boolean.TRUE);
		profile.put("piramide.devices.capabilities.flash", Boolean.TRUE);
		
		// piramide.users
		profile.put("piramide.user.capabilities.problems",                Boolean.TRUE);
		profile.put("piramide.user.capabilities.problems.sight",          Boolean.TRUE);
		profile.put("piramide.user.capabilities.problems.sight.diopters", Integer.valueOf(20));
		profile.put("piramide.user.capabilities.problems.hearing",        Boolean.FALSE);
		profile.put("piramide.user.capabilities.problems.smell",          Boolean.FALSE);
		profile.put("piramide.user.capabilities.problems.touch",          Boolean.FALSE);
		profile.put("piramide.user.capabilities.problems.problems",       Boolean.FALSE);
		
		return profile;
	}
}
