package com.okhttp;


import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class TestApi 
{
    public static OkHttpClient client;
    public static ObjectMapper mapper;
    public String BASE_URL="https://reqres.in/";

    @BeforeClass
    public void beforeClass()
    {
        client=new OkHttpClient();
        mapper=new ObjectMapper();
    }
    @Test(priority = 0)
    public void get_Request() throws IOException
    {
        Request request=new Request.Builder().url(BASE_URL+"api/users?page=2").build();
        Call call=client.newCall(request);
        Response response=call.execute();
        Assert.assertTrue(response.code()==200," Response code should be 200");
        
        JsonNode jn = mapper.readTree(response.body().string());
        System.out.println(jn.toPrettyString());
        Assert.assertTrue(jn.get("data").get(0).get("first_name").asText().equalsIgnoreCase("Michael"), "Checking first name");
        Assert.assertTrue(jn.get("data").get(0).get("last_name").asText().equalsIgnoreCase("Lawson"), "Checking last name");
        
    }
    @Test(priority = 1)
    public void get_RequestUsingQueryParameter() throws IOException
    {
        HttpUrl.Builder urlbuilder=HttpUrl.parse(BASE_URL+"api/users/").newBuilder();
        urlbuilder.addQueryParameter("id", "2");
        String url=urlbuilder.build().toString();

        Request request =new Request.Builder().url(url).build();
        Call call=client.newCall(request);
        Response response=call.execute();

        Assert.assertTrue(response.code()==200);
        System.out.println(response.code());
        JsonNode jn=mapper.readTree(response.body().string());
        Assert.assertTrue(jn.get("data").get("id").asText().equals("2"));
        System.out.println(jn.get("data").get("id").asText());
        Assert.assertTrue(jn.get("data").get("email").asText().equalsIgnoreCase("janet.weaver@reqres.in"));
        System.out.println(jn.get("data").get("email"));
        
    }
    @Test(priority = 2)
    public void post_Request() throws IOException
    {
        RequestBody formbody=new FormEncodingBuilder().add("name", "morpheus").add("job", "leader").build();
        Request request=new Request.Builder().url(BASE_URL+"api/users").post(formbody).build();
        Call call=client.newCall(request);
        Response response=call.execute();
        System.out.println(response.code());
        Assert.assertTrue(response.code()==201);
    }
}
