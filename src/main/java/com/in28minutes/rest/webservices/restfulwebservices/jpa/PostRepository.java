package com.in28minutes.rest.webservices.restfulwebservices.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in28minutes.rest.webservices.restfulwebservices.user.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{

    Optional<Post> findByIdAndUserId(Integer postId, Integer userId);
}
