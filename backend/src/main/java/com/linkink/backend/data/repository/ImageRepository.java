package com.linkink.backend.data.repository;

import com.linkink.backend.data.entity.Image;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends PagingAndSortingRepository<Image,Long> {
    long deleteByPostPostId(long postId);
    long deleteByVendorProfileId(long profileId);
}
