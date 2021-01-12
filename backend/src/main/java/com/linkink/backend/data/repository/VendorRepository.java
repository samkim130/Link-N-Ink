package com.linkink.backend.data.repository;

import com.linkink.backend.data.entity.Vendor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends PagingAndSortingRepository<Vendor, Long> {
}
