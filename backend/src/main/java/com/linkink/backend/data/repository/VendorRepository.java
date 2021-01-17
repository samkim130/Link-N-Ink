package com.linkink.backend.data.repository;

import com.linkink.backend.data.entity.Vendor;
import com.linkink.backend.data.projections.VendorView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends PagingAndSortingRepository<Vendor, Long> {
    @Query(value = "select PROFILE_ID as profileId, FIRST_NAME || ' ' || LAST_NAME as fullName from VENDORS", nativeQuery = true)
    List<VendorView> findAllWithProjection();
}
