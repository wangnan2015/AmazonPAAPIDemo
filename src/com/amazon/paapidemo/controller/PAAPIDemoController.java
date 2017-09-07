package com.amazon.paapidemo.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.amazon.paapidemo.helper.AppConfig;
import com.amazon.paapidemo.helper.SignedRequestsHelper;
import com.amazon.paapidemo.pojo.Cart;
import com.amazon.paapidemo.pojo.CartAddResponse;
import com.amazon.paapidemo.pojo.CartClearResponse;
import com.amazon.paapidemo.pojo.CartCreateResponse;
import com.amazon.paapidemo.pojo.CartGetResponse;
import com.amazon.paapidemo.pojo.ItemSearchResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PAAPIDemoController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SignedRequestsHelper signedRequestsHelper;
    
    @Autowired
    private AppConfig appConfig;
    
    // global cart data only in memory, not persisted in DB
    private Map<String, Cart> myCarts = new HashMap<>();
    
    /**
     * Show homepage
     * @return
     */
    @RequestMapping(value="/", method=RequestMethod.GET)
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    /**
     * Call PAAPI ItemSearch to search books
     * @param keyword
     * @return
     */
    @RequestMapping(value="/book", method=RequestMethod.GET)
    public ModelAndView search(String keyword) {
        
        log.info("search: " + keyword);
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("AWSAccessKeyId", appConfig.getAwsAccessKeyId());
        params.put("AssociateTag", appConfig.getAssociateTag());
        params.put("SearchIndex", "Books");
        params.put("Keywords", keyword);
        params.put("ResponseGroup", "BrowseNodes,Images,ItemAttributes,Offers");

        String url = signedRequestsHelper.sign(params);
        URI restServiceURI = UriComponentsBuilder.fromHttpUrl(url).build(true).toUri();

        ItemSearchResponse response = restTemplate.getForObject(restServiceURI, ItemSearchResponse.class);
        log.info("response: " + response);
        
        return new ModelAndView("query", "model", response.getItems());
    }

    /**
     * Call PAAPI CartGet to get cart info
     * @param input
     * @param request
     * @return
     */
    @RequestMapping(value="/cart", method=RequestMethod.GET, headers="Accept=application/json")
    public @ResponseBody Cart getCart(HttpServletRequest request) {

        log.info("getCart: " + request.getRemoteHost());

        if (!this.myCarts.containsKey(request.getRemoteHost())) {
            return null;
        }
        
        Cart cart = this.myCarts.get(request.getRemoteHost());
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "CartGet");
        params.put("AWSAccessKeyId", appConfig.getAwsAccessKeyId());
        params.put("AssociateTag", appConfig.getAssociateTag());
        params.put("CartId", cart.getCartId());
        params.put("HMAC", cart.getHmac());
        params.put("ResponseGroup", "Cart");

        String url = signedRequestsHelper.sign(params);
        URI restServiceURI = UriComponentsBuilder.fromHttpUrl(url).build(true).toUri();
        log.info("url: " + restServiceURI);

        CartGetResponse response = restTemplate.getForObject(restServiceURI, CartGetResponse.class);
        log.info("response: " + response);

        return response.getCart();
    }

    /**
     * Call PAAPI CartCreate to create a cart
     * @param input
     * @param request
     * @return
     */
    @RequestMapping(value="/cart", method=RequestMethod.POST, headers="Accept=application/json")
    public @ResponseBody Cart createCart(@RequestBody String input, HttpServletRequest request) {
        
        String asin = input.split("=")[1];
        log.info("create cart: " + asin);
            
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "CartCreate");
        params.put("AWSAccessKeyId", appConfig.getAwsAccessKeyId());
        params.put("AssociateTag", appConfig.getAssociateTag());
        params.put("Item.1.ASIN", asin);
        params.put("Item.1.Quantity", "1");
        params.put("ResponseGroup", "Cart");

        String url = signedRequestsHelper.sign(params);
        URI restServiceURI = UriComponentsBuilder.fromHttpUrl(url).build(true).toUri();
        log.info("url: " + restServiceURI);

        CartCreateResponse response = restTemplate.getForObject(restServiceURI, CartCreateResponse.class);
        log.info("response: " + response);
        
        this.myCarts.put(request.getRemoteHost(), response.getCart());
        return response.getCart();
    }

    /**
     * Call PAAPI CartAdd to add new item to cart
     * @param input
     * @param request
     * @return
     */
    @RequestMapping(value="/cart", method=RequestMethod.PUT, headers="Accept=application/json")
    public @ResponseBody Cart addtoCart(@RequestBody String input, HttpServletRequest request) {
        
        String asin = input.split("=")[1];
        log.info("addToCart: " + asin);
                    
        Cart cart = this.myCarts.get(request.getRemoteHost());
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "CartAdd");
        params.put("AWSAccessKeyId", appConfig.getAwsAccessKeyId());
        params.put("AssociateTag", appConfig.getAssociateTag());
        params.put("CartId", cart.getCartId());
        params.put("HMAC", cart.getHmac());
        params.put("Item.1.ASIN", asin);
        params.put("Item.1.Quantity", "1");
        params.put("ResponseGroup", "Cart");

        String url = signedRequestsHelper.sign(params);
        URI restServiceURI = UriComponentsBuilder.fromHttpUrl(url).build(true).toUri();
        log.info("url: " + restServiceURI);

        CartAddResponse response = restTemplate.getForObject(restServiceURI, CartAddResponse.class);
        
        log.info("response: " + response);
        return response.getCart();

    }

    /**
     * Call PAAPI CartClear to clear the cart
     * @param request
     * @return
     */
    @RequestMapping(value="/cart", method=RequestMethod.DELETE, headers="Accept=application/json")
    public @ResponseBody Cart clearCart(HttpServletRequest request) {
        
        log.info("clearCart ...");
                    
        Cart cart = this.myCarts.get(request.getRemoteHost());
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "CartClear");
        params.put("AWSAccessKeyId", appConfig.getAwsAccessKeyId());
        params.put("AssociateTag", appConfig.getAssociateTag());
        params.put("CartId", cart.getCartId());
        params.put("HMAC", cart.getHmac());
        params.put("ResponseGroup", "Cart");

        String url = signedRequestsHelper.sign(params);
        URI restServiceURI = UriComponentsBuilder.fromHttpUrl(url).build(true).toUri();
        log.info("url: " + restServiceURI);

        CartClearResponse response = restTemplate.getForObject(restServiceURI, CartClearResponse.class);
        
        log.info("response: " + response);
        return response.getCart();

    }


}
