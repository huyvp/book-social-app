package com.post.service;

import com.post.dto.request.PostRequest;
import com.post.dto.response.PageResponse;
import com.post.dto.response.PostResponse;

import java.util.List;

public interface IPostService {
    PostResponse createPost(PostRequest postRequest);

    PageResponse<List<PostResponse>> getMyPosts(Integer page, Integer limit);
}
