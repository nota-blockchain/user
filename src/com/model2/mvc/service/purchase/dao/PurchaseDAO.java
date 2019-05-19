package com.model2.mvc.service.purchase.dao;

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
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class PurchaseDAO {
		
	public PurchaseDAO(){
	}

	// 1. insertPurchase
	public void insertPurchase(Purchase purchase) throws Exception {

		
		
		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO transaction VALUES "
				+ "(seq_transaction_tran_no.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, 0, sysdate, to_date(?, 'YYYY/MM/DD'))";

		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, purchase.getPurchaseProd().getProdNo());
		pStmt.setString(2, purchase.getBuyer().getUserId());
		pStmt.setString(3, purchase.getPaymentOption());
		pStmt.setString(4, purchase.getReceiverName());
		pStmt.setString(5, purchase.getReceiverPhone());
		pStmt.setString(6, purchase.getDivyAddr());
		pStmt.setString(7, purchase.getDivyRequest());
		pStmt.setString(8, purchase.getDivyDate());

		pStmt.executeUpdate();

		pStmt.close();
		con.close();
	}

	// 2. findPurcase
	public Purchase findPurchase(int tranNo) throws Exception {
		
		

		Connection con = DBUtil.getConnection();

		String sql = "SELECT " 
				+ "tran_no, prod_no, buyer_id, payment_option, receiver_name, "
				+ "receiver_phone, dlvy_addr, dlvy_request, tran_status_code, " 
				+ "order_date, dlvy_date "
				+ "FROM transaction "
				+ "WHERE tran_no = ?";

		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, tranNo);
		
		ResultSet rs = pStmt.executeQuery();

		Purchase purchase = null;
		while (rs.next()) {
			purchase = new Purchase();
			ProductService productService = new ProductServiceImpl();
			UserService userService = new UserServiceImpl();

			purchase.setTranNo(rs.getInt("tran_no"));
			purchase.setPurchaseProd(productService.getProduct(rs.getInt("prod_no")));
			purchase.setBuyer(userService.getUser(rs.getString("buyer_id")));
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));

			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDivyAddr(rs.getString("dlvy_addr"));
			purchase.setDivyRequest(rs.getString("dlvy_request"));
			purchase.setTranCode(rs.getString("tran_status_code"));

			purchase.setOrderDate(rs.getDate("order_date"));
			purchase.setDivyDate(rs.getString("dlvy_date"));

		}

		rs.close();
		pStmt.close();
		con.close();

		return purchase;
	}
	
	// 3. findPurcase2
		public Purchase findPurchase2(int prodNo) throws Exception {
			
			

			Connection con = DBUtil.getConnection();

			String sql = "SELECT " 
						+ "tran_no "
						+ "FROM transaction "
						+ "WHERE prod_no = "+ prodNo;
			
			PreparedStatement pStmt = con.prepareStatement(sql);
			ResultSet rs = pStmt.executeQuery();

			Purchase purchase = null;
			while (rs.next()) {
				purchase = new Purchase();

				purchase.setTranNo(rs.getInt("tran_no"));

			}

			rs.close();
			pStmt.close();
			con.close();

			return purchase;
		}
	
	

	// 4. getPurchaseList
	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
		
		

		Map<String, Object> map = new HashMap<String, Object>();

		Connection con = DBUtil.getConnection();

		String sql = "SELECT " 
					+ "t.tran_no, t.prod_no, t.buyer_id, t.payment_option, t.receiver_name, " 
					+ "t.receiver_phone, t.dlvy_addr, t.dlvy_request, t.tran_status_code, " 
					+ "t.order_date, t.dlvy_date " 
					+ "FROM transaction t " 
					+ "LEFT JOIN product p ON t.prod_no = p.prod_no " 
					+ "WHERE buyer_id = '"+ buyerId +"'"; 
		
		sql += " ORDER BY 1";

		

		int totalCount = this.getTotalCount(sql);
		

		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		List<Purchase> list = new ArrayList<Purchase>();

		while (rs.next()) {
			Purchase purchase = new Purchase();
			UserService userService = new UserServiceImpl();
			ProductService productService = new ProductServiceImpl();
			
			purchase.setTranNo(rs.getInt("tran_no"));
			purchase.setPurchaseProd(productService.getProduct(rs.getInt("prod_no")));
			
			purchase.setBuyer(userService.getUser(rs.getString("buyer_id")));
			purchase.setPaymentOption(rs.getString("payment_option"));
			purchase.setReceiverName(rs.getString("receiver_name"));

			purchase.setReceiverPhone(rs.getString("receiver_phone"));
			purchase.setDivyAddr(rs.getString("dlvy_addr"));
			purchase.setDivyRequest(rs.getString("dlvy_request"));
			purchase.setTranCode(rs.getString("tran_status_code"));

			purchase.setOrderDate(rs.getDate("order_date"));
			purchase.setDivyDate(rs.getString("dlvy_date"));
			list.add(purchase);
		}

		map.put("totalCount", new Integer(totalCount));
		

		map.put("list", list);
		

		rs.close();
		pStmt.close();
		con.close();

		return map;
	}
	
	// 5.updatePurchase
	public void updatePurchase(Purchase purchase) throws Exception {
		
		
		
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction "
					+ "SET "
					+ "payment_option=?, receiver_name=?, receiver_phone=?, "
					+ "dlvy_addr=?, dlvy_request=?, dlvy_date=? "
					+ "WHERE tran_no = ?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		
		pStmt.setString(1, purchase.getPaymentOption());
		pStmt.setString(2, purchase.getReceiverName());
		pStmt.setString(3, purchase.getReceiverPhone());
		pStmt.setString(4, purchase.getDivyAddr());
		pStmt.setString(5, purchase.getDivyRequest());
		pStmt.setString(6, purchase.getDivyDate());
		pStmt.setInt(7, purchase.getTranNo());
		
		pStmt.executeUpdate();
		
		pStmt.close();
		con.close();
	}

	// 6. updateTranCode
	public void updateTranCode(Purchase purchase) throws Exception {
		
		
		
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE "
				+ "transaction "
				+ "SET tran_status_code = ? "
				+ "WHERE tran_no = ?";
		
		PreparedStatement pStmt = con.prepareStatement(sql);
		
		pStmt.setString(1, purchase.getTranCode());
		pStmt.setInt(2, purchase.getTranNo());
		
		pStmt.executeUpdate();
		
		pStmt.close();
		con.close();
	
	}
	
	// 7. getSaleList
		public Map<String,Object> getSaleList(Search search) throws Exception {
			return null;
		}
	
	
	
	
	
	
	
	
	// 게시판 Page 처리를 위한 전체 Row(totalCount) return
	private int getTotalCount(String sql) throws Exception {

		sql = "SELECT COUNT(*) " + "FROM ( " + sql + ") countTable";

		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if (rs.next()) {
			totalCount = rs.getInt(1);
		}

		pStmt.close();
		con.close();
		rs.close();

		return totalCount;
	}

	// 게시판 currentPage Row 만 return
	private String makeCurrentPageSql(String sql, Search search) {
		sql = "SELECT * " + "FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " + " 	FROM (	" + sql
				+ " ) inner_table " + "	WHERE ROWNUM <=" + search.getCurrentPage() * search.getPageSize() + " ) "
				+ "WHERE row_seq BETWEEN " + ((search.getCurrentPage() - 1) * search.getPageSize() + 1) + " AND "
				+ search.getCurrentPage() * search.getPageSize();


		return sql;
	}
}