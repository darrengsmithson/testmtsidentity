package com.mts.identity.testrespond.testrespond.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping(path = "/testresponse")
public class RespondService {

    @GetMapping
    public String getResponse() {
        return "from service B";
    }

    @GetMapping("/A")
    public String getResponseFromService() throws Exception {

        //msiks();
        //Mono<AccessToken> token = getAzureToken();

        String uri = "/testrequest";
        String resp = getWebClient().get()
            .uri(uri)
            //.headers(h -> h.setBearerAuth(token.block().getToken()))
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return resp + " from calling service A";
    }

    public WebClient getWebClient() {
        WebClient wc = WebClient.create("https://testrequest-1612878461242.azurewebsites.net");
        return wc;
    }


}
