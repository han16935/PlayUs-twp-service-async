package com.playus.twpservice.global.config.data.mongodb;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

    @Primary
    @Bean(name = "readMongoDbFactory")
    public MongoDatabaseFactory readMongoDbFactory(@Value("${spring.data.mongodb.read.uri}") String uri) {
        return new SimpleMongoClientDatabaseFactory(uri);
    }

//    @Bean(name = "chatMongoDbFactory")
//    public MongoDatabaseFactory chatMongoDbFactory(@Value("${spring.data.mongodb.chat.uri}") String uri) {
//        return new SimpleMongoClientDatabaseFactory(uri);
//    }



    @Primary
    @Bean(name = "readMongoTemplate")
    public MongoTemplate readMongoTemplate(@Qualifier("readMongoDbFactory") MongoDatabaseFactory factory) {
        return new MongoTemplate(factory);
    }

//    @Bean(name = "chatMongoTemplate")
//    public MongoTemplate chatMongoTemplate(@Qualifier("chatMongoDbFactory") MongoDatabaseFactory factory) {
//        return new MongoTemplate(factory);
//    }
}

