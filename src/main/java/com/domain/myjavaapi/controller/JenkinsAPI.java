package com.domain.myjavaapi.controller;

import com.domain.myjavaapi.models.JenkinsJobInfo;
import com.domain.myjavaapi.services.JenkinsService;
import com.domain.myjavaapi.utility.SlackMessageConstants;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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

        ApplicationContext ctx = new AnnotationConfigApplicationContext("com.domain.myjavaapi");
        JenkinsService jenkinsService = ctx.getBean(JenkinsService.class);

        jenkinsService.handleJobTrigger(jobDetails);
        return Response.ok().entity(SlackMessageConstants.THANK_YOU).build();

    }

}