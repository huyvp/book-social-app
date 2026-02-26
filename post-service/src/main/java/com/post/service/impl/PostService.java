package com.post.service.impl;

import com.post.dto.request.PostRequest;
import com.post.dto.response.PageResponse;
import com.post.dto.response.PostResponse;
import com.post.entity.Post;
import com.post.mapper.PostMapper;
import com.post.repository.PostRepository;
import com.post.service.IPostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService implements IPostService {

    PostRepository postRepository;
    PostMapper postMapper;

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = authentication.getName();

        Post post = Post.builder()
                .userId(userId)
                .content(postRequest.getContent())
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();

        post = postRepository.save(post);
        return postMapper.toPostResponse(post);
    }

    @Override
    public PageResponse<List<PostResponse>> getMyPosts(Integer page, Integer limit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        PageRequest pageRequest =PageRequest.of(
                page, limit, Sort.by("createDate").ascending()
        );

        Page<Post> pageData = postRepository.findAllByUserId(userId, pageRequest);

        List<PostResponse> listData =  pageData.stream().map(postMapper::toPostResponse).toList();

        return PageResponse.<List<PostResponse>>builder()
                .totalPage(pageData.getTotalPages())
                .total(pageData.getTotalElements())
                .data(listData)
                .build();
    }
}
