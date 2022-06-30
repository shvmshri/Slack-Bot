package com.domain.myjavaapi.objectFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChartReleaseDataFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChartReleaseDataFactory.class);
    private static final String ENDPOINT = "https://qa4-red-int.sprinklr.com/internal/api/v1/fetchModulesInfoForGivenRepo";
    private static final String HEADER = "X-Red-Api-Token";
    private static final String TOKEN = "token";
    private static final String K8S = "k8s";
    private static final String PARAM_NAME = "repo";
    private static final ConcurrentHashMap<String, List<String>> chartReleaseMappings = new ConcurrentHashMap<String, List<String>>();
    private static List<String> repositryNames = new ArrayList<>(Arrays.asList("Sprinklr Main App"));


    private void storeDataInMap(String chartReleaseInfo) {
        try {
            // Just to test
            // chartReleaseInfo = "{\"chart1\":{\"k8s\":[\"release11\",\"release12\"]},\"chart2\":{\"k8s\":[\"release21\",\"release22\"]}}";
            Map<String, Map<String, List<String>>> map = new Gson().fromJson(chartReleaseInfo, new TypeToken<HashMap<String, Map<String, List<String>>>>() {
            }.getType());
            for (Map.Entry<String, Map<String, List<String>>> entry : map.entrySet()) {
                chartReleaseMappings.put(entry.getKey(), entry.getValue().get(K8S));
            }
        } catch (Exception e) {
            LOGGER.error("[ChartReleaseDataCache_SEVERE] Error while using GSON to convert JSON string to map");
        }
    }

    private HttpRequestBase getRequestwithParam(String paramValue) {

        HttpRequestBase request = new HttpGet();
        request.setHeader(HEADER, TOKEN);

        try {
            URIBuilder uriBuilder = new URIBuilder(ENDPOINT);
            uriBuilder.addParameter(PARAM_NAME, paramValue);
            URI uri = uriBuilder.build();
            assert request != null;
            request.setURI(uri);
        } catch (Exception e) {
            LOGGER.error("[ChartReleaseDataFactory_SEVERE] Error occurred due to the attempt of using invalid URL to fetch data about chartNames and ReleaseNames for the repository = " + paramValue);
        }

        return request;
    }

    private void fetchChartReleaseData() {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        for (String repo : repositryNames) {
            HttpRequestBase request = getRequestwithParam(repo);
            try {
                HttpResponse httpResponse = httpclient.execute(request);
                String result = EntityUtils.toString(httpResponse.getEntity());
                storeDataInMap(result);
            } catch (Exception e) {
                LOGGER.error("[ChartReleaseDataCache_CRITICAL] Error occurred while executing request of fetching data from red API regarding chart and release details for repositry " + repo);
            }

        }

        try {
            httpclient.close();
        } catch (Exception e) {
            LOGGER.error("[ChartReleaseDataCache_SEVERE] Error occurred while closing Http Client");
        }

    }

    public boolean validateChartRelease(String chart, String release) {
        chartReleaseMappings.clear();
        fetchChartReleaseData();
        if (chartReleaseMappings.containsKey(chart)) {
            return chartReleaseMappings.get(chart).contains(release);
        } else {
            return false;
        }
    }

}