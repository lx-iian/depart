package com.james.depart.dao;

import com.james.depart.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author james
 * @date 2021-02-01
 */
public interface PostRepository extends MongoRepository<Post, String> {
}
