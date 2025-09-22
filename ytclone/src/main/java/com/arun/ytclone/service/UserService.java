package com.arun.ytclone.service;

import com.arun.ytclone.dto.UserInfoDto;
import com.arun.ytclone.model.User;
import com.arun.ytclone.repository.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${auth0.userinfoEndpoint}")
    private String userInfoEndpoint;

    private final UserRepository userRepository;

    public void registerUser(String jwtTokenValue) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndpoint))
                .setHeader("Authorization", String.format("Bearer %s", jwtTokenValue))
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2).build();

        try {
            HttpResponse<String> httpResponse = httpClient
                    .send(httpRequest, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UserInfoDto userInfoDto = objectMapper.readValue(httpResponse.body(), UserInfoDto.class);

            User user = new User();
            user.setEmail(userInfoDto.getEmail());
            user.setFirstName(userInfoDto.getGivenName());
            user.setLastName(userInfoDto.getFamilyName());
            user.setFullName(userInfoDto.getName());
            user.setSub(userInfoDto.getSub());
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error while registering user ", e);
        }
    }

    public User getCurrentUser() {
        String sub = ((Jwt) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getClaim("sub");

        return userRepository
                .findBySub(sub)
                .orElseThrow(()->
                        new IllegalArgumentException("Cannot find user with sub - " + sub));
    }

    public void addToLikedVideos(String videoId) {
        User user = getCurrentUser();
        user.addToLikedVideos(videoId);
        userRepository.save(user);
    }

    public void removeFromLikedVideos(String videoId) {
        User user = getCurrentUser();
        user.removeFromLikedVideos(videoId);
        userRepository.save(user);
    }

    public boolean ifVideoLiked(String videoId) {
        return getCurrentUser().getLikedVideos().stream().anyMatch(likedVideo -> likedVideo.equals(videoId));
    }

    public boolean ifVideoDisLiked(String videoId) {
        return getCurrentUser().getDisLikedVideos().stream().anyMatch(disLikedVideo -> disLikedVideo.equals(videoId));
    }

    public void addToDisLikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToDisLikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void removeFromDisLikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromDisLikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void addVideoToHistory(String id) {
        User currentUser = getCurrentUser();
        currentUser.addToVideoHistory(id);
        userRepository.save(currentUser);
    }

    public void subscribeUser(String userId) {
        User currentUser = getCurrentUser();
        currentUser.addToSubscribedToUser(userId);

        User user = getUserById(userId);
        user.addToSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public void unsubscribeUser(String userId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromSubscribedToUser(userId);

        User user = getUserById(userId);
        user.removeFromSubscribers(currentUser.getId());

        userRepository.save(currentUser);
        userRepository.save(user);
    }

    public Set<String> getUserHistory(String userId) {
        return getUserById(userId).getVideoHistory();
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("Cannot find user with Id = "+userId));
    }
}
