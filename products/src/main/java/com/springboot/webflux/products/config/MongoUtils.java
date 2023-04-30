package com.springboot.webflux.products.config;

import com.mongodb.*;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;

/**
 *
 * @author Mario Ruiz Rojo
 * Reader of DB connexion's parameter from MongoProperties
 */
public class MongoUtils {
    public static MongoClientSettings createMongoClientSettings(MongoProperties mongoProperties){
        String pass = String.valueOf(mongoProperties.getPassword());
        String uri = "mongodb+srv://"+
            mongoProperties.getUsername()+
            ":"+
            pass+
            "@"+
            mongoProperties.getHost()+
            "/"+
            mongoProperties.getDatabase()+
            ":"+
            mongoProperties.getPort();

        /**
         * Connection parameters object
         */        
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .readConcern(ReadConcern.DEFAULT)
                .writeConcern(WriteConcern.MAJORITY)
                .readPreference(ReadPreference.primary())
                .applyConnectionString(
                    new ConnectionString(uri)
                    )
                .build();

        //if localhost then it uses no password
        if(mongoProperties.getHost().equals("localhost")){
            mongoClientSettings = MongoClientSettings.builder()
                .readConcern(ReadConcern.DEFAULT)
                .writeConcern(WriteConcern.MAJORITY)
                .readPreference(ReadPreference.primary())
                .applyConnectionString(
                    new ConnectionString("mongodb://localhost/"+mongoProperties.getDatabase()+":27017")
                    )
                .build();
        }
        return mongoClientSettings;
    }
}
