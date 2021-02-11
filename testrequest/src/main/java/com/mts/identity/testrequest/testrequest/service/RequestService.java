package com.mts.identity.testrequest.testrequest.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.nimbusds.oauth2.sdk.GrantType;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/testrequest")
public class RequestService {

    @GetMapping
    public String getResponse() {
        return "from service A";
    }

    @GetMapping("/C")
    public Mono<String> obtainSecuredResource() throws Exception {

        getToken();

        WebClient client = WebClient.builder()
            .build();
        Mono<String> resource = client.post()
            .uri("https://login.microsoftonline.com/99804659-431f-48fa-84c1-65c9609de05b/oauth2/token")
            .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString(("a65892d2-a3ab-4d6b-b946-d7726a903261" + ":" + "b~gqS-QO8ByXvMstj45~-Rbiid1_sx2H1~").getBytes()))
            .body(BodyInserters.fromFormData(OAuth2ParameterNames.GRANT_TYPE, GrantType.CLIENT_CREDENTIALS.getValue()))
            .retrieve()
            .bodyToMono(JsonNode.class)
            .flatMap(tokenResponse -> {
                String accessTokenValue = tokenResponse.get("access_token")
                    .textValue();
                return client.get()
                    .uri("https://testrespond-1612865660384.azurewebsites.net/testresponse")
                    .headers(h -> h.setBearerAuth(accessTokenValue))
                    .retrieve()
                    .bodyToMono(String.class);
            });

        return resource.map(res -> "Retrieved the resource using a manual approach: " + res);

    }

    @GetMapping("/B")
    public String getResponseFromService() throws Exception {

        String token = getToken();

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

    private String getToken() throws Exception {

            String token = null;

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

            tokenCheck:
            while(!parser.isClosed()){
                JsonToken jsonToken = parser.nextToken();

                if(JsonToken.FIELD_NAME.equals(jsonToken)){
                    String fieldName = parser.getCurrentName();
                    jsonToken = parser.nextToken();

                    if("access_token".equals(fieldName)){
                        String accesstoken = parser.getValueAsString();
                        System.out.println("Access Token: " + accesstoken.substring(0,5)+ "..." + accesstoken.substring(accesstoken.length()-5));
                        token = accesstoken.substring(0,5)+ "..." + accesstoken.substring(accesstoken.length()-5);
                        break tokenCheck;
                    }
                }
            }

            return token;
    }


}
