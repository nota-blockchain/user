package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;

public class UpdatePurchaseAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		//System.out.println("UpdatePurchaseAction Ω√¿€");
		String tranNo = request.getParameter("tranNo");
		//System.out.println(tranNo);
		
		Purchase purchase = new Purchase();
		
		purchase.setTranNo(Integer.parseInt(tranNo));
		purchase.setPaymentOption(request.getParameter("paymentOption"));
		purchase.setReceiverName(request.getParameter("receiverName"));
		purchase.setReceiverPhone(request.getParameter("receiverPhone"));
		purchase.setDivyAddr(request.getParameter("receiverAddr"));
		purchase.setDivyRequest(request.getParameter("receiverRequest"));
		purchase.setDivyDate(request.getParameter("divyDate"));
		
		PurchaseService service = new PurchaseServiceImpl();
		service.updatePurchase(purchase);

		purchase = service.getPurchase(Integer.parseInt(tranNo));
		
		request.setAttribute("purchase", purchase);
		
		//System.out.println(purchase);
		//System.out.println("UpdateProductAction ≥°");
		
		return "forward:/purchase/updatePurchase.jsp";
	}
}