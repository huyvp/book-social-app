package com.identity.mapper;

import com.identity.dto.request.ProfileReq;
import com.identity.dto.request.UserReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "givenName", source = "firstName")
    @Mapping(target = "familyName", source = "lastName")
    ProfileReq toProfileReq(UserReq userReq);
}
