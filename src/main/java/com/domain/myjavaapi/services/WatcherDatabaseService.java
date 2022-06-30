package com.domain.myjavaapi.services;

import com.domain.myjavaapi.enums.ServerType;
import com.domain.myjavaapi.models.Watcher;
import com.domain.myjavaapi.objectFactory.MongoTemplateFactory;
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
public class WatcherDatabaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatcherDatabaseService.class);

    @Autowired
    private MongoTemplateFactory templateFactory;

    public ArrayList<String> getWatcherUserIds(String chart, String release) throws Exception {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHART_NAME).is(chart), Criteria.where(Watcher.RELEASE_NAME).is(release));

        Query query = new Query(criteria);
        query.fields().include(Watcher.USER_ID);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate(ServerType.SLACK_BOT);

        List<Watcher> watcherList = mongoTemplate.find(query, Watcher.class, Watcher.COLLECTION);
        ArrayList<String> userIds = new ArrayList<String>();
        for (Watcher watcher : watcherList) {
            userIds.add(watcher.getUserId());
        }

        return userIds;
    }

    public void addWatcherInfo(String chart, String release, String time, String userId, String userEmail) throws Exception {

        Watcher watcher = new Watcher(chart, release, time, userId, userEmail);

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHART_NAME).is(watcher.getChartName()), Criteria.where(Watcher.RELEASE_NAME).is(watcher.getReleaseName()));
        criteria = criteria.and(Watcher.USER_ID).is(watcher.getUserId());

        Query query = new Query(criteria);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate(ServerType.SLACK_BOT);

        FindAndReplaceOptions options = new FindAndReplaceOptions().upsert().returnNew();
        mongoTemplate.findAndReplace(query, watcher, options, Watcher.class, Watcher.COLLECTION);

    }

    public boolean removeWatcherInfo(String chart, String release, String userId) throws Exception {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHART_NAME).is(chart), Criteria.where(Watcher.RELEASE_NAME).is(release));
        criteria = criteria.and(Watcher.USER_ID).is(userId);

        Query query = new Query(criteria);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate(ServerType.SLACK_BOT);

        DeleteResult result = mongoTemplate.remove(query, Watcher.class, Watcher.COLLECTION);
        return (result.getDeletedCount() != 0);

    }

    //When someone Asks for number of watchers
    public List<Watcher> getWatcherUserEmails(String chart, String release, String userId) throws Exception {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHART_NAME).is(chart), Criteria.where(Watcher.RELEASE_NAME).is(release));

        Query query = new Query(criteria);
        query.fields().include(Watcher.USER_EMAIL);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate(ServerType.SLACK_BOT);

        return mongoTemplate.find(query, Watcher.class, Watcher.COLLECTION);

    }

    public boolean searchAWatcher(String chart, String release, String userId) throws Exception {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHART_NAME).is(chart), Criteria.where(Watcher.RELEASE_NAME).is(release));
        criteria = criteria.and(Watcher.USER_ID).is(userId);

        Query query = new Query(criteria);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate(ServerType.SLACK_BOT);
        Watcher watcher = mongoTemplate.findOne(query, Watcher.class, Watcher.COLLECTION);

        return (watcher != null);

    }

}