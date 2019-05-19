package com.model2.mvc.service.product.dao;
	
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
	
	
public class ProductDAO {
	
	public ProductDAO(){
	}
	
	public void insertProduct(Product product) throws Exception {
		
		//System.out.println("[insertProduct Start]");		
		
		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO product VALUES (seq_product_prod_no.NEXTVAL,?,?,?,?,?,SYSDATE)";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, product.getProdName());
		pStmt.setString(2, product.getProdDetail());
		pStmt.setString(3, product.getManuDate());
		pStmt.setInt(4, product.getPrice());
		pStmt.setString(5, product.getFileName());
		pStmt.executeUpdate();
		
		pStmt.close();
		con.close();
	}
	
	public Product findProduct(int productNo) throws Exception {
		
		//System.out.println("[findProduct Start]");
		
		Connection con = DBUtil.getConnection();

		String sql = "SELECT "
				+ "prod_no, prod_name, prod_detail, manufacture_day, price, image_file, reg_date "
				+ "FROM product "
				+ "WHERE prod_no = ?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, productNo);

		ResultSet rs = pStmt.executeQuery();
		//System.out.println(rs);
		
		Product product = null;
		while (rs.next()) {
			product = new Product();
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(rs.getInt("price"));
			product.setFileName(rs.getString("image_file"));
			product.setRegDate(rs.getDate("reg_date"));
		}
		
		rs.close();
		pStmt.close();
		con.close();
		

		return product;
	}
	
	public Map<String,Object> getProductList(Search search) throws Exception {
		
		//System.out.println("[getProductList Start]");
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT " 
					+ "p.prod_no, p.prod_name, p.prod_detail, p.manufacture_day, "
					+ "p.price, p.image_file, p.reg_date, t.tran_status_code "
					+ "FROM product p " 
					+ "LEFT JOIN transaction t ON t.prod_no = p.prod_no"; 
		
		if (search.getSearchCondition() != null) {
			if (search.getSearchCondition().equals("0") && !search.getSearchKeyword().equals("")) {
				sql += " WHERE prod_no LIKE '%" + search.getSearchKeyword() + "%'";
			} else if (search.getSearchCondition().equals("1") && !search.getSearchKeyword().equals("")) {
				sql += " WHERE prod_name LIKE '%" + search.getSearchKeyword() + "%'";
			} else if (search.getSearchCondition().equals("2") && !search.getSearchKeyword().equals("")) {
				sql += " WHERE price LIKE '%" + search.getSearchKeyword() + "%'";
			}
		}
		
		if (search.getSearchPrice() != null) {
			if (search.getSearchPrice().equals("lowPrice")) {
				sql += " ORDER BY p.price ASC";
			} else {
				sql += " ORDER BY p.price DESC";
			}
		}		
		
		//System.out.println("1.[ProductDAO :: Original SQL] :: " + sql);
		
		int totalCount = this.getTotalCount(sql);
		//System.out.println(totalCount);
		
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
 
		List<Product> list = new ArrayList<Product>();
		
		while(rs.next()) {
			Product product = new Product();
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("manufacture_day"));
			product.setPrice(rs.getInt("price"));
			product.setFileName(rs.getString("image_file"));
			product.setRegDate(rs.getDate("reg_date"));
			product.setProTranCode(rs.getString("tran_status_code"));
			
			list.add(product);
		}

		map.put("totalCount", new Integer(totalCount));
		//System.out.println(map.get("totalCount"));

		map.put("list", list);
		//System.out.println(map.get("list"));

		rs.close();
		pStmt.close();
		con.close();

		return map;
	}
	
	public void updateProduct(Product product) throws Exception {
		
		//System.out.println("updateProduct 시작");
		
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE product "
					+ "SET prod_name=?, prod_detail=?, manufacture_day=?, "
					+ "price=?, image_file=? "
					+ "WHERE prod_no=?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, product.getProdName());
		pStmt.setString(2, product.getProdDetail());
		pStmt.setString(3, product.getManuDate());
		pStmt.setInt(4, product.getPrice());
		pStmt.setString(5, product.getFileName());
		pStmt.setInt(6, product.getProdNo());
		
		pStmt.executeUpdate();
		
		pStmt.close();
		con.close();
		
	}
	// 게시판 Page 처리를 위한 전체 Row(totalCount)  return
		private int getTotalCount(String sql) throws Exception {
			
			sql = "SELECT COUNT(*) "+
			          "FROM ( " +sql+ ") countTable";
			
			Connection con = DBUtil.getConnection();
			PreparedStatement pStmt = con.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();
			
			int totalCount = 0;
			if( rs.next() ){
				totalCount = rs.getInt(1);
			}
			
			pStmt.close();
			con.close();
			rs.close();
			
			return totalCount;
		}
		
		// 게시판 currentPage Row 만  return 
		private String makeCurrentPageSql(String sql , Search search){
			sql = 	"SELECT * "+ 
						"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
										" 	FROM (	"+sql+" ) inner_table "+
										"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
						"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
			
			//System.out.println(sql);	
			
			return sql;
		}
	
}	