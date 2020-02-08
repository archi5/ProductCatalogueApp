package com.rapidor.constants;

public class Constants {
	public static String driver = "com.mysql.cj.jdbc.Driver";
	public static String connectionUrl = "jdbc:mysql://localhost:3306/product";
	public static String user = "root";
	public static String password = "super05";

	public static String parentCodeQry = "select Parent_Code from Products where Item_Code = ? AND Item_Name = ?;";
	public static String allProductQry = "select Item_Code from products where Item_Name = ?;";
	public static String countQry = "select count(*) as count, sum(case when enabled like 'y%' then 1 else 0 end) as Active, sum(case when enabled like 'n%'then 1 else 0 end) as Inactive from products;";
	public static String avgQuery = "select Category_L1, Category_L2, MRP_Price from products;";
}
