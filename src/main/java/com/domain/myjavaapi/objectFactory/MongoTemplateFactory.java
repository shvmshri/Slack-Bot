package com.domain.myjavaapi.objectFactory;

import com.domain.myjavaapi.enums.ServerType;
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

    private static final String GLOBAL_MONGO_URI = "mongodb+srv://shvmshri:gaurisis@sandbox.zr9k6.mongodb.net/GlobalServers?retryWrites=true&w=majority";
    private static final String GLOBAL_COLLECTION = "Servers";
    private static final String GLOBAL_DATABASE = "GlobalServers";
    private static final String SERVER_CATEGORY = "serverCategory";
    private static final String SERVER_CATEGORY_VALUE = "MONGO";
    private static final String SERVER_TYPE = "serverType";
    private static final String APPLICATION_COLLECTION_NAME = "Watcher";
    private static ConcurrentHashMap<ServerType, MongoTemplate> mongoTemplateMappings = new ConcurrentHashMap<ServerType, MongoTemplate>();

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

    private MongoServerInfo getMongoServerInfo(MongoTemplate globalMongoTemplate, ServerType serverType) {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where(SERVER_CATEGORY).is(SERVER_CATEGORY_VALUE), Criteria.where(SERVER_TYPE).is(serverType));
        Query query = new Query(criteria);
        return globalMongoTemplate.findOne(query, MongoServerInfo.class, GLOBAL_COLLECTION);

    }

    public MongoTemplate getGlobalMongoTemplate() {

        mongoTemplateMappings.putIfAbsent(ServerType.GLOBAL,mongoTemplate(GLOBAL_MONGO_URI, GLOBAL_DATABASE));
        return mongoTemplateMappings.get(ServerType.GLOBAL);

    }

    private MongoTemplate initApplicationMongoTemplate(ServerType serverType) {

        MongoTemplate globalMongoTemplate = getGlobalMongoTemplate();
        MongoServerInfo mongoServerInfoInfo = getMongoServerInfo(globalMongoTemplate,serverType);
        MongoTemplate applicationMongoTemplate = mongoTemplate(mongoServerInfoInfo.getUrl(), mongoServerInfoInfo.getDbName());
        applicationMongoTemplate.indexOps(APPLICATION_COLLECTION_NAME).ensureIndex(new Index().expire(0).on("expireAt", Sort.Direction.ASC));
        return applicationMongoTemplate;

    }

    public MongoTemplate getApplicationMongoTemplate(ServerType serverType) {

        mongoTemplateMappings.putIfAbsent(serverType,initApplicationMongoTemplate(serverType));
        return mongoTemplateMappings.get(serverType);

    }

}