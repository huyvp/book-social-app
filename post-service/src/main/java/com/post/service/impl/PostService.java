package com.post.service.impl;

import com.post.client.ProfileClient;
import com.post.dto.request.PostRequest;
import com.post.dto.response.PageResponse;
import com.post.dto.response.PostResponse;
import com.post.dto.response.UserProfileResponse;
import com.post.entity.Post;
import com.post.mapper.PostMapper;
import com.post.repository.PostRepository;
import com.post.service.IPostService;
import com.post.utils.DateFormater;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService implements IPostService {

    PostRepository postRepository;
    PostMapper postMapper;
    DateFormater dateFormater;
    ProfileClient profileClient;

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

        UserProfileResponse profile = null;
        try {
            profile = profileClient.getProfile(userId).getResult();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        PageRequest pageRequest = PageRequest.of(
                page, limit, Sort.by("createDate").ascending()
        );

        Page<Post> pageData = postRepository.findAllByUserId(userId, pageRequest);

        String username = profile != null ? profile.getUsername() : null;

        List<PostResponse> listData = pageData.stream().map(post -> {
            var postResponse = postMapper.toPostResponse(post);
            postResponse.setUserName(username);
            postResponse.setCreated(dateFormater.format(post.getCreatedDate()));
            return postResponse;
        }).toList();

        return PageResponse.<List<PostResponse>>builder()
                .totalPage(pageData.getTotalPages())
                .total(pageData.getTotalElements())
                .data(listData)
                .build();
    }
}
