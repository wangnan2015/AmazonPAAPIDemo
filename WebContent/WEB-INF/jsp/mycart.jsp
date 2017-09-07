<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="mycart"></div>

<script>

$(document).ready(function() {
	
	showMyCart();

});

function showMyCart() {
	console.info("show my cart ...");
	$.ajax({
		type: 'GET',
		url: 'cart',
		timeout: 60000,
		contentType: 'application/json',
		dataType: 'json',
		data: {},
		async: true,
		success: function(response){
			console.info(response);
			if (response.subTotal == null) {
				$("#mycart").html('<hr/>' + '购物车总金额: $0.0');
			} else {
				$("#mycart").html(
						'<hr/>'
						+ '<div>购物车总金额: ' + response.subTotal.formattedPrice + '</div>'
				        + '<div><a href="' + response.purchaseURL + '">去亚马逊购物车结算</a></div>'
				        + '<div><a href="javascript:void(0);" onclick="clearMyCart()">清空购物车</a></div>');
			}
		},
		error: function(xhr, status, error){
			//alert(error);
		}
	});
}

function createMyCart(asin) {
	console.info("create my cart: " + asin);
	$.ajax({
		type: 'POST',
		url: 'cart',
		timeout: 60000,
		contentType: 'application/json',
		dataType: 'json',
		data: {asin: asin},
		async: true,
		success: function(response){
			console.info(response);
			showMyCart();
		},
		error: function(xhr, status, error){
			alert(error);
		}
	});
}

function addtoMyCart(asin) {
	console.info("create my cart: " + asin);
	$.ajax({
		type: 'PUT',
		url: 'cart',
		timeout: 60000,
		contentType: 'application/json',
		dataType: 'json',
		data: {asin: asin},
		async: true,
		success: function(response){
			console.info(response);
			showMyCart();
		},
		error: function(xhr, status, error){
			alert(error);
		}
	});
}

function clearMyCart() {
	console.info("clear my cart ...");
	$.ajax({
		type: 'DELETE',
		url: 'cart',
		timeout: 60000,
		contentType: 'application/json',
		dataType: 'json',
		data: {},
		async: true,
		success: function(response){
			console.info(response);
			showMyCart();
		},
		error: function(xhr, status, error){
			alert(error);
		}
	});
}

</script>