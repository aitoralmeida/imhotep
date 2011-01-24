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
package piramide.interaction.reasoner.wizard;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class RuleFileFilter extends FileFilter {
	
	public final static String fcl = "fcl";
	
	//Accepts directories and .fcl files
	@Override
	public boolean accept(File f) {
	    if (f.isDirectory()) {
	        return true;
	    }
	
	    String extension = RuleFileFilter.getExtension(f);
	    if (extension != "") {
	        if (extension.equals(RuleFileFilter.fcl)) {
	                return true;
	        }
			return false;
	    }
	
	    return false;
	}
	
	//Description of the filter
	@Override
	public String getDescription() {
	    return "Rule files (.fcl)";
	}
	
	public static String getExtension(File file) {
	    String extension = "";
	    String filename = file.getName();
	    int i = filename.lastIndexOf('.');
	
	    if (i > 0 &&  i < filename.length() - 1) {
	        extension = filename.substring(i+1).toLowerCase();
	    }
	    return extension;
	}

}
