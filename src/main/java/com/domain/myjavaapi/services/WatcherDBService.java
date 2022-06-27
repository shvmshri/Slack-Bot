package com.domain.myjavaapi.services;

import com.domain.myjavaapi.config.MongoTemplateFactory;
import com.domain.myjavaapi.models.JenkinsJobInfo;
import com.domain.myjavaapi.models.Watcher;
import com.mongodb.client.result.DeleteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WatcherDBService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatcherDBService.class);

    @Autowired
    private MongoTemplateFactory templateFactory;

    public ArrayList<String> searchWatcherUserIds(JenkinsJobInfo job) throws Exception {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHARTNAME).is(job.getChartName()), Criteria.where(Watcher.RELEASENAME).is(job.getReleaseName()));

        Query query = new Query(criteria);
        query.fields().include(Watcher.USERID);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate();

        List<Watcher> watcherList = mongoTemplate.find(query, Watcher.class, "Watcher");
        ArrayList<String> userIds = new ArrayList<String>();
        for (Watcher watcher : watcherList) {
            userIds.add(watcher.getUserId());
        }

        return userIds;
    }

    public void addWatcherInfo(String chart, String release, String time, String userId, String userEmail) throws Exception {

        Watcher watcher = new Watcher(chart, release, time, userId, userEmail);

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHARTNAME).is(watcher.getChartName()), Criteria.where(Watcher.RELEASENAME).is(watcher.getReleaseName()));
        criteria = criteria.and(Watcher.USERID).is(watcher.getUserId());

        Query query = new Query(criteria);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate();

        FindAndReplaceOptions options = new FindAndReplaceOptions().upsert().returnNew();
        mongoTemplate.findAndReplace(query, watcher, options, Watcher.class, "Watcher");

    }

    public boolean removeWatcherInfo(String chart, String release, String userId) throws Exception {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHARTNAME).is(chart), Criteria.where(Watcher.RELEASENAME).is(release));
        criteria = criteria.and(Watcher.USERID).is(userId);

        Query query = new Query(criteria);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate();

        DeleteResult result = mongoTemplate.remove(query, Watcher.class, "Watcher");
        if (result.getDeletedCount() == 0) {
            return false;
        }
        return true;

    }

    //When someone Asks for number of watchers
    public List<Watcher> UsersListInfo(String chart, String release, String userId) throws Exception {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHARTNAME).is(chart), Criteria.where(Watcher.RELEASENAME).is(release));

        Query query = new Query(criteria);
        query.fields().include(Watcher.USEREMAIL);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate();

        List<Watcher> watcherList = mongoTemplate.find(query, Watcher.class, "Watcher");
        return watcherList;
    }

}