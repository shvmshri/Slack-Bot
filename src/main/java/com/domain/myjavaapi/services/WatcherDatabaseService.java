package com.domain.myjavaapi.services;

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
        criteria.andOperator(Criteria.where(Watcher.CHARTNAME).is(chart), Criteria.where(Watcher.RELEASENAME).is(release));

        Query query = new Query(criteria);
        query.fields().include(Watcher.USERID);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate();

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
        criteria.andOperator(Criteria.where(Watcher.CHARTNAME).is(watcher.getChartName()), Criteria.where(Watcher.RELEASENAME).is(watcher.getReleaseName()));
        criteria = criteria.and(Watcher.USERID).is(watcher.getUserId());

        Query query = new Query(criteria);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate();

        FindAndReplaceOptions options = new FindAndReplaceOptions().upsert().returnNew();
        mongoTemplate.findAndReplace(query, watcher, options, Watcher.class, Watcher.COLLECTION);

    }

    public boolean removeWatcherInfo(String chart, String release, String userId) throws Exception {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHARTNAME).is(chart), Criteria.where(Watcher.RELEASENAME).is(release));
        criteria = criteria.and(Watcher.USERID).is(userId);

        Query query = new Query(criteria);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate();

        DeleteResult result = mongoTemplate.remove(query, Watcher.class, Watcher.COLLECTION);
        if (result.getDeletedCount() == 0) {
            return false;
        }
        return true;

    }

    //When someone Asks for number of watchers
    public List<Watcher> getWatcherUserEmails(String chart, String release, String userId) throws Exception {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHARTNAME).is(chart), Criteria.where(Watcher.RELEASENAME).is(release));

        Query query = new Query(criteria);
        query.fields().include(Watcher.USEREMAIL);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate();

        List<Watcher> watcherList = mongoTemplate.find(query, Watcher.class, Watcher.COLLECTION);
        return watcherList;
    }

    public boolean searchAWatcher(String chart, String release, String userId) throws Exception {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(Watcher.CHARTNAME).is(chart), Criteria.where(Watcher.RELEASENAME).is(release));
        criteria = criteria.and(Watcher.USERID).is(userId);

        Query query = new Query(criteria);

        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate();
        Watcher watcher = mongoTemplate.findOne(query, Watcher.class, Watcher.COLLECTION);

        if (watcher == null) return false;
        return true;

    }

}