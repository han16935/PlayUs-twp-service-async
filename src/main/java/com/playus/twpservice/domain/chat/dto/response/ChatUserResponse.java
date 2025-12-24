//package com.playus.twpservice.domain.chat.dto.response;
//
//import com.playus.twpservice.domain.common.feign.response.UserInfoResponse;
//import lombok.Builder;
//
//@Builder
//public record ChatUserResponse(
//    long userId,
//    String nickName,
//    String profileImageUrl
//) {
//    public static ChatUserResponse of(long userId, UserInfoResponse userInfo) {
//        return ChatUserResponse.builder()
//                .userId(userId)
//                .nickName(userInfo.nickname())
//                .profileImageUrl(userInfo.profileImageUrl())
//                .build();
//    }
//}
