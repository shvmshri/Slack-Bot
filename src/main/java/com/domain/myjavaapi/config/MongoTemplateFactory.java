package com.domain.myjavaapi.config;

import com.domain.myjavaapi.models.MongoServerInfo;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class MongoTemplateFactory {

    private ConcurrentHashMap<String, MongoTemplate> mongoTemplateMappings = new ConcurrentHashMap<String, MongoTemplate>();
    private static String GLOBAL_MONGO_URI = "mongodb+srv://shvmshri:gaurisis@sandbox.zr9k6.mongodb.net/GlobalServers?retryWrites=true&w=majority";
    private static String GLOBAL_COLLECTION = "Servers";
    private static String GLOBAL_DATABASE = "GlobalServers";
    private static String SERVER_CATEGORY = "serverCategory";
    private static String SERVER_CATEGORY_VALUE = "MONGO";
    private static String SERVER_TYPE = "serverType";
    private static String WATCHER_COLLECTION_NAME = "Watcher";
    private static String GLOBAL_TEMPLATE = "GlobalTemplate";
    private static String APPLICATION_TEMPLATE = "ApplicationTemplate";

    private MongoClient createMongoClient(String uri) {

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);

    }

    private MongoTemplate mongoTemplate(String uri, String database) {

        return new MongoTemplate(createMongoClient(uri), database);
    }

    private MongoServerInfo getMongoServerInfo(MongoTemplate globalMongoTemplate) {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(SERVER_CATEGORY).is(SERVER_CATEGORY_VALUE), Criteria.where(SERVER_TYPE).is(ServerType.SHARED_LINKS));
        Query query = new Query(criteria);
        MongoServerInfo mongoServerInfo = globalMongoTemplate.findOne(query, MongoServerInfo.class, GLOBAL_COLLECTION);
        return mongoServerInfo;

    }

    public MongoTemplate getGlobalMongoTemplate() {

        MongoTemplate globalMongoTemplate;
        if (mongoTemplateMappings.containsKey(GLOBAL_TEMPLATE)) {
            globalMongoTemplate = mongoTemplateMappings.get(GLOBAL_TEMPLATE);
        } else {
            globalMongoTemplate = mongoTemplate(GLOBAL_MONGO_URI, GLOBAL_DATABASE);
            mongoTemplateMappings.put(GLOBAL_TEMPLATE, globalMongoTemplate);
        }
        return globalMongoTemplate;

    }

    private MongoTemplate initApplicationMongoTemplate() {

        MongoTemplate globalMongoTemplate = getGlobalMongoTemplate();
        MongoServerInfo mongoServerInfoInfo = getMongoServerInfo(globalMongoTemplate);
        MongoTemplate applicationMongoTemplate = mongoTemplate(mongoServerInfoInfo.getUrl(), mongoServerInfoInfo.getDbName());
        applicationMongoTemplate.indexOps(WATCHER_COLLECTION_NAME).ensureIndex(new Index().expire(0).on("expireAt", Sort.Direction.ASC));
        return applicationMongoTemplate;

    }

    public MongoTemplate getApplicationMongoTemplate() {

        MongoTemplate applicationMongoTemplate;
        if (mongoTemplateMappings.containsKey(APPLICATION_TEMPLATE)) {
            applicationMongoTemplate = mongoTemplateMappings.get(APPLICATION_TEMPLATE);
        } else {
            applicationMongoTemplate = initApplicationMongoTemplate();
            mongoTemplateMappings.put(APPLICATION_TEMPLATE, applicationMongoTemplate);
        }
        return applicationMongoTemplate;

    }

}