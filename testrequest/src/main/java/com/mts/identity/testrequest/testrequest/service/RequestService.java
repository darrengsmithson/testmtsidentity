package com.mts.identity.testrequest.testrequest.service;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

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

        String token = getAzureToken();

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

        return "token from url";
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

    private String getTokenFromURL() {
        WebClient wc = WebClient.create("http://169.254.169.254/metadata/identity/oauth2/token?resource=https://management.azure.com/&api-version%3D=2018-02-01");

        String response = null;
        response = wc.get().retrieve().bodyToMono(String.class).block();

        return response;
    }

}
