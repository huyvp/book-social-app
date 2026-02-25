package com.post.service;

import com.post.dto.request.PostRequest;
import com.post.dto.response.PostResponse;

import java.util.List;

public interface IPostService {
    PostResponse createPost(PostRequest postRequest);

    List<PostResponse> getMyPosts();
}
