package com.arun.ytclone.service;

import com.arun.ytclone.dto.UploadVideoResponse;
import com.arun.ytclone.dto.VideoDto;
import com.arun.ytclone.model.Video;
import com.arun.ytclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class VideoService {

    @Autowired
    private final S3Service s3Service;

    @Autowired
    private final VideoRepository videoRepository;

    @Autowired
    private final UserService userService;

    public UploadVideoResponse uploadVideo(MultipartFile file) {
        String videoUrl = s3Service.uploadFile(file);
        Video video = new Video();
        video.setUrl(videoUrl);

        Video savedVideo = videoRepository.save(video);

        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getUrl());
    }

    public Video editVideo(VideoDto videoDto) {
        Video savedVideo = getVideoById(videoDto.getId());

        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());

        videoRepository.save(savedVideo);
        return savedVideo;
    }

    public String uploadThumbnail(MultipartFile file, String id) {
        Video savedVideo = getVideoById(id);
        String thumbnailUrl = s3Service.uploadFile(file);
        savedVideo.setThumbnailUrl(thumbnailUrl);
        videoRepository.save(savedVideo);
        return thumbnailUrl;
    }

    private Video getVideoById(String id) {
        return videoRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Cannot find video by ID - " + id));
    }

    public Video getVideoDetails(String videoId) {
        return getVideoById(videoId);
    }

    public Video likeVideo(String videoId) {
        Video video = getVideoById(videoId);
        if (userService.ifVideoLiked(videoId)) {
            video.decrementLikes();
            userService.removeFromLikedVideos(videoId);
        } else if (userService.ifVideoDisLiked(videoId)) {
            video.decrementDisLikes();
            video.incrementLikes();
            userService.addToLikedVideos(videoId);
            userService.removeFromDisLikedVideos(videoId);
        } else {
            video.incrementLikes();
            userService.addToLikedVideos(videoId);
        }
        videoRepository.save(video);
        return video;
    }

    public Video disLikeVideo(String videoId) {
        Video video = getVideoById(videoId);
        if (userService.ifVideoDisLiked(videoId)) {
            video.decrementDisLikes();
            userService.removeFromDisLikedVideos(videoId);
        } else if (userService.ifVideoLiked(videoId)) {
            video.decrementLikes();
            video.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
            userService.removeFromLikedVideos(videoId);
        } else {
            video.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        }
        videoRepository.save(video);
        return video;
    }

}
