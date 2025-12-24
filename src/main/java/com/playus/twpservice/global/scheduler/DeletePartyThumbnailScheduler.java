package com.playus.twpservice.global.scheduler;

import com.playus.twpservice.domain.party.entity.PartyThumbnailUrl;
import com.playus.twpservice.domain.party.repository.write.PartyThumbnailUrlRepository;
//import com.playus.twpservice.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
//@RequiredArgsConstructor
//public class DeletePartyThumbnailScheduler {
//
//    private final PartyThumbnailUrlRepository partyThumbnailUrlRepository;
//    private final S3Service s3Service;
//
////    @Scheduled(cron = "0 0 4 * * *")
//    public void deleteAllDeletedThumbnailsFromS3() {
//        LocalDateTime now = LocalDateTime.now();
//        partyThumbnailUrlRepository.findAllDeletedPartyThumbnailUrlBefore(now).stream()
//                .map(PartyThumbnailUrl::getThumbnailUrl)
//                .forEach(s3Service::deleteImage);
//    }
//}
