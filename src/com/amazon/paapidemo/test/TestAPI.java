package com.amazon.paapidemo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class TestAPI {

    private static final String associateTag = "your associate tag";
    private static final String awsAccessKeyId = "your access key";
    private static final String awsSecretKey = "your secret key";

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, IOException {

        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2013-08-01");
        params.put("Operation", "ItemSearch");
        params.put("AWSAccessKeyId", awsAccessKeyId);
        params.put("AssociateTag", associateTag);
        params.put("SearchIndex", "Books");
        params.put("Keywords", "java");
        params.put("ResponseGroup", "BrowseNodes,Images,ItemAttributes,Offers");

        SignedRequestsHelper signedRequestsHelper = new SignedRequestsHelper(awsAccessKeyId, awsSecretKey);
        String requestUrl = signedRequestsHelper.sign(params);
        URL restServiceURL = new URL(requestUrl);

        HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
        if (httpConnection.getResponseCode() != 200) {
            throw new RuntimeException("HTTP GET Request Failed with Error code : " + httpConnection.getResponseCode());
        }
        
        String xml = convert(httpConnection.getInputStream(), Charset.forName("UTF-8"));
        System.out.println(xml);

    }

    private static String convert(InputStream inputStream, Charset charset) throws IOException {
        
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset))) { 
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
     
        return stringBuilder.toString();
    }

}
