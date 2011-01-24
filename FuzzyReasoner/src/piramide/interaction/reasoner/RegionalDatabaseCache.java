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
package piramide.interaction.reasoner;

import piramide.interaction.reasoner.db.DatabaseException;
import piramide.interaction.reasoner.db.IDatabaseManager;
import piramide.interaction.reasoner.db.MobileDevices;

class RegionalDatabaseCache{
	
	private volatile MobileDevices data;
	private volatile long creationStamp;
	private final IDatabaseManager dbManager;
	private final String region;
	
	private final long maxTime;
	
	RegionalDatabaseCache(String region, IDatabaseManager dbManager, long maxTime){
		this.region = region;
		this.maxTime = maxTime;
		this.dbManager = dbManager;
	}
	
	MobileDevices retrieve() throws DatabaseException{
		synchronized(this){
			final long currentTime = System.currentTimeMillis(); 
			if((currentTime - this.creationStamp) < this.maxTime)
				return this.data;
			this.creationStamp = System.currentTimeMillis();
		}
		this.data = this.dbManager.getResults(new Geolocation(this.region));
		this.creationStamp = System.currentTimeMillis();
		return this.data;
	}
}
