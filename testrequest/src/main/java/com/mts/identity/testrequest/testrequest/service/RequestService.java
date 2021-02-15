package com.mts.identity.testrequest.testrequest.service;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

@RestController
@RequestMapping(path = "/testrequest")
public class RequestService {

    @GetMapping
    public String getResponse() {
        return "from service A";
    }

    @GetMapping("/B")
    public String getResponseFromService() {

        //String token = getAzureToken();
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Im5PbzNaRHJPRFhFSzFqS1doWHNsSFJfS1hFZyIsImtpZCI6Im5PbzNaRHJPRFhFSzFqS1doWHNsSFJfS1hFZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC85OTgwNDY1OS00MzFmLTQ4ZmEtODRjMS02NWM5NjA5ZGUwNWIvIiwiaWF0IjoxNjEzMzM2MjQ5LCJuYmYiOjE2MTMzMzYyNDksImV4cCI6MTYxMzM0MDE0OSwiYWNyIjoiMSIsImFpbyI6IkFWUUFxLzhUQUFBQVR2TG1ZdzNyOTBUMmNsbERHV2dkM2JnZHkxaXZaSzZBK0NvVW5SRXRHbFJ0VFBpRjZDN0VtZzFqbThGbUxSVmdEbkZIN2Z4Nll2MjduWjN2bktxT1A3YmdYaFhIWmdSa210QytDNXB3TTBzPSIsImFtciI6WyJwd2QiLCJtZmEiXSwiYXBwaWQiOiJjNDRiNDA4My0zYmIwLTQ5YzEtYjQ3ZC05NzRlNTNjYmRmM2MiLCJhcHBpZGFjciI6IjIiLCJncm91cHMiOlsiZmE4ZDc1MWMtMTc0Ny00ZWRlLWI5MWMtYjliM2YyNjIwNmE0IiwiMTc2MmE4Y2EtNjc3OC00ZTNhLTk3MWUtZjA2NzFiMzNjOTE0Il0sImlwYWRkciI6IjgyLjI3LjE2Ny4xNSIsIm5hbWUiOiJEYXJyZW4iLCJvaWQiOiJhOGVmZWJjOS1hNGFmLTRjMjEtYTJlYy0zZGVmMDVkYWNiNmMiLCJwdWlkIjoiMTAwMzIwMDEwMjA1RkI5OSIsInJoIjoiMC5BQUFBV1VhQW1SOUQta2lFd1dYSllKM2dXNE5BUzhTd084Rkp0SDJYVGxQTDN6eDVBTE0uIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoiMDF6TFlGZzZFTXVpalRhQjBaYllCNHA3c1VxQXFNdEw4cjZ1WDB5MWs5TSIsInRpZCI6Ijk5ODA0NjU5LTQzMWYtNDhmYS04NGMxLTY1Yzk2MDlkZTA1YiIsInVuaXF1ZV9uYW1lIjoiZGFycmVuLnNtaXRoc29uQG10c25kcGgub25taWNyb3NvZnQuY29tIiwidXBuIjoiZGFycmVuLnNtaXRoc29uQG10c25kcGgub25taWNyb3NvZnQuY29tIiwidXRpIjoiQ3hja05fRFpyVXE5Q2pzcGROMThBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiOWI4OTVkOTItMmNkMy00NGM3LTlkMDItYTZhYzJkNWVhNWMzIl0sInhtc190Y2R0IjoxNjA3NDQyNDQwfQ.X4CitigylWkmWvIKO9-DKFSSPAWsgOnFRxYrMWs_W6-Evlz7xsvZsW2R-WMTVia3rjQKEmhXCFpAXuvBo2ESs4kOovyuRQRJ9b62aRHLSvnV6XemIhGHFIPLAhHYT6wgUe0XKH8EV1E3Oef6iGPxc_G3s64WKgnaYc688qp4r8N4whg45LwJNxLuSrGSxA0AKE5jMlbm0Lqi8PFo0ODcFzJvaE6iqlYs9ZtnAAurdf7eVc8mP_ajF1-OwY6UcRKsy5ET3gDw5p8Ef1h2HH33FdV8X--h3vBWI_O08pVdDmZqDIGnP_SpSIdcDJhdjlpszLVMlCHtH3L6HPMHUUkiMg";

        String uri = "/testresponse";
        String resp = getWebClient().get()
            .uri(uri)
            .headers(h -> h.setBearerAuth(token))
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return resp + " from calling service B";
    }

    @GetMapping("/C")
    public String getResponseFromServiceUsingURL() {

        String token = getTokenFromURL();

        String uri = "/testresponse";
        String resp = getWebClient().get()
            .uri(uri)
            .headers(h -> h.setBearerAuth(token))
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return resp + " from calling service B";
    }

    @GetMapping("/D")
    public String getTokenFromService() {

        String token = getAzureToken();

        return "token from managed identity - " + token;
    }

    @GetMapping("/E")
    public String getTokenFromServiceUsingURL() throws Exception {

        String token = getTokenFromURL();

        return "token from url - " + token;
    }

    @GetMapping("/F")
    public String getTESTTokenFromServiceUsingURL() {

        String token = getTestTokenFromURL();

        return "TEST OUTPUT from url - " + token;
    }

    @GetMapping("/testchange")
    public String getTestChange() {

        return "test change 6";
    }

    public WebClient getWebClient() {
        WebClient wc = WebClient.create("https://testrespond-1612865660384.azurewebsites.net");
        return wc;
    }

    private String getAzureToken() {
        ManagedIdentityCredential credential = new ManagedIdentityCredentialBuilder()
                                            .clientId("89ead37f-fc46-42bc-94a5-c328d48c2d77")
                                            .build();

        TokenRequestContext trc = new TokenRequestContext();
        trc.setScopes(Arrays.asList("api://810b6f10-0703-48b3-90f9-bb80133181d4/Files.Read"));

        AccessToken at = credential.getToken(trc).block();

        String accessToken = at.getToken();

        System.out.println("Token is - " + accessToken);

        return accessToken;

    }

    private String getTokenFromURL() throws Exception {
        URL msiEndpoint = new URL("http://169.254.169.254/metadata/identity/oauth2/token?api-version=2018-02-01&resource=https://management.azure.com/");
        HttpURLConnection con = (HttpURLConnection) msiEndpoint.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Metadata", "true");

        if (con.getResponseCode()!=200) {
            throw new Exception("Error calling managed identity token endpoint.");
        }

        InputStream responseStream = con.getInputStream();

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(responseStream);

        String ret = "";

        while(!parser.isClosed()){
            JsonToken jsonToken = parser.nextToken();

            if(JsonToken.FIELD_NAME.equals(jsonToken)){
                String fieldName = parser.getCurrentName();
                jsonToken = parser.nextToken();

                if("access_token".equals(fieldName)){
                    String accesstoken = parser.getValueAsString();
                    ret = accesstoken.substring(0,5)+ "..." + accesstoken.substring(accesstoken.length()-5);
                }
            }
        }

        return ret;

    }

    private String getTestTokenFromURL()
    {
        try
        {

            URL url = new URL("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=2b1fd2d7f77ccf1b7de9b441571b39b8");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Metadata", "true");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return response.toString();
        }
        catch(Exception ex)
        {
            return "ex is -  " + ex.getMessage();
        }
    }

}
