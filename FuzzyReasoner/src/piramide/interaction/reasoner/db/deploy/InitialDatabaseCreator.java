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
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import piramide.interaction.reasoner.db.DatabaseManager;

/**
 * This class will take the WURFL XML file and generate a database with
 * some device-related fields filled, and an empty field related to the
 * trend of this file. This trend will be filled by another script.
 * 
 * Once the database is filled, DatabaseMigrator will populate the rest
 * of the tables.
 */
public class InitialDatabaseCreator {
	
	private final static String [] REGIONS = {"all", "jp", "es"};
	
	public static void main(String [] args) throws Exception {
		if(args.length != 1){
			System.err.println("Usage: creator wurfl-path dbuser dbpassword");
			System.exit(-1);
		}
		
		Class.forName(com.mysql.jdbc.Driver.class.getName());
		
		final String wurflPath = args[0];
		
		final File file = new File(wurflPath);
		final Map<String, WurlfInfo> devicesInformation = extractInformationFromWufl(file);
		
		final Connection connection = DriverManager.getConnection(DatabaseManager.CONNECTION_URL, DatabaseManager.USERNAME, DatabaseManager.PASSWORD);
		connection.setAutoCommit(false);
		
        final String sentence = "INSERT INTO Downloaded(device_name, wurfl_id, marketing_name, brand_name, model_name, real_height, real_width, reso_height, reso_width, region, value) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL) ";
        
        final PreparedStatement statement = connection.prepareStatement(sentence);
        
        int counter = 0;
        
		for(String deviceName : devicesInformation.keySet())
			for(String region : REGIONS){
				final WurlfInfo info = devicesInformation.get(deviceName);
		        
				statement.setString(1, deviceName);
				statement.setString(2, info.getId());
				statement.setString(3, info.getMarketingName());
				statement.setString(4, info.getBrandName());
				statement.setString(5, info.getModelName());
				
				fill(statement, 6, info.getRealHeight());
				fill(statement, 7, info.getRealWidth());
				fill(statement, 8, info.getResoHeight());
				fill(statement, 9, info.getResoWidth());
				
				statement.setString(10, region);
				
				statement.execute();
				
				counter++;
				if(counter % 100 == 0)
					connection.commit();
			}
		
		connection.commit();
		
		System.err.println(devicesInformation.size());
	}
	
	private static void fill(PreparedStatement statement, int position, String info) throws SQLException{
		if(info == null)
			statement.setNull(position, Types.FLOAT);
		else 
			statement.setFloat(position, Float.parseFloat(info));

	}
	
	private static Map<String, WurlfInfo> extractInformationFromWufl(
			final File file) throws ParserConfigurationException, SAXException,
			IOException {
		final Map<String, WurlfInfo> todos = new HashMap<String, WurlfInfo>();
		int unknownBrands = 0;
		
		final NodeList devicesList = retrieveDevices(file);
        
		for(int deviceNumber = 0; deviceNumber < devicesList.getLength(); ++deviceNumber){
			final Element device = (Element)devicesList.item(deviceNumber);
			
			final NodeList groupList = device.getElementsByTagName("group");
			
			Element productInfo = null;
			Element display = null;
			
			for(int groupNumber = 0; groupNumber < groupList.getLength(); ++groupNumber){
				final Element group = (Element)groupList.item(groupNumber);
				if(group.getAttribute("id").equals("product_info"))
					productInfo = group;
				else if(group.getAttribute("id").equals("display"))
					display = group;
			}
			
			if(productInfo == null)
				continue;
			
			if(display == null)
				continue;
			
			String modelName = null;
			String brandName = null;
			String marketingName = null;
			
			String resoWidth = null;
			String resoHeight = null;
			
			String realWidth = null;
			String realHeight = null;
			
			final NodeList productInfoCapabilityList = productInfo.getElementsByTagName("capability");
			
			for(int capabilityNumber = 0; capabilityNumber < productInfoCapabilityList.getLength(); ++capabilityNumber){
				final Element capability = (Element)productInfoCapabilityList.item(capabilityNumber);
				if(capability.getAttribute("name").equals("model_name"))
					modelName = capability.getAttribute("value");
				else if(capability.getAttribute("name").equals("brand_name"))
					brandName = capability.getAttribute("value");
				else if(capability.getAttribute("name").equals("marketing_name"))
					marketingName = capability.getAttribute("value");
			}
			
			final NodeList displayCapabilityList = display.getElementsByTagName("capability");
			
			for(int capabilityNumber = 0; capabilityNumber < displayCapabilityList.getLength(); ++capabilityNumber){
				final Element capability = (Element)displayCapabilityList.item(capabilityNumber);
				
				if(capability.getAttribute("name").equals("resolution_width"))
					resoWidth = capability.getAttribute("value");
				else if(capability.getAttribute("name").equals("resolution_height"))
					resoHeight = capability.getAttribute("value");
				else if(capability.getAttribute("name").equals("physical_screen_height"))
					realHeight = capability.getAttribute("value");
				else if(capability.getAttribute("name").equals("physical_screen_width"))
					realWidth = capability.getAttribute("value");
			}

			
			if(modelName == null && marketingName == null)
				continue;
			
			if(brandName == null){
				final String userAgent = device.getAttribute("user_agent");
				final String nameToFind = (modelName != null)?modelName:marketingName;
				int position = userAgent.indexOf(nameToFind);
				
				if(position == -1)
					position = userAgent.length();
				
				brandName = userAgent.substring(0, position);
				
	            if(brandName.indexOf("MOT-") >= 0)
	                brandName = "MOTOROLA";
	            else if(brandName.toLowerCase().indexOf("nokia") >= 0)
	                brandName = "Nokia";
	            else if(brandName.toLowerCase().indexOf("sonyericsson") >= 0)
	                brandName = "SonyEricsson";
	            else if(brandName.indexOf("LG") >= 0)
	                brandName = "LG";
	            else if(brandName.toUpperCase().indexOf("SAMSUNG") >= 0)
	                brandName = "SAMSUNG";
	            else if(brandName.indexOf("HTC") >= 0)
	                brandName = "HTC";
	            else if(userAgent.indexOf("LG") == 0)
	                brandName = "LG";
	            else if(brandName.indexOf("i-mobile") == 0)
	                brandName = "i-mobile";
	            else if(brandName.indexOf("SAGEM") == 0)
	                brandName = "SAGEM";
	            else if(userAgent.indexOf("Mac OS X") >= 0)
	                brandName = "Apple";
	            else if(brandName.indexOf("SIE-") == 0)
	                brandName = "Siemens";
	            else if(brandName.indexOf("SEC") == 0)
	                brandName = "SAMSUNG";
	            else if(userAgent.indexOf("BlackBerry") == 0)
	                brandName = "Blackberry";
	            else if(modelName != null && modelName.equals("802SE"))
	                brandName = "Sony Ericsson";
	            else if(modelName != null && modelName.equals("705SH"))
	                brandName = "Sharp";
	            else if(device.getAttribute("id").indexOf("htc") == 0)
	                brandName = "HTC";
	            else if(device.getAttribute("id").indexOf("blackberry") == 0)
	                brandName = "Blackberry";
	            else if(device.getAttribute("id").indexOf("hp") == 0)
	                brandName = "HP";
	            else if(device.getAttribute("id").indexOf("o2") == 0)
	                brandName = "O2";
	            else if(device.getAttribute("id").indexOf("mot_") == 0)
	                brandName = "Motorola";
	            else if(device.getAttribute("id").indexOf("sharp_") == 0)
	                brandName = "Sharp";
	            else if(device.getAttribute("id").indexOf("nec_") == 0)
	                brandName = "NEC";
	            else if(device.getAttribute("id").indexOf("vkmobile_") == 0)
	                brandName = "VK Mobile";
	            else if(device.getAttribute("id").indexOf("portalmmm_") == 0)
	                brandName = "NEC";
	            else if(device.getAttribute("id").indexOf("docomo_") == 0)
	                brandName = "DoCoMo";
	            else if(device.getAttribute("id").indexOf("softbank_") == 0)
	                brandName = "SoftBank";
	            else if(device.getAttribute("id").indexOf("kddi_") == 0)
	                brandName = "KDDI";
	            else if(device.getAttribute("id").indexOf("lenovo_") == 0)
	                brandName = "Lenovo";
	            else if(device.getAttribute("id").indexOf("samsung_") == 0)
	                brandName = "Samsung";
	            else if(device.getAttribute("id").indexOf("ericsson") == 0)
	                brandName = "Sony Ericsson";
	            else if(device.getAttribute("id").indexOf("phillips_") == 0)
	                brandName = "Phillips";
	            else if(device.getAttribute("id").indexOf("alcatel_") == 0)
	                brandName = "Alcatel";
	            else if(device.getAttribute("id").indexOf("gradiente_") == 0)
	                brandName = "Gradiente";
	            else if(device.getAttribute("id").indexOf("rim950_") == 0)
	                brandName = "RIM";
	            else if(device.getAttribute("id").indexOf("rim_") == 0)
	                brandName = "RIM";
	            else if(device.getAttribute("id").indexOf("rim957_") == 0)
	                brandName = "RIM";
	            else if(device.getAttribute("id").indexOf("skyzen_") == 0)
	                brandName = "Skyzen";
	            else if(device.getAttribute("id").indexOf("tsm_5mt_") == 0)
	                brandName = "Vitelcom";
	            else if(device.getAttribute("id").indexOf("telit_") == 0)
	                brandName = "Telit";
	            else if(device.getAttribute("id").indexOf("savajeos_") == 0)
	                brandName = "SavaJe";
	            else if(device.getAttribute("id").indexOf("telme_") == 0)
	                brandName = "Tel.Me.";
	            else if(device.getAttribute("id").indexOf("huawei_") == 0)
	                brandName = "Huawei";
	            else if(device.getAttribute("id").indexOf("sagem_") == 0)
	                brandName = "Sagem";
	            else if(device.getAttribute("id").indexOf("pt_") == 0)
	                brandName = "Pantech";
	            else if(device.getAttribute("id").indexOf("pg_") == 0)
	                brandName = "Pantech";
	            else if(device.getAttribute("id").indexOf("kgt_") == 0)
	                brandName = "DoCoMo";
	            else if(device.getAttribute("id").indexOf("pantech_") == 0)
	                brandName = "Pantech";
	            else if(device.getAttribute("id").indexOf("panasonic_") == 0)
	                brandName = "Panasonic";
	            else if(device.getAttribute("id").indexOf("palm_") == 0)
	                brandName = "Palm";
	            else if(device.getAttribute("id").indexOf("kwc_") == 0)
	                brandName = "Kyocera";
	            else if(device.getAttribute("id").indexOf("sprint_") == 0)
	                brandName = "Sprint";
	            else if(device.getAttribute("id").indexOf("zte_") == 0)
	                brandName = "ZTE";
	            else if(device.getAttribute("id").indexOf("amc_") == 0)
	                brandName = "ZTE";
	            else if(device.getAttribute("id").indexOf("beeline_") == 0)
	                brandName = "Beeline";
	            else if(device.getAttribute("id").indexOf("motorola_") == 0)
	                brandName = "Motorola";
	            else if(device.getAttribute("id").indexOf("qtek_") == 0)
	                brandName = "Qtek";
	            else if(device.getAttribute("id").indexOf("mitsu_") == 0)
	                brandName = "Mitsubishi";
	            else if(device.getAttribute("id").indexOf("vertu_") == 0)
	                brandName = "Vertu";
	            else if(device.getAttribute("id").indexOf("onda_") == 0)
	                brandName = "Onda";
	            else if(device.getAttribute("id").indexOf("inq_") == 0)
	                brandName = "InQ";
	            else if(device.getAttribute("id").indexOf("sony_") == 0)
	                brandName = "Sony Ericsson";
	            else if(device.getAttribute("id").indexOf("nokia_") == 0)
	                brandName = "Nokia";
	            else if(device.getAttribute("id").indexOf("philips") == 0)
	                brandName = "philips";
	            else if(device.getAttribute("id").indexOf("lg_") == 0)
	                brandName = "lg";
	            else if(device.getAttribute("id").indexOf("tsm_") == 0)
	                brandName = "tsm";
	            else if(device.getAttribute("id").indexOf("audiovox_") == 0)
	                brandName = "Audiovox";
	            else if(device.getAttribute("id").indexOf("sendop600_") == 0)
	                brandName = "Sendo";
	            else if(device.getAttribute("id").indexOf("opwv_sdk_ver1_sub7023119") == 0)
	                brandName = "Openwave";
	            else if(device.getAttribute("id").indexOf("amazon_") == 0)
	                brandName = "Amazon";
	            else if(device.getAttribute("id").indexOf("upg1_ver_1_subblazer43do50") == 0)
	                continue;
	            else if(device.getAttribute("id").indexOf("netfront_") == 0)
	                continue;
	            else if(device.getAttribute("id").indexOf("elson_") == 0)
	                continue;
	            else if(device.getAttribute("id").indexOf("generic_") == 0)
	                continue;
	            else if(device.getAttribute("id").indexOf("opera_nokia_") == 0)
	                continue;
	            else if(device.getAttribute("id").equals("ms_mobile_browser_ver1_msie60_ie812"))
	                continue;
	            else{
	            	System.out.println("Unknown brand " + device.getAttribute("id") + " " + modelName + " " + marketingName);
	            	unknownBrands += 1;
	            }
			}
			
			if(brandName.equals("") || (modelName != null && modelName.equals("")))
				continue;
			
			if(modelName != null){
				final Charset latin1 = Charset.forName("latin1");
				final ByteBuffer encodedModelName = latin1.encode(modelName);
				final CharBuffer decodedModelName = latin1.decode(encodedModelName);
				if(!decodedModelName.toString().equals(modelName))
					continue;
			}
			
	        if(brandName.indexOf("SonyEricsson") >= 0)
	            brandName = "Sony Ericsson";
	        else if( brandName.equals("RIM"))
	            brandName = "Blackberry";
	        else if( brandName.equals("BenQ-Siemens"))
	            brandName = "BenQ Siemens";
	        else if( brandName.toUpperCase().equals("MOTOROLA"))
	            brandName = "Motorola";
	        else if( brandName.toUpperCase().equals("SAMSUNG"))
	            brandName = "Samsung";
	        else if( brandName.toUpperCase().equals("SOFTBANK"))
	            brandName = "SoftBank";
			
			if(modelName != null)
				modelName = modelName.toLowerCase();
					
			brandName = brandName.toLowerCase();
			
			String secondName = marketingName;
			if(secondName == null || secondName.length() == 0)
				secondName = modelName;
			
			final String humanName = (brandName + " " + secondName).toLowerCase();

			if(todos.containsKey(humanName)){
				final WurlfInfo obj = todos.get(humanName);
				if(obj.getResoHeight() == null)
					obj.setResoHeight(resoHeight);
				if(obj.getResoWidth() == null)
					obj.setResoWidth(resoWidth);
				if(obj.getRealHeight() == null)
					obj.setRealHeight(realHeight);
				if(obj.getRealWidth() == null)
					obj.setRealWidth(realWidth);
			}else{
				final WurlfInfo obj = new WurlfInfo(device.getAttribute("id"), brandName, marketingName, modelName, realWidth, realHeight, resoWidth, resoHeight);
				todos.put(humanName, obj);
			}
		}
		
		System.out.println("There are " + todos.size() + " elements, with " + unknownBrands + " unknown");
		return todos;
	}

	private static NodeList retrieveDevices(final File file)
			throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		final DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        final Document doc = docBuilder.parse(file);
		
        final NodeList list = doc.getElementsByTagName("device");
		return list;
	}
}
