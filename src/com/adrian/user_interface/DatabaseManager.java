package com.adrian.user_interface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * <P>A Simple MySQL Connection API Wrapper
 *
 * <P>Each method provides several kinds of information. This Class can do the following:
 * <UL>
 *   <LI> Check If there's existing Database.
 *   <LI> Create Database if there's no existing.
 *   <LI> Drop Database or Delete Database.
 *   <LI> Create Connection (Default: MUST)
 *   <LI> Create Table
 *   <LI> Get / Fetch Data 
 *   <LI> Add / Append Data  
 *   <LI> Update / Set Data
 *   <LI> Delete / Drop Data (Record)
 * </UL>
 *
 */
public class DatabaseManager {
	public Connection connection;
	private Statement statement;
	
	/**
	 * <P>This checks if there's an existing Database.
	 * @param input Name of the Database the you would like to check.
	 * @return boolean Outputs true if there's a Database existing, else false.
	 * @throws SQLException
	 * */
	public boolean checkDB(String input) throws SQLException {
		String sqlQuery = "SHOW DATABASES LIKE '" + input + "'";
		ResultSet rs = statement.executeQuery(sqlQuery);
		rs.next();
		try {
			String temp = rs.getString(1);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * <P>Creates a new Database when there's no existing Database.
	 * @param databaseName Name of the Database you want to create.
	 * @throws SQLException
	 * */
	public void createDatabase(String databaseName) throws SQLException {
		String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        statement.execute(sql);
        System.out.println("Database created successfully...");   
	}
	
	/**
	 * <P>Drops or Deletes Database.
	 * @param databaseName Name of the Database you want to drop or delete.
	 * @throws SQLException
	 * */
	public void dropDatabase(String databaseName) throws SQLException {
		String sql = "DROP DATABASE IF EXISTS" + databaseName;
        statement.execute(sql);
        System.out.println(databaseName + " was deleted successfully...");   
	}
	
	/**
	 * <P>Connects Java program to MySQL Database using JDBC.
	 * 
	 * *
	 * <BR>
	 * Code Sample:
	 * <BR><BR>
	 * {@code
	 * db.createConnection();
	 * }
	 * */
	public void createConnection(String schemaName) {
		// PLEASE CREATE SCHEMA FIRST
		String url = "jdbc:mysql://localhost:3306/";
		String databaseName = schemaName;
		String username = "root";
		String password = "Adrian123";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url + databaseName, username, password);
			statement = (Statement) connection.createStatement();
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	/**
	 * <P>Creates new Table with Columns indicated using SQL Injection.
	 * @param tableName Name of the table you want to create.
	 * @param columnsQuery MySQL Query for creating columns, and idNo is instantly created.
	 * @throws SQLException
	 * */
	public void createTable(String tableName, String columnsQuery) throws SQLException {
		// SHOULD ONLY BE USED TO DEVELOPERS!!!!
		String sql = "CREATE TABLE " + tableName + " (idNo INT(64) NOT NULL AUTO_INCREMENT, "
				+ columnsQuery
				+ ",PRIMARY KEY (`idNo`));";
		 statement.execute(sql);
	}
	
	/**
	 * <P>Get or Fetch Data in Database Table using pattern as SQL Query.
	 * @param pattern SQL Query or Pattern to filter what column should be fetched within the Table.
	 * @param tableName Name of the Table your data should be fetched.
	 * @param whereCondition  A SQL Query for clean fetching data within the table.
	 * @return String | List[Object] if there's existing or List as string if others are affected, and will be fetched.
	 * @throws SQLException
	 * 
	 * <BR>
	 * Code Sample:
	 * <BR><BR>
	 * {@code
	 * String x = (String) db.readDB("name", "usertable", "idNo = 1;").get(0);
	 * }
	 * */
	public List<Object> readDB(String pattern, String tableName, String whereCondition) throws SQLException {
		// READ DB
		if(pattern == "") pattern = "*";
		List<Object> outputs = new ArrayList<Object>();
		String sql = "SELECT " + pattern + " FROM " + tableName + " WHERE " + whereCondition;
//		String sql = "SELECT * FROM userTable;";
		ResultSet resultSet = statement.executeQuery(sql);
		while(resultSet.next()){
			String foundType = resultSet.getString(pattern);
			outputs.add(foundType);
		}
		return outputs;
	}
	
	/**
	 * <P>Adds Data to a specific Column within the Table.
	 * @param data Value you want to be added within the Column.
	 * @param tableName Name of the Table you want the data to be appended.
	 * @param columnQuery Column to be inserted to.
	 * @throws SQLException
	 *  
	 * <BR>
	 * Code Sample:
	 * <BR><BR>
	 * {@code
	 * db.addDB("TESTDB", "userTable", "name");
	 * }
	 */
	public void addDB(String data, String tableName, String columnQuery) throws SQLException {
		PreparedStatement POST = connection.prepareStatement("INSERT INTO "
				+ tableName
				+ " ("
				+ columnQuery
				+ ") VALUES (?)");
	    POST.setString(1, data);
	    POST.execute();
	}
	
	public void addDB(String[] data, String tableName, String columnQuery) throws SQLException {
		PreparedStatement POST = connection.prepareStatement("INSERT INTO "
				+ tableName
				+ " ("
				+ columnQuery
				+ ") VALUES (?, ?, ?, ?)");
	    POST.setInt(1, Integer.parseInt(data[0]));
	    POST.setInt(2, Integer.parseInt(data[1]));
	    POST.setInt(3, Integer.parseInt(data[2]));
	    POST.setString(4, data[3]);
	    POST.execute();
	}
	
	/**
	 * <P>Updates existing data within the Database Table
	 * @param updateValue Value to be replaced within the old data.
	 * @param tableName Name of the table you want to replace the old data with.
	 * @param targetColumn Name of the Column you want to replace the existing data.
	 * @param idNumber ID Number you want to filter for updating one value at a time.
	 * @throws SQLException
	 * 
	 * <BR>
	 * Code Sample:
	 * <BR><BR>
	 * {@code
	 * db.updateDB(param1Field.getText().toString(), "userTable", "name", 1);
	 * }
	 */
	public void updateDB(String updateValue, String tableName, String targetColumn, int idNumber) throws SQLException {
		String sql = "UPDATE "
				+ tableName
				+ " SET "
				+ targetColumn
				+ " = ? WHERE idNo = ?;";
		PreparedStatement POST = connection.prepareStatement(sql);
		POST.setString(1, updateValue);
		POST.setInt(2, idNumber);
		POST.execute();
	}
	
	public void updateDB(int updateValue, String tableName, String targetColumn, int idNumber) throws SQLException {
		String sql = "UPDATE "
				+ tableName
				+ " SET "
				+ targetColumn
				+ " = ? WHERE idNo = ?;";
		PreparedStatement POST = connection.prepareStatement(sql);
		POST.setInt(1, updateValue);
		POST.setInt(2, idNumber);
		POST.execute();
	}
	
	/**
	 * <P>Delete record within the Database Table.
	 * @param tableName Name of the Table you want the deleting will happen.
	 * @param targetColumn Name of the Column you want the record to look for.
	 * @param idNumber ID Number of the data to be deleted.
	 * @throws SQLException
	 * 
	 *  
	 * <BR>
	 * Code Sample:
	 * <BR><BR>
	 * <pre>
	 * // Deletes Record with an idNo of 1
	 * 
	 * deleteDB("userTable", "idNo", 1);
	 * </pre>
	 */
	public void deleteDB(String tableName, String targetColumn, int idNumber) throws SQLException {
		String sql = "DELETE FROM "
				+ tableName
				+ " WHERE "
				+ targetColumn
				+ " = "
				+ idNumber;
		
		statement.execute(sql);
	}
}
