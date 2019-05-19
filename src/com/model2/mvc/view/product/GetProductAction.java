package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class GetProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("GetProductAction.java 실행됨");
		
		String prodNo = request.getParameter("prodNo");
		System.out.println("GetProductAction.java : prodNo : " + prodNo);
		
		ProductService service = new ProductServiceImpl();
		Product product = service.getProduct(Integer.parseInt(prodNo));
		System.out.println("GetProductAction.java : product : " + product);
		
		request.setAttribute("product", product);
		
		String menu = request.getParameter("menu");
		
		if (menu.equals("manage")) {
			System.out.println("GetProductAction.java equals 'manage' 실행");
//			return "forward:/product/updateProductView.jsp";
			return "redirect:/updateProductView.do?prodNo="+prodNo;
		} else {
			System.out.println("GetProductAction.java equals 'search' 실행");
			return "forward:/product/getProduct.jsp";
		}
		
		// Cookie 작성할것 = '최근 본 상품'
	}
}