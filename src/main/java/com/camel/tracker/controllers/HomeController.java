package com.camel.tracker.controllers;

import com.camel.tracker.models.LocationsStats;
import com.camel.tracker.services.CoronavirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CoronavirusDataService coronavirusDataService;

    //return template(front end page) name
    @GetMapping("/covid")
    public String covid(Model model) {
        List<LocationsStats> allStats = coronavirusDataService.getAllStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();
        int totalCasesNJ=allStats.stream().filter(stat -> stat.getState().equals("New Jersey")).mapToInt(stat->stat.getLatestTotalCases()).sum();
        int totalNewCasesNJ = allStats.stream().filter(stat -> stat.getState().equals("New Jersey")).mapToInt(stat->stat.getDiffFromPreviousDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("totalCasesNJ", totalCasesNJ);
        model.addAttribute("totalNewCasesNJ", totalNewCasesNJ);

        return "covid";
    }



    @GetMapping("/covidStaticBetterLook")
    public String covidStaticBetterLook(Model model) {
        List<LocationsStats> allStats = coronavirusDataService.getAllStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();
        int totalCasesNJ=allStats.stream().filter(stat -> stat.getState().equals("New Jersey")).mapToInt(stat->stat.getLatestTotalCases()).sum();
        int totalNewCasesNJ = allStats.stream().filter(stat -> stat.getState().equals("New Jersey")).mapToInt(stat->stat.getDiffFromPreviousDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("totalCasesNJ", totalCasesNJ);
        model.addAttribute("totalNewCasesNJ", totalNewCasesNJ);

        return "covidStaticBetterLook";
    }

}
