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
package piramide.multimodal.client.tester;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;

public class ProfileManager {
	private static final String CAPABILITIES_FILENAME = "capacities.xml";
	
	private final TesterActivity activity;
	
	ProfileManager(TesterActivity activity){
		this.activity = activity;
	}
	
	boolean exists(){
		return Arrays.asList(activity.fileList()).contains(CAPABILITIES_FILENAME);
	}
	
    Map<String, Map<String, String>> loadProfile() throws ProfileManagementException{
    	final Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();
    	
    	final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(this.activity.openFileInput(CAPABILITIES_FILENAME));
			final NodeList nodes = doc.getDocumentElement().getChildNodes();
			for(int i = 0; i < nodes.getLength(); ++i){
				final Node node = nodes.item(i);
				if(node instanceof Element){
					
					final String categoryName = node.getNodeName();
					if(!values.containsKey(categoryName))
						values.put(categoryName, new HashMap<String, String>());
					
					final NodeList nodesInCategory = node.getChildNodes();
					for(int j = 0; j < nodesInCategory.getLength(); ++j){
						final Node nodeInCategory = nodesInCategory.item(j);
						
						if(nodeInCategory instanceof Element){
							final String key = node.getNodeName();
							
							final String value = getStringFromNode(nodeInCategory);
							values.get(categoryName).put(key, value);
							
						}
					}
				}
			}
		} catch (Exception e) {
			throw new ProfileManagementException(e);
		}
		return values;
    }
    
    // Code from http://stackoverflow.com/questions/2290945/writing-xml-on-android
    private static String getStringFromNode(Node root) throws IOException {
    	final StringBuilder result = new StringBuilder();
        if (root.getNodeType() == 3)
            result.append(root.getNodeValue());
        else {
            if (root.getNodeType() != 9) {
            	final StringBuffer attrs = new StringBuffer();
                for (int k = 0; k < root.getAttributes().getLength(); ++k) {
                    attrs.append(" ").append(
                            root.getAttributes().item(k).getNodeName()).append(
                            "=\"").append(
                            root.getAttributes().item(k).getNodeValue())
                            .append("\" ");
                }
                final String attrsStr = attrs.toString();
                result.append("<").append(root.getNodeName()).append((attrsStr.length() == 0)?"":" ")
                        .append(attrs).append(">");
            } else {
                result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            }

            final NodeList nodes = root.getChildNodes();
            for (int i = 0, j = nodes.getLength(); i < j; i++) {
            	final Node node = nodes.item(i);
                result.append(getStringFromNode(node));
            }

            if (root.getNodeType() != 9) {
                result.append("</").append(root.getNodeName()).append(">");
            }
        }
        return result.toString();
    }	
    
    void update(Map<String, Map<String, String>> values) throws ProfileManagementException{
    	try {
			final FileOutputStream fos = this.activity.openFileOutput(CAPABILITIES_FILENAME, Context.MODE_PRIVATE);
			final PrintStream psp = new PrintStream(fos);
			
			psp.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			psp.println("<capabilities>");
			
			for(String category : values.keySet()){
				psp.println("\t<" + category + ">");
				
				final Map<String, String> innerValues = values.get(category);
				for(String key : innerValues.keySet())
					psp.println("\t\t" + innerValues.get(key));
				
				psp.println("\t</" + category + ">");
			}
			
			psp.println("</capabilities>");
			psp.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			throw new ProfileManagementException(e);
		} catch (IOException e) {
			throw new ProfileManagementException(e);
		}
    }    
    
}
