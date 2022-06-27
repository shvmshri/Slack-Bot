package com.domain.myjavaapi.config;

import com.domain.myjavaapi.models.Global;
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
    private String globalMongoURI = System.getenv("GLOBAL_MONGO_URI");
    private String globalCollection = System.getenv("GLOBAL_COLLECTION");
    private String globalDatabase = System.getenv("GLOBAL_DATABASE");

    private MongoClient mongo(String uri) {

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);

    }

    private MongoTemplate mongoTemplate(String uri, String database) {

        return new MongoTemplate(mongo(uri), database);
    }

    private Global getApplicationInfo(MongoTemplate globalMongoTemplate) {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("serverCategory").is("MONGO"), Criteria.where("serverType").is("SHARED_LINKS"));
        Query query = new Query(criteria);
        Global global = globalMongoTemplate.findOne(query, Global.class, globalCollection);
        return global;

    }

    private MongoTemplate applicationMongoTemplateHandler() {

        MongoTemplate globalMongoTemplate = getGlobalMongoTemplate();
        Global globalInfo = getApplicationInfo(globalMongoTemplate);
        MongoTemplate applicationMongoTemplate = mongoTemplate(globalInfo.getUrl(), globalInfo.getDbName());
        applicationMongoTemplate.indexOps("Watcher").ensureIndex(new Index().expire(0).on("expireAt", Sort.Direction.ASC));
        return applicationMongoTemplate;

    }

    public MongoTemplate getGlobalMongoTemplate() {

        MongoTemplate globalMongoTemplate;
        if (mongoTemplateMappings.containsKey("GLOBAL")) {
            globalMongoTemplate = mongoTemplateMappings.get("GLOBAL");
        } else {
            globalMongoTemplate = mongoTemplate(globalMongoURI, globalDatabase);
            mongoTemplateMappings.put("GLOBAL", globalMongoTemplate);
        }
        return globalMongoTemplate;

    }

    public MongoTemplate getApplicationMongoTemplate() {

        MongoTemplate applicationMongoTemplate;
        if (mongoTemplateMappings.containsKey("APPLICATION")) {
            applicationMongoTemplate = mongoTemplateMappings.get("APPLICATION");
        } else {
            applicationMongoTemplate = applicationMongoTemplateHandler();
            mongoTemplateMappings.put("APPLICATION", applicationMongoTemplate);
        }
        return applicationMongoTemplate;

    }

}