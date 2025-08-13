package com.the.mudpot.controller;

import com.the.mudpot.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping
    public Map<String, String> getStatus() {
        return statisticService.getStatus();
    }

    @RequestMapping("/chart")
    public Map<String, Integer> getStatistics() {
        return statisticService.getStatistics();
    }
}
