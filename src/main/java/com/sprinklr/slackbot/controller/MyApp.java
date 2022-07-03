package com.sprinklr.slackbot.controller;

import com.google.gson.Gson;
import com.sprinklr.slackbot.dto.SlackExternalSource;
import com.sprinklr.slackbot.dto.SlackRequestPayload;
import com.sprinklr.slackbot.service.SlackWatcherAppService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@ApplicationPath("/api")
@Path("/slack/events")

@Service
public class MyApp extends Application {

    private static final String PAYLOAD = "payload";
    private static final Gson gson = new Gson();


    @POST
    @Path("/load")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

    public SlackExternalSource load(MultivaluedMap<String, String> formParams) {
        SlackRequestPayload slackRequestPayload = gson.fromJson(formParams.getFirst(PAYLOAD), SlackRequestPayload.class);
        ApplicationContext context = new AnnotationConfigApplicationContext("com.sprinklr.slackbot");
        SlackWatcherAppService slackWatcherAppService = context.getBean(SlackWatcherAppService.class);
        return slackWatcherAppService.getSlackObject(slackRequestPayload.getActionId(), slackRequestPayload.getValue(),
                slackRequestPayload.getPrivateMetadata());
    }

}
