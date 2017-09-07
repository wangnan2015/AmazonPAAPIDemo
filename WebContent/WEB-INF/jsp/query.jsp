<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<hr/>

<c:forEach var="item" items="${model.items}">
<div class="product-box">
    <a target="_blank" href="${item.detailPageURL}">
        <img src="${item.mediumImage.url}" width="120" height="160">
    </a>
    <div class="product-title">
        <h3>${item.itemAttributes.title}</h3>
    </div>
    <p class="product-price">${item.offers.offers[0].offerListing.price.formattedPrice}<br>
       <a target="_blank" style="color: #337ab7; text-decoration:none;" href="${item.offers.moreOffersUrl}"> More offers </a>
    </p>
    <div>
        <span class="a-button a-button-primary">
            <span class="a-button-inner">
                <img src="http://webservices.amazon.com/scratchpad/assets/images/Amazon-Favicon-64x64.png" class="a-icon a-icon-shop-now">
                <button type="button" class="a-button-input">${item.asin}</button>
                <span class="a-button-text">加入购物车</span>
            </span>
        </span>
    </div>
</div>
</c:forEach>

<script>


$(document).ready(function() {
	
	$(".a-button-input").click(function(){
		
		if ($("#mycart").html() == '') {
			createMyCart($(this).html());			
		} else {
			addtoMyCart($(this).html());
		}

	});	




});


</script>
