package com.arun.ytclone.controller;

import com.arun.ytclone.dto.CommentDto;
import com.arun.ytclone.dto.UploadVideoResponse;
import com.arun.ytclone.dto.VideoDto;
import com.arun.ytclone.model.Video;
import com.arun.ytclone.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file")MultipartFile file) {
        return videoService.uploadVideo(file);
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(@RequestParam("file")MultipartFile file, @RequestParam("id")String id) {
        return videoService.uploadThumbnail(file, id);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public Video editVideo(@RequestBody VideoDto videoDto) {
        return videoService.editVideo(videoDto);
    }

    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public Video videoDetails(@PathVariable String videoId) {
        return videoService.getVideoDetails(videoId);
    }

    @PostMapping("/{videoId}/like")
    @ResponseStatus(HttpStatus.OK)
    public Video likeVideo(@PathVariable String videoId) {
        return videoService.likeVideo(videoId);
    }

    @PostMapping("/{videoId}/dislike")
    @ResponseStatus(HttpStatus.OK)
    public Video disLikeVideo(@PathVariable String videoId) {
        return videoService.disLikeVideo(videoId);
    }

    @PostMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public boolean addComment(@PathVariable String videoId, @RequestBody CommentDto commentDto) {
        videoService.addComment(videoId, commentDto);
        return true;
    }

    @GetMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllComments(@PathVariable String videoId) {
        return videoService.getAllComments(videoId);
    }
}
