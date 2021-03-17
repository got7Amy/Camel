package com.camel.tracker.services;

import com.camel.tracker.models.LocationsStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

//loads data - when app start make call to csv to fetch data
@Service
public class CoronavirusDataService {

    private static String DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_US.csv";

    private List<LocationsStats> allStats = new ArrayList<>();

    public List<LocationsStats> getAllStats() {
        return allStats;
    }

    @PostConstruct
    @Scheduled(cron = "0 5 0 * * *", zone = "UTC")
    public void fetchData() throws IOException, InterruptedException {
        List<LocationsStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        //create the request
        HttpRequest request = HttpRequest.newBuilder()
                //parse URL to URI and input to uri() method
                .uri(URI.create(DATA_URL))
                .build();

        //make request and get response
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString()); //BodyHandlers.ofString() is asking to return response as strings

//        System.out.println(httpResponse.body().toString());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationsStats locationStat = new LocationsStats();
            locationStat.setState(record.get("Province_State"));
            locationStat.setCity(record.get("Admin2"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int previousDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPreviousDay(latestCases - previousDayCases);
            newStats.add(locationStat);
            //System.out.println(locationStat);
        }
        this.allStats = newStats;
    }
}
