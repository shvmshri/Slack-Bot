package com.sprinklr.slackbot.factory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sprinklr.slackbot.util.AppProperties;
import com.sprinklr.slackbot.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChartReleaseDataFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChartReleaseDataFactory.class);
    private static final String ENDPOINT = "https://qa4-red-int.sprinklr.com/internal/api/v1/fetchModulesInfoForGivenRepo";
    private static final String HEADER = "X-Red-Api-Token";
    private static final String K8S = "k8s";
    private static final String PARAM_NAME = "repo";

    private static ConcurrentHashMap<String, Map<String, Map<String, ArrayList<String>>>> repoModuleInfoCache = new ConcurrentHashMap<>();

    //same list of repositories as provided in WatcherApputil
//    private static List<String> repositryNames = new ArrayList<>(Arrays.asList("Sprinklr Main App"));


    private void storeDataInMap(String repo, String chartReleaseInfo) {
        try {
            // Just to test
            // chartReleaseInfo = "{\"chart1\":{\"k8s\":[\"release11\",\"release12\"]},\"chart2\":{\"k8s\":[\"release21\",\"release22\"]}}";
            Map<String, Map<String, ArrayList<String>>> map = new Gson().fromJson(chartReleaseInfo, new TypeToken<HashMap<String, Map<String, List<String>>>>() {
            }.getType());
            repoModuleInfoCache.put(repo, map);
        } catch (Exception e) {
            LOGGER.error("[ChartReleaseDataCache_SEVERE] Error while using GSON to convert JSON string to map", e);
        }
    }

    private void fetchChartReleaseData(String repo) {
        try {
            URL url = new URL(ENDPOINT);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            //Set URL parameters
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(getParamsString(PARAM_NAME, repo));
            out.flush();
            out.close();

            //Set Header
            con.setRequestProperty(HEADER, AppProperties.RED_INTERNAL_API_TOKEN);

            //Response
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try {
                    String result = responseStringBody(con);
                    System.out.println(result);
                    storeDataInMap(repo, result);
                } catch (Exception e) {
                    LOGGER.error("[ChartReleaseDataFactory_SEVERE] Error while reading response from the request");
                }

            } else {
                LOGGER.error("[ChartReleaseDataFactory_SEVERE] Invalid response code receieved while fetching data");
            }

            //Close Connection
            con.disconnect();


        } catch (Exception e) {
            LOGGER.error("[ChartReleaseDataFactory_SEVERE] Error occurred due while fetching Chart,Release information", e);
        }


    }

    private String responseStringBody(HttpURLConnection con) throws Exception {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        return content.toString();

    }

    private String getParamsString(String param, String paramValue) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();


        result.append(URLEncoder.encode(param, "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(paramValue, "UTF-8"));


        return result.toString();

    }

    public ArrayList<String> getChartNames(String repo) {

        if (!repoModuleInfoCache.containsKey(repo)) {
            fetchChartReleaseData(repo);
//            storeDataInMap("Sprinklr Main App", "{\"chart1\":{\"k8s\":[\"release11\",\"release12\"]},\"chart2\":{\"k8s\":[\"release21\",\"release22\"]}}");
//            storeDataInMap("Spinklr custom helloo", "{\"chart3\":{\"k8s\":[\"release31\",\"release32\"]},\"chart4\":{\"k8s\":[\"release41\",\"release42\"]}}");
        }

        Map<String, Map<String, ArrayList<String>>> chartReleaseMap = repoModuleInfoCache.get(repo);
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> chartNames = Utils.fromJson(chartReleaseMap.keySet().toString(), listType);


        Collections.sort(chartNames);
        return chartNames;


    }

    public ArrayList<String> getReleaseNames(String repo, String chart) {
        if (!repoModuleInfoCache.containsKey(repo)) {
            fetchChartReleaseData(repo);
//            storeDataInMap("Sprinklr Main App", "{\"chart1\":{\"k8s\":[\"release11\",\"release12\"]},\"chart2\":{\"k8s\":[\"release21\",\"release22\"]}}");
//            storeDataInMap("Spinklr custom helloo", "{\"chart3\":{\"k8s\":[\"release31\",\"release32\"]},\"chart4\":{\"k8s\":[\"release41\",\"release42\"]}}");

        }

        Map<String, Map<String, ArrayList<String>>> chartReleaseMap = repoModuleInfoCache.get(repo);
        ArrayList<String> releaseNames = chartReleaseMap.get(chart).get(K8S);

        Collections.sort(releaseNames);
        return releaseNames;

    }

    public void refreshModuleInfo(String repo) {
        repoModuleInfoCache.clear();
        fetchChartReleaseData(repo);
//        storeDataInMap("Sprinklr Main App", "{\"chart1\":{\"k8s\":[\"release11\",\"release12\"]},\"chart2\":{\"k8s\":[\"release21\",\"release22\"]}}");
//        storeDataInMap("Spinklr custom helloo", "{\"chart3\":{\"k8s\":[\"release31\",\"release32\"]},\"chart4\":{\"k8s\":[\"release41\",\"release42\"]}}");

    }


}