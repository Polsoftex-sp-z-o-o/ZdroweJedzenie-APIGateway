package com.gateway.services;

import com.gateway.dto.Card;
import com.gateway.dto.OrderedProduct;
import com.gateway.dto.Payment;
import com.gateway.dto.ProductPrice;
import com.gateway.exceptions.CartNotFoundException;
import com.gateway.helpers.JsonRequestHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
        ProductPrice[] prices = loadProductPrices(Arrays.asList(products));

        double price = Arrays.stream(prices)
                .map(ProductPrice::getPrice)
                .reduce(0d, Double::sum);

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

    private ProductPrice[] loadProductPrices(List<OrderedProduct> products) throws IOException {
            StringBuilder path = new StringBuilder(productsServiceBaseUrl + "/products/ids?");

            for(OrderedProduct product : products){
                for (int i = 0; i < product.getQuantity(); i++) {
                    path.append("ids=");
                    path.append(product.getProductId().toString());
                    path.append("&");
                }
            }

            path.append(trailingElement());

            URL loadCartUrl = new URL(path.toString());
            String jsonString = JsonRequestHelper.Get(loadCartUrl);

            return new Gson().fromJson(jsonString, ProductPrice[].class);
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
