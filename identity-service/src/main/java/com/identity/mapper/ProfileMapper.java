package com.identity.mapper;

import com.identity.dto.request.ProfileReq;
import com.identity.dto.request.UserReq;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileReq toProfileReq(UserReq userReq);
}
