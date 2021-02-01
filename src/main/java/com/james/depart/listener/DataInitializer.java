package com.james.depart.listener;

import com.james.depart.dao.PostRepository;
import com.james.depart.domain.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author james
 * @date 2021-02-01
 */
@Component
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final PostRepository postRepository;

    public DataInitializer(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<Post> posts = initPost();
        posts.forEach(post -> log.info("Post: {}", post));
    }

    private List<Post> initPost() {
        postRepository.deleteAll();

        return Stream.of("Post one", "Post two")
                .map(title -> {
                    Post post = Post.builder()
                            .title(title)
                            .content("Content of " + title)
                            .build();
                    return postRepository.save(post);
                })
                .collect(Collectors.toList());
    }
}