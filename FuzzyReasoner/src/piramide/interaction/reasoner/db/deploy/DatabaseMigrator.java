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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import piramide.interaction.reasoner.db.DatabaseManager;

import com.csvreader.CsvReader;

public class DatabaseMigrator extends DatabaseManager {

	private static final String INVALID_FILE_PATH = "BadOnes.txt";
	
	public DatabaseMigrator() throws Exception {
	}

	public static void main(String [] args) throws Exception {
		new DatabaseMigrator().populateTables();
	}
	
	/**
	* Given the Downloaded table, we populate the Devices and Trends tables.
	*/
	public void populateTables() {
		int invalidData = 0;
		
		try{
			final Connection con = DriverManager.getConnection( CONNECTION_URL, USERNAME, PASSWORD);
			
			final Statement stmtClearTrends = con.createStatement();
			stmtClearTrends.execute("DELETE FROM Trends");
			stmtClearTrends.close();
			
			final Statement stmtClearDevices = con.createStatement();
			stmtClearDevices.execute("DELETE FROM Devices");
			stmtClearDevices.close();
			
			
			con.setAutoCommit(false);
			
			
			final Statement stmtDownloaded = con.createStatement();
			final ResultSet rsDownloaded   = stmtDownloaded.executeQuery("SELECT device_name, wurfl_id, marketing_name, brand_name, model_name, " + 
										"real_height, real_width, reso_height, " + 
										"reso_width, value, region FROM Downloaded");
			final List<String> deviceNames = new Vector<String>();
			final PreparedStatement stmtDevices = con.prepareStatement("" +
								"INSERT INTO Devices(device_name, wurfl_id, marketing_name, brand_name, model_name, " +
								"real_height, real_width, real_size, reso_height, reso_width, reso_size) " +
								"Values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
			final PreparedStatement stmtTrends = con.prepareStatement("" +
								"INSERT INTO Trends( device_name, region, month, year, value, error_margin ) " +
								"Values(?, ?, ?, ?, ?, ? )");
			final Map<String, Integer> month2month = new HashMap<String, Integer>();
			month2month.put("Jan",  Integer.valueOf(1));
			month2month.put("Feb",  Integer.valueOf(2));
			month2month.put("Mar",  Integer.valueOf(3));
			month2month.put("Apr",  Integer.valueOf(4));
			month2month.put("May",  Integer.valueOf(5));
			month2month.put("Jun",  Integer.valueOf(6));
			month2month.put("Jul",  Integer.valueOf(7));
			month2month.put("Aug",  Integer.valueOf(8));
			month2month.put("Sep",  Integer.valueOf(9));
			month2month.put("Oct",  Integer.valueOf(10));
			month2month.put("Nov",  Integer.valueOf(11));
			month2month.put("Dec",  Integer.valueOf(12));
						
			int counter = 0;
			final Set<String> badOnes = new HashSet<String>();
								
			while(rsDownloaded.next()){
				++counter;
				
				if(counter % 100 == 0){
					System.out.println("committed... " + counter);
					con.commit();
				}
				
				final String deviceName = rsDownloaded.getString("device_name");
				
				if(!deviceNames.contains(deviceName)){
					stmtDevices.setString(1, deviceName);
					stmtDevices.setString(2, rsDownloaded.getString("wurfl_id"));
					stmtDevices.setString(3, rsDownloaded.getString("marketing_name"));
					stmtDevices.setString(4, rsDownloaded.getString("brand_name"));
					stmtDevices.setString(5, rsDownloaded.getString("model_name"));
					stmtDevices.setFloat(6, rsDownloaded.getFloat("real_height"));
					stmtDevices.setFloat(7, rsDownloaded.getFloat("real_width"));
					stmtDevices.setFloat(8, rsDownloaded.getFloat("real_height") * rsDownloaded.getFloat("real_width"));
					stmtDevices.setInt(9, rsDownloaded.getInt("reso_height"));
					stmtDevices.setInt(10, rsDownloaded.getInt("reso_width"));
					stmtDevices.setInt(11, rsDownloaded.getInt("reso_height") * rsDownloaded.getInt("reso_width"));
					stmtDevices.execute();
					deviceNames.add(deviceName);
				}
					
				final String base64csv = rsDownloaded.getString("value");
				
				final CsvReader csvReader2 = buildCSVReader(base64csv);
				if(!csvReader2.readRecord() && base64csv.length() != 924){
					final byte [] binaryCSV = new Base64().decode(base64csv);
					final String stringCSV = new String(binaryCSV, "utf-8");
					if(stringCSV.contains("could not be interpreted")){
						badOnes.add(  rsDownloaded.getString("region") + "@" + deviceName );
					}
				}
				
				
				final CsvReader csvReader = buildCSVReader(base64csv);
				
				int previousMonth = 0;
				int previousYear  = 0;
				float currentValue = 0.0f;
				String currentErrorMargin = "";
				
				boolean executed = false;
				while(csvReader.readRecord()){
					executed = true;
					final String week        = csvReader.get(0);
					final float value        = Float.parseFloat(csvReader.get(1));
					final String errorMargin = csvReader.get(2);
					
					final String [] weekParts = week.split(" ");
					
					final int month = month2month.get(weekParts[0]).intValue();
					final int year  = Integer.parseInt(weekParts[2]);
					
					if(month == previousMonth && year == previousYear){
						currentValue += value;
						currentErrorMargin = currentErrorMargin + "; " + errorMargin;
						continue;
					}
					
					stmtTrends.setString(1, deviceName);
					stmtTrends.setString(2, rsDownloaded.getString("region"));
					stmtTrends.setInt(3, previousMonth);
					stmtTrends.setInt(4, previousYear);
					stmtTrends.setFloat(5, currentValue);
					stmtTrends.setString(6, currentErrorMargin);
					stmtTrends.execute();
					
					currentValue       = value;
					currentErrorMargin = errorMargin;
					previousMonth      = month;
					previousYear       = year;
				}
				if(!executed){
					invalidData++;
					badOnes.add(  rsDownloaded.getString("region") + "@" + deviceName );
				}
			}
			
			con.commit();
			
			stmtDevices.close();
			stmtTrends.close();
			stmtDownloaded.close();
			
			System.out.println("Counter: " + counter);
			System.out.println("Invalid data: " + invalidData);
			
			
			if(invalidData > 0) {
				final StringBuilder builder = new StringBuilder();
				for(String badOne : badOnes){
					builder.append(badOne);
					builder.append("\n");
				}
				
				FileUtils.writeStringToFile(new File(INVALID_FILE_PATH), builder.toString());
				
				System.err.println("Warning: data of " + invalidData + " mobile devices is wrong. Check which devices at " + INVALID_FILE_PATH);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private CsvReader buildCSVReader(final String base64csv) throws UnsupportedEncodingException {
		
		final byte [] binaryCSV = new Base64().decode(base64csv);
		final String stringCSV = new String(binaryCSV, "utf-8");
		final StringBuilder csvBuilder = new StringBuilder();

		boolean recording = false;
		for(String line : stringCSV.split("\n")){
			if(recording){
				csvBuilder.append(line);
				csvBuilder.append('\n');
			}
			if(line.startsWith("Week") && line.indexOf("std error") >= 0)
				recording = true;
			else if(line.trim().length() == 0 && recording)
				break;
		}

		final byte [] extractedBytes = csvBuilder.toString().getBytes(Charset.defaultCharset());
		final InputStream csvStream = new ByteArrayInputStream(extractedBytes);

		final CsvReader csvReader = new CsvReader(csvStream, Charset.defaultCharset());
		return csvReader;
	}

}
