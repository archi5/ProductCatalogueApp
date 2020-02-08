package com.rapidor.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.rapidor.services.MysqlConnection;

public class App {
	public static void main(String[] args) {
		MysqlConnection conn = null;
		try {
			
			conn = MysqlConnection.getInstance();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Type the Product Name/Product Code");
			String productCode = null;
			productCode = reader.readLine();
			conn.findTopParent(productCode);
			
			System.out.println("Type the Product Name eg: Agnes, Aike to find all it's children");
			String prdName = reader.readLine();
			conn.findAllProducts(prdName);
			
			System.out.println("active and inactive count");
			conn.findCount();
			
			System.out.println("Average product price per Category L1 and Category L2");
			conn.averagePrice();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
