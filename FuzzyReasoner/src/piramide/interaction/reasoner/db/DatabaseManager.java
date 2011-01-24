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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import piramide.interaction.reasoner.Geolocation;

public class DatabaseManager implements IDatabaseManager {
	
	public static final String USERNAME = "piramide";
	public static final String PASSWORD = "piramide_password";
	public static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/PiramideTrendsFull";
	
	public DatabaseManager() throws DatabaseException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new DatabaseException(e);
		}
	}
	
	protected String getConnectionUrl(){
		return DatabaseManager.CONNECTION_URL;
	}
	
	
	public static void main(String [] args) throws Exception {
		final IDatabaseManager dm = new DatabaseManager();
		// dm.populateTables();
		/* final List<MobileDevice> mobileDevices = dm.getResults(2).getMobileDevices();
		for(MobileDevice mobileDevice : mobileDevices){
			System.out.println(mobileDevice.getName());
			System.out.println(mobileDevice.getCapabilities().get(DeviceCapability.real_size));
		}*/
		
		for(String str : dm.getGeolocation()){
			System.out.println(str);
		}
	}
	
	@Override
	public MobileDevices getResults() throws DatabaseException {
		return getResults(-1, Geolocation.ALL);
	}
		
	@Override
	public MobileDevices getResults(int size) throws DatabaseException {
		return getResults(size, Geolocation.ALL);
	}
		
	@Override
	public MobileDevices getResults(Geolocation geo) throws DatabaseException {
		return getResults(-1, geo);
	}
	
	@Override
	public String [] getGeolocation() throws DatabaseException {
		try {
			final Connection con = DriverManager.getConnection( this.getConnectionUrl() , USERNAME, PASSWORD);
			final PreparedStatement stmtDevices = con.prepareStatement("SELECT DISTINCT region FROM Trends");
			final List<String> regions = new ArrayList<String>();
			final ResultSet results = stmtDevices.executeQuery();
			while(results.next()){
				regions.add(results.getString(1));
			}
			return regions.toArray(new String[]{});
		} catch (SQLException e) {
			throw new DatabaseException("Database error: " + e.getMessage(), e);
		}
	}
	
	@Override
	public MobileDevices getResults(int size, Geolocation geo) throws DatabaseException {
		final QueryInformation queryInformation = new QueryInformation();
		
		final List<MobileDevice> devices = new Vector<MobileDevice>();
		
		final Connection con;
		final Statement stmtDevices;
		try {
			con = DriverManager.getConnection( this.getConnectionUrl() , USERNAME, PASSWORD);
			stmtDevices = con.createStatement();
		} catch (SQLException e) {
			throw new DatabaseException("Database error: " + e.getMessage(), e);
		}
		
		final StringBuilder statement = new StringBuilder();
		
		statement.append("SELECT device_name, id");
		for(DeviceCapability capability : DeviceCapability.values()){
			statement.append(", ");
			statement.append(capability.name());
		}
		statement.append(" FROM Devices ");
		
		final ResultSet rsDevices;
		try {
			rsDevices = stmtDevices.executeQuery(statement.toString());
		} catch (SQLException e) {
			throw new DatabaseException("Database error: " + e.getMessage(), e);
		}
		int counter = 0;
		
		int maxDate = Integer.MAX_VALUE; // maxDate is YEAR * 12 + MONTHs
		
		try{
			while(rsDevices.next()){
				if(counter++ == size)
					break;
				final MaxDateHolder currentMaxDate = new MaxDateHolder();
				
				final MobileDevice mobileDevice = retrieveMobileDevice(geo, queryInformation, con, rsDevices, currentMaxDate);
				devices.add(mobileDevice);
				
				if(currentMaxDate.getCurrentMaxDate() != 0 && currentMaxDate.getCurrentMaxDate() < maxDate)
					maxDate = currentMaxDate.getCurrentMaxDate();
			}
		}catch(SQLException e){
			throw new DatabaseException("Database error: " + e.getMessage(), e);
		}
		
		final int maxMonth = (maxDate - 1) % 12 + 1;
		final int maxYear  = (maxDate - 1) / 12;
		
		queryInformation.setMaxMonth(maxMonth);
		queryInformation.setMaxYear(maxYear);
		
		return new MobileDevices(devices);
	}

	private MobileDevice retrieveMobileDevice(Geolocation geo,
			final QueryInformation queryInformation, final Connection con,
			final ResultSet rsDevices, final MaxDateHolder currentMaxDate)
			throws SQLException {
		final PreparedStatement stmtTrends = con.prepareStatement("SELECT value, year, month FROM Trends WHERE device_name = ? AND region = ?");
		stmtTrends.setString(1, rsDevices.getString("device_name"));
		stmtTrends.setString(2, geo.getCode());
		final ResultSet rsTrends = stmtTrends.executeQuery();
		
		final List<Trend> trends = new Vector<Trend>();
		while(rsTrends.next()){
			final Trend trend = new Trend(rsTrends.getInt("year"), rsTrends.getInt("month"), rsTrends.getDouble("value"));
			if(trend.getYear() == 0 || trend.getMonth() == 0) // Invalid data
				continue;
			trends.add(trend);
			int currentDate = (trend.getMonth() - 1) + trend.getYear() * 12;
			if(currentDate > currentMaxDate.getCurrentMaxDate())
				currentMaxDate.setCurrentMaxDate(currentDate);
		}
		
		final Map<DeviceCapability, Number> capabilities = new HashMap<DeviceCapability, Number>();
		for(DeviceCapability capability : DeviceCapability.values())
			capabilities.put(capability, (Number)rsDevices.getObject(capability.name()));
		
		
		return new MobileDevice(rsDevices.getString("device_name"), capabilities, trends, queryInformation);
	}
	
	private static class MaxDateHolder{
		private int currentMaxDate = 0;

		int getCurrentMaxDate() {
			return this.currentMaxDate;
		}

		void setCurrentMaxDate(int currentMaxDate) {
			this.currentMaxDate = currentMaxDate;
		}
	}
	
	@Override
	public MobileDevice retrieveDeviceNames(String deviceID) throws DatabaseException{
		final StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT device_name");
		for(DeviceCapability capability : DeviceCapability.values()){
			sqlQuery.append(", ");
			sqlQuery.append(capability.name());
		}
		sqlQuery.append(" FROM Devices WHERE device_name = ?");
		
		Connection con = null;
		final PreparedStatement stmtDevices;
		try {
			con = DriverManager.getConnection( this.getConnectionUrl() , USERNAME, PASSWORD);
			stmtDevices = con.prepareStatement(sqlQuery.toString());
			stmtDevices.setString(1, deviceID);
			
			final ResultSet dbResults = stmtDevices.executeQuery();
			if(!dbResults.next())
				throw new DatabaseException("Device not found");
			
			final QueryInformation info = new QueryInformation();
			final MaxDateHolder currentMaxDate = new MaxDateHolder();
			
			return retrieveMobileDevice(Geolocation.ALL, info, con, dbResults, currentMaxDate);
		} catch (SQLException e) {
			throw new DatabaseException("Database error: " + e.getMessage(), e);
		} finally {
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				throw new DatabaseException("Database error: " + e.getMessage(), e);
			}
		}
	}	
	
	@Override
	public List<MobileDevice> searchDeviceNames(String query, int max) throws DatabaseException{
		final List<String> conditions = new ArrayList<String>();
		
		for(String name : query.split(" ")){
			if(name.trim().length() > 0){
				
				// TODO: are there more conditions of SQL Injection?
				final String safeName = name.trim().replace("'", "\"");
				
				final StringBuilder builder = new StringBuilder();
				builder.append("device_name LIKE '%");
				builder.append(safeName);
				builder.append("%' OR wurfl_id LIKE '%");
				builder.append(safeName);
				builder.append("%' OR marketing_name LIKE '%");
				builder.append(safeName);
				builder.append("%' OR brand_name LIKE '%");
				builder.append(safeName);
				builder.append("%' OR model_name LIKE '%");
				builder.append(safeName);
				builder.append("%'");
				
				conditions.add(builder.toString());
			}
		}
		
		final StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT device_name");
		for(DeviceCapability capability : DeviceCapability.values()){
			sqlQuery.append(", ");
			sqlQuery.append(capability.name());
		}
		sqlQuery.append(" FROM Devices WHERE (");
		
		sqlQuery.append(conditions.get(0));
		sqlQuery.append(")");
		
		for(int i = 1; i < conditions.size(); ++i){
			final String currentCondition = conditions.get(i);
			sqlQuery.append(" AND (");
			sqlQuery.append(currentCondition);
			sqlQuery.append(")");
		}
		
		sqlQuery.append(" LIMIT ");
		sqlQuery.append(max);
		
		final List<MobileDevice> results = new ArrayList<MobileDevice>();
		final Connection con;
		final Statement stmtDevices;
		try {
			con = DriverManager.getConnection( this.getConnectionUrl() , USERNAME, PASSWORD);
			stmtDevices = con.createStatement();
			final ResultSet dbResults = stmtDevices.executeQuery(sqlQuery.toString());
			while(dbResults.next()){
				final QueryInformation info = new QueryInformation();
				final MaxDateHolder currentMaxDate = new MaxDateHolder();
				
				final MobileDevice mobileDevice = retrieveMobileDevice(Geolocation.ALL, info, con, dbResults, currentMaxDate);
				results.add(mobileDevice);
			}
			
			con.close();
		} catch (SQLException e) {
			throw new DatabaseException("Database error: " + e.getMessage(), e);
		}
		
		return results;
	}
}
