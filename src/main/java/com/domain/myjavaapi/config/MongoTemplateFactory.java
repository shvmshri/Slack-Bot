package com.domain.myjavaapi.config;
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

    private ConcurrentHashMap<String, MongoTemplate> mappingMongoTemplate = new ConcurrentHashMap<String, MongoTemplate>();
//    private String globalMongoURI = System.getenv("GLOBAL_MONGO_URI");
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

    private String getApplicationURI(MongoTemplate globalTemplate) {
//global, shared links ,  enum
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("serverCategory").is("MONGO"), Criteria.where("serverType").is("SHARED_LINKS"));
        Query query = new Query(criteria);
        String uri = globalTemplate.find(query,class_name).url;

//        String uri = "mongodb+srv://shvmshri:gaurisis@sandbox.zr9k6.mongodb.net/SprinklrTestDb?retryWrites=true&w=majority";
        return uri;
    }

    private String getApplicationDB(MongoTemplate globalTemplate) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("serverCategory").is("MONGO"), Criteria.where("serverType").is("SHARED_LINKS"));
        Query query = new Query(criteria);
        String db = globalTemplate.find(query,class_name).dbName;

//        String db = "SprinklrTestDb";
        return db;
    }

    private MongoTemplate findApplicationMongoTemplate() {

        MongoTemplate globalTemplate;
        if(mappingMongoTemplate.containsKey("global")){
            globalTemplate = mappingMongoTemplate.get("global");
        } else{
            globalTemplate = mongoTemplate(globalMongoURI,globalDatabase);
            mappingMongoTemplate.put("global",globalTemplate);
        }

        MongoTemplate applicationMongoTemplate =  mongoTemplate(getApplicationURI(globalTemplate),getApplicationDB(globalTemplate));
//        MongoTemplate applicationMongoTemplate  = mongoTemplate("mongodb+srv://shvmshri:gaurisis@sandbox.zr9k6.mongodb.net/SprinklrTestDb?retryWrites=true&w=majority","SprinklrTestDb");
        applicationMongoTemplate.indexOps("Watcher").ensureIndex(new Index().expire(0).on("expireAt", Sort.Direction.ASC));
        return applicationMongoTemplate;

    }

    public MongoTemplate ApplicationMongoTemplate() {
        MongoTemplate applicationMongoTemplate;
        if (mappingMongoTemplate.containsKey("application")) {
            applicationMongoTemplate = mappingMongoTemplate.get("application");
        } else {
            applicationMongoTemplate = findApplicationMongoTemplate();
            mappingMongoTemplate.put("application", applicationMongoTemplate);
        }
        return applicationMongoTemplate;
    }


}










