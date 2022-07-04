package com.sprinklr.slackbot.factory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.reflect.TypeToken;
import com.sprinklr.slackbot.util.AppProperties;
import com.sprinklr.slackbot.util.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class ChartReleaseDataFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChartReleaseDataFactory.class);
    private static final String K8S = "k8s";
    private static final String ENDPOINT = "https://qa4-red-int.sprinklr.com/internal/api/v1/fetchModulesInfoForGivenRepo";
    private static final String HEADER = "X-Red-Api-Token";
    private static final String PARAM_NAME = "repo";
    private static LoadingCache<String, Map<String, Map<String, ArrayList<String>>>> repoModuleInfoCache;

    public ChartReleaseDataFactory() {

        repoModuleInfoCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<String, Map<String, Map<String, ArrayList<String>>>>() {
                    @NotNull
                    @Override
                    public Map<String, Map<String, ArrayList<String>>> load(@NotNull String repo) throws Exception {
                        String result = fetchChartReleaseData(repo);
                        Type chartReleaseMapType = new TypeToken<Map<String, Map<String, ArrayList<String>>>>() {
                        }.getType();
                        return Utils.fromJson(result, chartReleaseMapType);
                    }
                });

    }


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

    private String fetchChartReleaseData(String paramValue) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = null;
        HttpRequestBase request = getRequestwithParam(paramValue);
        try {
            HttpResponse httpResponse = httpclient.execute(request);
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            LOGGER.error("[ChartReleaseDataCache_CRITICAL] Error occurred while executing request of fetching data from red API regarding chart and release details for repositry " + paramValue, e);
        }

        try {
            httpclient.close();
        } catch (Exception e) {
            LOGGER.error("[ChartReleaseDataCache_SEVERE] Error occurred while closing Http Client", e);
        }
        return result;

    }

    public ArrayList<String> getChartNames(String repo) {

        try {
            Map<String, Map<String, ArrayList<String>>> chartReleaseMap = repoModuleInfoCache.get(repo);
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> chartNames = Utils.fromJson(chartReleaseMap.keySet().toString(), listType);


            Collections.sort(chartNames);
            return chartNames;
        } catch (Exception e) {
            LOGGER.error("Exception occurred while fetching the module info", e);
            return null;
        }

    }

    public ArrayList<String> getReleaseNames(String repo, String chart) {
        try {
            Map<String, Map<String, ArrayList<String>>> chartReleaseMap = repoModuleInfoCache.get(repo);
            ArrayList<String> releaseNames = chartReleaseMap.get(chart).get(K8S);

            Collections.sort(releaseNames);
            return releaseNames;
        } catch (Exception e) {
            LOGGER.error("Exception occurred while fetching the module info", e);
            return null;
        }

    }

    public void refreshModuleInfo() {

        for (String key : repoModuleInfoCache.asMap().keySet()) {
            repoModuleInfoCache.refresh(key);
        }

    }

}