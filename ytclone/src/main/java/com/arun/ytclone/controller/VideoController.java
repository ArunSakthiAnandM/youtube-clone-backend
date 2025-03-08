package com.arun.ytclone.controller;

import com.arun.ytclone.dto.UploadVideoResponse;
import com.arun.ytclone.dto.VideoDto;
import com.arun.ytclone.model.Video;
import com.arun.ytclone.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    @Autowired
    private final VideoService videoService;

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
}
