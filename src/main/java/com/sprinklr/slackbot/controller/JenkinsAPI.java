package com.sprinklr.slackbot.controller;

import com.sprinklr.slackbot.bean.JenkinsJobInfo;
import com.sprinklr.slackbot.service.JenkinsService;
import com.sprinklr.slackbot.util.WatcherAppMessageConstants;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@ApplicationPath("/api")
@Path("/jenkins")
@Service
public class JenkinsAPI extends Application {

    @POST
    @Path("/on-job-trigger")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response onJobTrigger(JenkinsJobInfo jobDetails) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.sprinklr.slackbot");
        JenkinsService jenkinsService = ctx.getBean(JenkinsService.class);

        jenkinsService.handleJobTrigger(jobDetails);
        return Response.ok().entity(WatcherAppMessageConstants.THANK_YOU).build();

    }

}