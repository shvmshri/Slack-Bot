package com.domain.myjavaapi.services;


import com.domain.myjavaapi.models.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class WatcherDatabaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatcherDatabaseService.class);

//    @Autowired
//    private MongoTemplateFactory templateFactory;

    ArrayList<Watcher> demoDatabase = new ArrayList<>();

    public List<String> getWatcherUserIds(String chart, String release) throws Exception {

        Predicate<Watcher> chartNameCheck = w -> Objects.equals(w.getChartName(), chart);
        Predicate<Watcher> releaseNameCheck = w -> Objects.equals(w.getReleaseName(), release);

        return demoDatabase.stream().filter(chartNameCheck.and(releaseNameCheck)).map(Watcher::getUserId).collect(Collectors.toList());

//        Criteria criteria = new Criteria();
//        criteria.andOperator(Criteria.where(Watcher.CHART_NAME).is(chart), Criteria.where(Watcher.RELEASE_NAME).is(release));
//
//        Query query = new Query(criteria);
//        query.fields().include(Watcher.USER_ID);
//
////        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate(ServerType.SLACK_BOT);
//
//        List<Watcher> watcherList = mongoTemplate.find(query, Watcher.class, Watcher.COLLECTION);
//        ArrayList<String> userIds = new ArrayList<String>();
//        for (Watcher watcher : watcherList) {
//            userIds.add(watcher.getUserId());
//        }
//
//        return userIds;
    }

    public void addWatcherInfo(String chart, String release, String time, String userId, String userEmail) throws Exception {

        Watcher watcher = new Watcher(chart, release, time, userId, userEmail);

        Predicate<Watcher> chartNameCheck = w -> Objects.equals(w.getChartName(), chart);
        Predicate<Watcher> releaseNameCheck = w -> Objects.equals(w.getReleaseName(), release);
        Predicate<Watcher> userIdCheck = w -> Objects.equals(w.getUserId(), userId);

        demoDatabase.removeIf((chartNameCheck.and(releaseNameCheck)).and(userIdCheck));
        demoDatabase.add(watcher);

//        Criteria criteria = new Criteria();
//        criteria.andOperator(Criteria.where(Watcher.CHART_NAME).is(watcher.getChartName()), Criteria.where(Watcher.RELEASE_NAME).is(watcher.getReleaseName()));
//        criteria = criteria.and(Watcher.USER_ID).is(watcher.getUserId());
//
//        Query query = new Query(criteria);
//
////        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate(ServerType.SLACK_BOT);
//
//        FindAndReplaceOptions options = new FindAndReplaceOptions().upsert().returnNew();
//        mongoTemplate.findAndReplace(query, watcher, options, Watcher.class, Watcher.COLLECTION);

    }

    public boolean removeWatcherInfo(String chart, String release, String userId) throws Exception {

        Predicate<Watcher> chartNameCheck = w -> Objects.equals(w.getChartName(), chart);
        Predicate<Watcher> releaseNameCheck = w -> Objects.equals(w.getReleaseName(), release);
        Predicate<Watcher> userIdCheck = w -> Objects.equals(w.getUserId(), userId);

        return demoDatabase.removeIf((chartNameCheck.and(releaseNameCheck)).and(userIdCheck));


//        Criteria criteria = new Criteria();
//        criteria.andOperator(Criteria.where(Watcher.CHART_NAME).is(chart), Criteria.where(Watcher.RELEASE_NAME).is(release));
//        criteria = criteria.and(Watcher.USER_ID).is(userId);
//
//        Query query = new Query(criteria);
//
////        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate(ServerType.SLACK_BOT);
//
//        DeleteResult result = mongoTemplate.remove(query, Watcher.class, Watcher.COLLECTION);
//        return (result.getDeletedCount() != 0);

    }

    //When someone Asks for number of watchers
    public List<Watcher> getWatcherUserEmails(String chart, String release, String userId) throws Exception {

        Predicate<Watcher> chartNameCheck = w -> Objects.equals(w.getChartName(), chart);
        Predicate<Watcher> releaseNameCheck = w -> Objects.equals(w.getReleaseName(), release);

        return demoDatabase.stream().filter(chartNameCheck.and(releaseNameCheck)).collect(Collectors.toList());

//        Criteria criteria = new Criteria();
//        criteria.andOperator(Criteria.where(Watcher.CHART_NAME).is(chart), Criteria.where(Watcher.RELEASE_NAME).is(release));
//
//        Query query = new Query(criteria);
//        query.fields().include(Watcher.USER_EMAIL);
//
////        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate(ServerType.SLACK_BOT);
//
//        return mongoTemplate.find(query, Watcher.class, Watcher.COLLECTION);

    }

    public boolean searchAWatcher(String chart, String release, String userId) throws Exception {

        Predicate<Watcher> chartNameCheck = w -> Objects.equals(w.getChartName(), chart);
        Predicate<Watcher> releaseNameCheck = w -> Objects.equals(w.getReleaseName(), release);
        Predicate<Watcher> userIdCheck = w -> Objects.equals(w.getUserId(), userId);

        return demoDatabase.stream().anyMatch((chartNameCheck.and(releaseNameCheck)).and(userIdCheck));


//        Criteria criteria = new Criteria();
//        criteria.andOperator(Criteria.where(Watcher.CHART_NAME).is(chart), Criteria.where(Watcher.RELEASE_NAME).is(release));
//        criteria = criteria.and(Watcher.USER_ID).is(userId);
//
//        Query query = new Query(criteria);
//
////        MongoTemplate mongoTemplate = templateFactory.getApplicationMongoTemplate(ServerType.SLACK_BOT);
//        Watcher watcher = mongoTemplate.findOne(query, Watcher.class, Watcher.COLLECTION);
//
//        return (watcher != null);

    }

}