package com.arun.ytclone.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MongoDBIndexConfig {

    private final MongoClient mongoClient;

    @Value("${spring.data.mongodb.database:ytclone}")
    private String databaseName;

    @PostConstruct
    public void initIndexes() {
        log.info("Initializing MongoDB indexes...");
        
        try {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            
            // Video collection indexes
            MongoCollection<Document> videoCollection = database.getCollection("Video");
            
            // Index on userID for faster user video queries
            videoCollection.createIndex(new Document("userID", 1));
            log.info("Created index on Video.userID");
            
            // Index on tags for tag-based searches
            videoCollection.createIndex(new Document("tags", 1));
            log.info("Created index on Video.tags");
            
            // Index on videoStatus for filtering published videos
            videoCollection.createIndex(new Document("videoStatus", 1));
            log.info("Created index on Video.videoStatus");
            
            // Compound index for user videos by status
            videoCollection.createIndex(new Document("userID", 1).append("videoStatus", 1));
            log.info("Created compound index on Video.userID and videoStatus");
            
            // User collection indexes
            MongoCollection<Document> userCollection = database.getCollection("User");
            
            // Index on email (already @Id, but ensuring it's there)
            userCollection.createIndex(new Document("email", 1));
            log.info("Created index on User.email");
            
            // Index on sub (Auth0 subject ID)
            userCollection.createIndex(new Document("sub", 1));
            log.info("Created index on User.sub");
            
            log.info("MongoDB indexes initialized successfully");
            
        } catch (Exception e) {
            log.error("Error creating MongoDB indexes: {}", e.getMessage(), e);
        }
    }
}
