package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdateTranCodeByProdAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		// �ǸŻ�ǰ���� ����ϱ� Ŭ����
		//System.out.println("UpdateTranCodeByProdAction ����");
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		String tranCode = request.getParameter("tranCode");
		
		//System.out.println(prodNo);
		//System.out.println(tranCode);
		
		PurchaseService service = new PurchaseServiceImpl();

		Purchase purchase = service.getPurchase2(prodNo);
		
		int tranNo = purchase.getTranNo();
		
		//System.out.println(tranNo);
		
		purchase.setTranNo(tranNo);
		purchase.setTranCode(tranCode);
		
		service.updateTranCode(purchase);
		request.setAttribute("purchase", purchase);
		
		//System.out.println("UpdateTranCodeByProdAction ��");
		return "forward:/listProduct.do?menu=manage";
	}
}