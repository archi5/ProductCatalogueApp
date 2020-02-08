package com.rapidor.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.rapidor.constants.Constants;

public class MysqlConnection {

	private static MysqlConnection instance;
	private static String driver = Constants.driver;
	private static String connectionUrl = Constants.connectionUrl;
	private static String user = Constants.user;
	private static String password = Constants.password;

	private static Connection connection;
	private static Statement state = null;
	private static ResultSet result;
	private static PreparedStatement pstate;

	private MysqlConnection() throws SQLException {
		try {
			Class.forName(driver);
			this.connection = DriverManager.getConnection(connectionUrl, user, password);
		} catch (ClassNotFoundException ex) {
			System.out.println("Database Connection Creation Failed : " + ex.getMessage());
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public static MysqlConnection getInstance() throws SQLException {
		if (instance == null) {
			instance = new MysqlConnection();
		} else if (instance.getConnection().isClosed()) {
			instance = new MysqlConnection();
		}

		return instance;
	}

	public void findTopParent(String prdCode) throws SQLException {
		String prdNme = prdCode.split("-")[0];
		String prntCode = null;
		try {
			connection = this.getConnection();
			pstate = connection.prepareStatement(Constants.parentCodeQry);
			pstate.setString(1, prdCode);
			pstate.setString(2, prdNme);
			result = pstate.executeQuery();
			result.next();
			prntCode = result.getString(1);

			if (prntCode.isEmpty())
				System.out.println("Parent Code not present.");
			else
				System.out.println("Parent Code is: " + prntCode);
		} catch (SQLException e) {
			System.err.println("Given Product Code is incorrect.");
		}
	}

	public void findAllProducts(String ProductName) throws SQLException {
		try {
			connection = this.getConnection();
			pstate = connection.prepareStatement(Constants.allProductQry);
			pstate.setString(1, ProductName);
			result = pstate.executeQuery();
			while (result.next()) {
				System.out.println(result.getString(1));
			}
		} catch (SQLException e) {
			System.err.println("Given Product Name/Product Code is incorrect.");
		}

	}

	public void findCount() throws SQLException {

		try {
			connection = this.getConnection();
			pstate = connection.prepareStatement(Constants.countQry);
			result = pstate.executeQuery();

			while (result.next()) {
				System.out.println("Total Records: " + result.getInt(1));
				System.out.println("Active: " + result.getInt(2));
				System.out.println("Inactive: " + result.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void averagePrice() throws SQLException {

		try {
			connection = this.getConnection();
			pstate = connection.prepareStatement(" select Category_L1, Category_L2, MRP_Price from products;");
			result = pstate.executeQuery();

			Map<String, String> customMap = new HashMap<String, String>();

			while (result.next()) {
				String categoryL1 = result.getString(1);
				String categoryL2 = result.getString(2);
				int price = result.getInt(3);
				String freqSum = null;
				if (!customMap.containsKey(categoryL1)) {
					freqSum = "1-" + price;
					customMap.put(categoryL1, freqSum);
				} else {
					String res[] = customMap.get(categoryL1).split("-");
					int freq = Integer.parseInt(res[0]) + 1;
					int sum = Integer.parseInt(res[1]) + price;
					freqSum = freq + "-" + sum;
					customMap.put(categoryL1, freqSum);
				}

				if (!customMap.containsKey(categoryL2)) {
					freqSum = "1-" + price;
					customMap.put(categoryL2, freqSum);
				} else {
					String res[] = customMap.get(categoryL2).split("-");
					int freq = Integer.parseInt(res[0]) + 1;
					int sum = Integer.parseInt(res[1]) + price;
					freqSum = freq + "-" + sum;
					customMap.put(categoryL2, freqSum);
				}

			}

			Iterator itr = customMap.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry pair = (Map.Entry) itr.next();
				String pairValue[] = pair.getValue().toString().split("-");
				int freq = Integer.parseInt(pairValue[0]);
				int sum = Integer.parseInt(pairValue[1]);
				float avg = sum/freq;
				System.out.println(pair.getKey() + ": " + avg);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (result != null) {
				result.close();
			}
			if (pstate != null) {
				pstate.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

	}

}
