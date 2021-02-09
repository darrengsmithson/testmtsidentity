package com.mts.identity.testrespond.testrespond.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/testresponse")
public class RespondService {

    @GetMapping
    public String getResponse() {
        return "from service B";
    }


}
