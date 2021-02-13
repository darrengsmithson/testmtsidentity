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

    public WebClient getWebClient() {
        WebClient wc = WebClient.create("https://testrespond-1612865660384.azurewebsites.net");
        return wc;
    }

    private String getAzureToken() {
        ManagedIdentityCredential credential = new ManagedIdentityCredentialBuilder()
                                            .clientId("880d2a32-d5a4-499e-b277-de9727be52bd")
                                            .build();

        TokenRequestContext trc = new TokenRequestContext();
        trc.setScopes(Arrays.asList("Files.Read"));

        AccessToken at = credential.getToken(trc).block();

        String accessToken = at.getToken();

        System.out.println("Token is - " + accessToken);

        return accessToken;

    }

}
