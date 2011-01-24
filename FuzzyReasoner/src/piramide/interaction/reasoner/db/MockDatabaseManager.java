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
package piramide.interaction.reasoner.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MockDatabaseManager extends DatabaseManager {

	public static final String MOCK_FILE_NAME = "mobiledata.dump";
	
	public MockDatabaseManager() throws DatabaseException {
	}
	
	@Override
	public MobileDevices getResults(int size) throws DatabaseException {
		try{
			final File dumpFile = new File(MockDatabaseManager.MOCK_FILE_NAME);
			final FileInputStream fis = new FileInputStream(dumpFile);
			final ObjectInputStream ois = new ObjectInputStream(fis);
			return (MobileDevices)ois.readObject();
		}catch(IOException e){
			throw new DatabaseException("Couldn't load results: " + e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new DatabaseException("Couldn't load results: " + e.getMessage(), e);
		}
	}

}
