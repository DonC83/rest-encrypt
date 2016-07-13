package com.donc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by donovan on 6/07/2016.
 */
@RequestMapping(path = "/rea")
public class REAController {

    @RequestMapping(method = RequestMethod.POST, consumes = "text/html")
    public ResponseEntity post(String content) {
        System.out.println(content);
        return null;
    }

}
