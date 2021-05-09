package com.gateway.services;

import com.gateway.dto.Card;
import com.gateway.dto.OrderedProduct;
import com.gateway.dto.Payment;
import com.gateway.exceptions.CartNotFoundException;
import com.gateway.helpers.JsonRequestHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PaymentService {

    @Value("${service.address.orders}")
    private String orderServiceBaseUrl;

    @Value("${service.address.products}")
    private String productsServiceBaseUrl;

    @Value("${service.address.payment}")
    private String paymentServiceBaseUrl;

    @Value("${add-trailing-slash-when-forwarding}")
    private boolean _addTrailingSlashes;


    public void makePayment(Card paymentData, UUID userId) throws IOException, CartNotFoundException {
        OrderedProduct[] products = loadCartContent(userId);
        double price = calculateCartPrice(products);

        makePayment(new Payment(paymentData, price));
        confirmOrder(userId);
    }

    private void confirmOrder(UUID userId) throws IOException {
        String path = orderServiceBaseUrl + "/orders?userid=" + userId + trailingElement();
        HttpURLConnection con = (HttpURLConnection)new URL(path).openConnection();

        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.connect();
        con.getInputStream();
    }

    private void makePayment(Payment payment) throws IOException {
        String path = paymentServiceBaseUrl + "/payment" + trailingElement();
        HttpURLConnection con = (HttpURLConnection)new URL(path).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jPayment = new Gson().toJson(payment);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jPayment.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        con.connect();
        con.getInputStream();
    }

    private double calculateCartPrice(OrderedProduct[] cart){
        return Arrays.stream(cart)
                .map(product -> product.getQuantity() * loadProductPrice(product.getProductId()))
                .reduce(0d, Double::sum);
    }

    private double loadProductPrice(UUID productId){
        //String path = productsServiceBaseUrl + "/products/" + productId + trailingElement();
        //String jproduct = JsonRequestHelper.Get(new URL(path));

        //todo implement this!!!
        return 10;
    }

    private OrderedProduct[] loadCartContent(UUID userId) throws IOException, CartNotFoundException {

        try {
            String path = orderServiceBaseUrl + "/cart?userid=" + userId + trailingElement();
            URL loadCartUrl = new URL(path);
            JsonElement cart = new Gson()
                    .fromJson(JsonRequestHelper.Get(loadCartUrl), JsonObject.class)
                    .get("orderedProducts");

            return new Gson().fromJson(cart, OrderedProduct[].class);
        }catch(FileNotFoundException e){
            throw new CartNotFoundException();
        }
    }

    private String trailingElement(){
        if(_addTrailingSlashes) return "/";
        return "";
    }
}
