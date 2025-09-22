package com.arun.ytclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Document(value = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;

    private String sub;

    private String firstName;

    private String lastName;

    private String fullName;

    @Id
    private String email;

    private String password;

    private Set<String> subscribers = ConcurrentHashMap.newKeySet();

    private Set<String> subscribedToUsers = ConcurrentHashMap.newKeySet();

    private Set<String> videoHistory = ConcurrentHashMap.newKeySet();

    private Set<String> likedVideos = ConcurrentHashMap.newKeySet();

    private Set<String> disLikedVideos = ConcurrentHashMap.newKeySet();

    public void addToLikedVideos(String videoId) {
        likedVideos.add(videoId);
    }

    public void removeFromLikedVideos(String videoId) {
        likedVideos.remove(videoId);
    }

    public void addToDisLikedVideos(String videoId) {
        disLikedVideos.add(videoId);
    }

    public void removeFromDisLikedVideos(String videoId) {
        disLikedVideos.remove(videoId);
    }

    public void addToVideoHistory(String id) {
        videoHistory.add(id);
    }

    public void addToSubscribedToUser(String userId) {
        subscribedToUsers.add(userId);
    }

    public void addToSubscribers(String id) {
        subscribers.add(id);
    }

    public void removeFromSubscribedToUser(String userId) {
        subscribedToUsers.remove(userId);
    }

    public void removeFromSubscribers(String id) {
        subscribers.remove(id);
    }
}
