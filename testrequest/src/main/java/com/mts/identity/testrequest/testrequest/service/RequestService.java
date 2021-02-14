package com.mts.identity.testrequest.testrequest.service;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Im5PbzNaRHJPRFhFSzFqS1doWHNsSFJfS1hFZyIsImtpZCI6Im5PbzNaRHJPRFhFSzFqS1doWHNsSFJfS1hFZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC85OTgwNDY1OS00MzFmLTQ4ZmEtODRjMS02NWM5NjA5ZGUwNWIvIiwiaWF0IjoxNjEzMzM1MjYxLCJuYmYiOjE2MTMzMzUyNjEsImV4cCI6MTYxMzMzOTE2MSwiYWNyIjoiMSIsImFpbyI6IkFWUUFxLzhUQUFBQWpIRGxtVWpvSXAwL0tHOWpsK2RWa2tOc1pCbWg5QXZ5QlJVMCtRcTRBcFloYnoyOW1FbTJ6d3hpdmJDSUNid3ZLMmpueTR0VEtBc2duenVYdjUrUml3NzQ0b0ovT1Y4QVc5cFZPbm9uS3owPSIsImFtciI6WyJwd2QiLCJtZmEiXSwiYXBwaWQiOiJjNDRiNDA4My0zYmIwLTQ5YzEtYjQ3ZC05NzRlNTNjYmRmM2MiLCJhcHBpZGFjciI6IjIiLCJncm91cHMiOlsiZmE4ZDc1MWMtMTc0Ny00ZWRlLWI5MWMtYjliM2YyNjIwNmE0IiwiMTc2MmE4Y2EtNjc3OC00ZTNhLTk3MWUtZjA2NzFiMzNjOTE0Il0sImlwYWRkciI6IjgyLjI3LjE2Ny4xNSIsIm5hbWUiOiJEYXJyZW4iLCJvaWQiOiJhOGVmZWJjOS1hNGFmLTRjMjEtYTJlYy0zZGVmMDVkYWNiNmMiLCJwdWlkIjoiMTAwMzIwMDEwMjA1RkI5OSIsInJoIjoiMC5BQUFBV1VhQW1SOUQta2lFd1dYSllKM2dXNE5BUzhTd084Rkp0SDJYVGxQTDN6eDVBTE0uIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoiMDF6TFlGZzZFTXVpalRhQjBaYllCNHA3c1VxQXFNdEw4cjZ1WDB5MWs5TSIsInRpZCI6Ijk5ODA0NjU5LTQzMWYtNDhmYS04NGMxLTY1Yzk2MDlkZTA1YiIsInVuaXF1ZV9uYW1lIjoiZGFycmVuLnNtaXRoc29uQG10c25kcGgub25taWNyb3NvZnQuY29tIiwidXBuIjoiZGFycmVuLnNtaXRoc29uQG10c25kcGgub25taWNyb3NvZnQuY29tIiwidXRpIjoib2ZJeXRqdDEzVVdWOE1fT2VLTmdBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiOWI4OTVkOTItMmNkMy00NGM3LTlkMDItYTZhYzJkNWVhNWMzIl0sInhtc190Y2R0IjoxNjA3NDQyNDQwfQ.eJjwuoWdLHitmNDc9lgfdvixmuWnnwwLntMlXsJSLZPm0icpBq0TT0b2fvYBioCLSF5cxj99Wxi9raAx5K2eHnnQuCM8aYTAVvbXVCfS4dpzvViqjUNsrTC4rdPl3ZG5qQRi83lyjIyxGDT8QuGC_YXQk23gKX4CC8LJirD134vA4vzpa_Ox6pr4l2MYS7hwgy1pNJAiytyCCq3JOYnEODNTt_Xx7OKDsT6H6rgd8YI7iwsBfXQy7vTUAOLe-z1Pj3ofG33esOnLku-bLPauqIYl5p9eOVWpiDqNokcrNZhDMnb0_UmWiwf3qZojYLvD9BNdOqiFE4KH_rEdqbgqIw";


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
    public String getTokenFromServiceUsingURL() {

        String token = getTokenFromURL();

        return "token from url - " + token;
    }

    @GetMapping("/testchange")
    public String getTestChange() {

        return "test change 4";
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

    private String getTokenFromURL()
    {
        try
        {
            URL url = new URL("http://169.254.169.254/metadata/identity/oauth2/token?resource=https://management.azure.com/&api-version%3D=2018-02-01");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("metadata", "true");
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
