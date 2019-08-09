/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itta.springrest.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.itta.springrest.Order;
import net.itta.springrest.Produit;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import static org.hamcrest.CoreMatchers.equalTo;
import org.hamcrest.Matchers;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class LesTestsRest {

    @Test
    public void whenGetOrdersRequestUsingRestTemplate_thenCorrect() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Order>> response
                = restTemplate.exchange("http://localhost:8084/ittaspringrest/orders",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
                });
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        List<Order> lo = response.getBody();
        assertTrue("Liste est vide ou nulle", lo != null && !lo.isEmpty());
        assertThat(lo.get(0), Matchers.instanceOf(Order.class));
    }

    @Test
    public void whenGetOrder1RequestUsingRestTemplate_thenCorrect() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Order> response
                = restTemplate.getForEntity("http://localhost:8084/ittaspringrest/orders/1", Order.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Order lo = response.getBody();
        assertThat(lo.getNo(), Matchers.equalTo(1));
    }

    @Test
    public void whenSendGetProduitsForOrder1RequestUsingRestTemplate_thenCorrect() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Produit>> response
                = restTemplate.exchange("http://localhost:8084/ittaspringrest/orders/1/produits",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Produit>>() {
                });
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        List<Produit> lp = response.getBody();
        assertTrue("Liste est vide ou nulle", lp != null && !lp.isEmpty());
        assertThat(lp.get(0), Matchers.instanceOf(Produit.class));

    }

    @Test
    public void whenSendGetProduit1ForOrder1RequestUsingRestTemplate_thenCorrect() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Produit> response
                = restTemplate.getForEntity("http://localhost:8084/ittaspringrest/orders/1/produit/1", Produit.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        Produit lo = response.getBody();
        assertThat(lo.getId(), Matchers.equalTo(1));
    }

    @Test
    public void whenPostProduit1ForOrder1RequestUsingRestTemplate_thenCorrect() {
        RestTemplate restTemplate = new RestTemplate();
        Produit p = new Produit(10, "toto", "toto", 100);
        ResponseEntity<Boolean> response = restTemplate.postForEntity("http://localhost:8084/ittaspringrest/orders/1", p, Boolean.class);
        System.out.println(response.getBody());
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        if (response.getBody() == true) {
            ResponseEntity<Produit> response2
                    = restTemplate.getForEntity("http://localhost:8084/ittaspringrest/orders/1/produit/10", Produit.class);
            assertThat(response2.getStatusCode(), equalTo(HttpStatus.OK));
            Produit lo = response2.getBody();
            assertThat(lo.getId(), Matchers.equalTo(10));
            restTemplate.delete("http://localhost:8084/ittaspringrest/orders/1/produit/10");
        }

        ResponseEntity<Produit> response2
                = restTemplate.getForEntity("http://localhost:8084/ittaspringrest/orders/1/produit/10", Produit.class);
        assertThat(response2.getStatusCode(), equalTo(HttpStatus.OK));
        Produit lo = response2.getBody();
        assertNull("le produit n'a pas été enlevée", lo);

    }

    @Test
    @SuppressWarnings("empty-statement")
    public void whenPatchProduit1ForOrder1RequestUsingRestTemplate_thenCorrect() throws IOException {
        String url = "http://localhost:8084/ittaspringrest/orders/1/produit/1";
        String json = "[{\"nom\":\"toto\"}]";
        HttpResponse httpResponse = executePatch(url, json);
        assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.OK.value()));;
        //si patch return produit
        String jsonreturn = EntityUtils.toString(httpResponse.getEntity());
        Produit lo = new ObjectMapper().readValue(jsonreturn, Produit.class);

        assertThat(lo.getNom(), Matchers.equalTo("toto"));

        //si le patch return void ou int
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Produit> response2
                = restTemplate.getForEntity("http://localhost:8084/ittaspringrest/orders/1/produit/1", Produit.class);
        assertThat(response2.getStatusCode(), equalTo(HttpStatus.OK));
        lo = response2.getBody();
        assertThat(lo.getNom(), Matchers.equalTo("toto"));
    }

    @Test
    public void whenPatchOrder1RequestUsingRestTemplate_thenCorrect() throws MalformedURLException, IOException {
        String url = "http://localhost:8084/ittaspringrest/orders/1";
        String json = "[{\"idClient\":12}]";
        HttpResponse httpResponse = executePatch(url, json);
        assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.OK.value()));;
        String jsonreturn = EntityUtils.toString(httpResponse.getEntity());
        Order lo = new ObjectMapper().readValue(jsonreturn, Order.class);
        assertThat(lo.getIdClient(), Matchers.equalTo(12));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Order> response2
                = restTemplate.getForEntity("http://localhost:8084/ittaspringrest/orders/1", Order.class);
        assertThat(response2.getStatusCode(), equalTo(HttpStatus.OK));
        lo = response2.getBody();
        assertThat(lo.getIdClient(), Matchers.equalTo(12));
    }

    HttpResponse executePatch(String url, String json) throws IOException, UnsupportedCharsetException {
        HttpClient client = HttpClients.createDefault();
        HttpPatch updateRequest = new HttpPatch(url);
        updateRequest.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        HttpResponse httpResponse = client.execute(updateRequest);
        return httpResponse;
    }

}
