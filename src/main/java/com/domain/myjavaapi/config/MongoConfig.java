//package com.domain.myjavaapi.config;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//import java.util.Collection;
//import java.util.Collections;
//
//@Configuration
//public class MongoConfig extends AbstractMongoClientConfiguration {
//    private static final String uri = System.getenv("MONGO_DB_URL");
//
//    @Override
//    protected String getDatabaseName() {
//        return "SprinklrTestDb";
//    }
//
//    @Override
//    public MongoClient mongoClient() {
//        ConnectionString connectionString = new ConnectionString(uri);
//        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .build();
//
//        return MongoClients.create(mongoClientSettings);
//    }
//
//    @Override
//    public Collection getMappingBasePackages() {
//        return Collections.singleton("com.example.demotry");
//    }
//
//    @Override
//    protected boolean autoIndexCreation() {
//        return true;
//    }
//
//
//}
//
//
//
//
