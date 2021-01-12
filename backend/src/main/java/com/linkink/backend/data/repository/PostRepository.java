package com.linkink.backend.data.repository;

import com.linkink.backend.data.entity.Post;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post,Long> {
}
