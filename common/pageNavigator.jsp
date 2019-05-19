<%@ page contentType="text/html; charset=euc-kr"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${ search.searchPrice == null || search.searchPrice == '' }">

	<c:if test="${ resultPage.currentPage <= resultPage.pageUnit }"> ��  </c:if>
	
	<c:if test="${ resultPage.currentPage > resultPage.pageUnit }">
		<a href="javascript:fncGetList('${ resultPage.currentPage-1}')"> ��  </a>
	</c:if>

	<c:forEach var="i" begin="${resultPage.beginUnitPage}" end="${resultPage.endUnitPage}" step="1">
		<a href="javascript:fncGetList('${ i }');">${ i }</a>
	</c:forEach>

	<c:if test="${ resultPage.endUnitPage >= resultPage.maxPage }">  �� </c:if>
	
	<c:if test="${ resultPage.endUnitPage < resultPage.maxPage }">
		<a href="javascript:fncGetList('${resultPage.endUnitPage+1}')"> �� </a>
	</c:if>
	
</c:if>

<c:if test="${ search.searchPrice != null 
	&& search.searchPrice != '' 
		&& search.searchPrice == 'lowPrice' }">

	<c:if test="${ resultPage.currentPage <= resultPage.pageUnit }"> ��  </c:if>
	
	<c:if test="${ resultPage.currentPage > resultPage.pageUnit }">
		<a href="javascript:fncLowPrice('${ resultPage.currentPage-1}')"> ��  </a>
	</c:if>

	<c:forEach var="i" begin="${resultPage.beginUnitPage}" end="${resultPage.endUnitPage}" step="1">
		<a href="javascript:fncLowPrice('${ i }');">${ i }</a>
	</c:forEach>

	<c:if test="${ resultPage.endUnitPage >= resultPage.maxPage }"> �� </c:if>
	
	<c:if test="${ resultPage.endUnitPage < resultPage.maxPage }">
		<a href="javascript:fncLowPrice('${resultPage.endUnitPage+1}')"> �� </a>
	</c:if>
	
</c:if>

<c:if test="${ search.searchPrice != null 
	&& search.searchPrice != '' 
		&& search.searchPrice == 'highPrice' }">

	<c:if test="${ resultPage.currentPage <= resultPage.pageUnit }"> ��  </c:if>
	
	<c:if test="${ resultPage.currentPage > resultPage.pageUnit }">
		<a href="javascript:fncHighPrice('${ resultPage.currentPage-1}')"> ��  </a>
	</c:if>

	<c:forEach var="i" begin="${resultPage.beginUnitPage}" end="${resultPage.endUnitPage}" step="1">
		<a href="javascript:fncHighPrice('${ i }');">${ i }</a>
	</c:forEach>

	<c:if test="${ resultPage.endUnitPage >= resultPage.maxPage }"> �� </c:if>
	
	<c:if test="${ resultPage.endUnitPage < resultPage.maxPage }">
		<a href="javascript:fncHighPrice('${resultPage.endUnitPage+1}')"> �� </a>
	</c:if>
	
</c:if>
