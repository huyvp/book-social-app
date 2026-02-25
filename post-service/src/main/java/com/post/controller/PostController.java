package com.post.controller;

import com.post.dto.request.PostRequest;
import com.post.handler.ResponseHandler;
import com.post.service.IPostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    IPostService postService;

    @PostMapping(path = "/create")
    public ResponseEntity<Object> post(@RequestBody PostRequest postRequest) {
        return ResponseHandler.execute(
                postService.createPost(postRequest)
        );
    }

    @GetMapping(path = "/my-post")
    public ResponseEntity<Object> getMyPosts() {

        return ResponseHandler.execute(
                postService.getMyPosts()
        );
    }
}