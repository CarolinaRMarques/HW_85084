package com.tqs_assignment.airquality.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HtmlController {
    @RequestMapping(value = "/")
    public static String home() {
        return "index";
    }

    @RequestMapping(value = "/coord")
    public static String coord() {
        return "coord";
    }

    @RequestMapping(value = "/air")
    public static String air() {
        return "airquality";
    }

    @RequestMapping(value = "/hourly")
    public static String hours() {
        return "hourly";
    }
}
