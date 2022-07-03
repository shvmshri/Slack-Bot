package com.sprinklr.slackbot.factory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sprinklr.slackbot.util.AppProperties;
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
    private static final String PARAM_NAME = "repo";
    private static List<String> repositryNames = new ArrayList<>(Arrays.asList("Sprinklr Main App"));


    private HttpRequestBase getRequestwithParam(String paramValue) {

        HttpRequestBase request = new HttpGet();
        request.setHeader(HEADER, AppProperties.RED_INTERNAL_API_TOKEN);

        try {
            URIBuilder uriBuilder = new URIBuilder(ENDPOINT);
            uriBuilder.addParameter(PARAM_NAME, paramValue);
            URI uri = uriBuilder.build();
            assert request != null;
            request.setURI(uri);
        } catch (Exception e) {
            LOGGER.error("[ChartReleaseDataFactory_SEVERE] Error occurred due to the attempt of using invalid URL to fetch data about chartNames and ReleaseNames for the repository = " + paramValue, e);
        }

        return request;
    }

    public String fetchChartReleaseData() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = null;
        for (String repo : repositryNames) {
            HttpRequestBase request = getRequestwithParam(repo);
            try {
                HttpResponse httpResponse = httpclient.execute(request);
                result = EntityUtils.toString(httpResponse.getEntity());
                //storeDataInMap(result);
            } catch (Exception e) {
                LOGGER.error("[ChartReleaseDataCache_CRITICAL] Error occurred while executing request of fetching data from red API regarding chart and release details for repositry " + repo, e);
            }

        }

        try {
            httpclient.close();
        } catch (Exception e) {
            LOGGER.error("[ChartReleaseDataCache_SEVERE] Error occurred while closing Http Client", e);
        }
        return result;
    }


}