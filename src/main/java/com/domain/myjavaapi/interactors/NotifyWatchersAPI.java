package com.domain.myjavaapi.interactors;

import com.domain.myjavaapi.models.JenkinsJobInfo;
import com.domain.myjavaapi.services.WatcherSlackService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@ApplicationPath("/api")
@Path("/notify-watchers")

@Service
public class NotifyWatchersAPI extends Application {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response JenkinsHandler(JenkinsJobInfo jobDetails) {
        ApplicationContext ctx =  new AnnotationConfigApplicationContext("com.domain.myjavaapi");
        WatcherSlackService watcherSlackService = ctx.getBean(WatcherSlackService.class);
        watcherSlackService.handleNotifyUsers(jobDetails);
        return Response.ok().entity("Thanks! We received your data").build();
    }

}