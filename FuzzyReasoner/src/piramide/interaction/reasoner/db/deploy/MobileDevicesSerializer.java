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
package piramide.interaction.reasoner.db.deploy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import piramide.interaction.reasoner.db.DatabaseManager;
import piramide.interaction.reasoner.db.IDatabaseManager;
import piramide.interaction.reasoner.db.MobileDevices;
import piramide.interaction.reasoner.db.MockDatabaseManager;



public class MobileDevicesSerializer {
	
	/**
	 * Generates a big "mobiledata.dump" file, to avoid the requirements of MySQL, etc. for testing.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String [] args) throws Exception {
		final IDatabaseManager manager = new DatabaseManager();
		final MobileDevices mobileDevices = manager.getResults();
		
		final File dumpFile = new File(MockDatabaseManager.MOCK_FILE_NAME);
		if(dumpFile.exists())
			dumpFile.delete();
		
		final FileOutputStream fos = new FileOutputStream(dumpFile); 
		final ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(mobileDevices);
		oos.flush();
		oos.close();
		fos.flush();
		fos.close();
	}
	
}
