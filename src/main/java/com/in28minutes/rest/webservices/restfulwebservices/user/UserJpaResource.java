package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
	
	private UserRepository repository;
	private PostRepository postRepository;
	
	public UserJpaResource(UserRepository repository, PostRepository postRepository) {
		this.repository = repository;
		this.postRepository = postRepository;
	}
	
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers(){
		
		return repository.findAll();
	}
	
	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id){
		
		Optional<User> user = repository.findById(id);
		
		if(user.isEmpty()) {
			throw new UserNotFoundException("id: " + id);   //this will be printed in the console
		}
		
		//This step is simply wrapping the User object around an Entity Model (Hover over the EntityModel word to know more)
		EntityModel<User> entityModel = EntityModel.of(user.get());
		
		//WebMvcLinkBuilder helps use to add links to our entity model response
		//basically, we are adding the link to the retrieveAllUsers() to our response with the name as 'all-users'
		//WebMvcLinkBuilder is a builder to ease building link instances pointing to Spring MVC Controllers
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("All-Users"));
		
		return entityModel;
	}
	
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id){
		
		repository.deleteById(id);
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int id){
		
		Optional<User> user = repository.findById(id);
		
		if(user.isEmpty()) {
			throw new UserNotFoundException("id: " + id);   //this will be printed in the console
		}
		
		return user.get().getPosts();
	}
	
	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		//@Valid means, the User user will be validated according to the User class at the time of the request
		
		User savedUser = repository.save(user);
		
		//what this line means is, we are, to the current url (/users), adding the /{id}, 
		//and that id's value is populated by the newly created user name savedUser, 
		//and then we are returning the entire thing for example, "/users/4", as a URI in the header of the response
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(savedUser.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Post> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post){
		
		Optional<User> user = repository.findById(id);
		
		if(user.isEmpty()) {
			throw new UserNotFoundException("id: " + id);   //this will be printed in the console
		}
		
		post.setUser(user.get());
		
		Post savedPost = postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(savedPost.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/jpa/users/{id}/posts/{postId}")
	public Post retrievePost(@PathVariable int id, @PathVariable int postId) {
		
		/*
		 ******THIS IS WITHOUT USING DIRECT DATABASE
		 * Optional<User> user = repository.findById(id);
		 * 
		 * // Check if user exists if (user.isEmpty()) { throw new
		 * UserNotFoundException("User not found with id: " + id); }
		 * 
		 * User theUser = user.get();
		 * 
		 * Optional<Post> required_Post = theUser.getPosts().stream() .filter(post ->
		 * post.getId() == postId) .findFirst();
		 * 
		 * if (required_Post.isEmpty()) { throw new
		 * UserNotFoundException("Post not found with id: " + postId); //TECHNICALLY,
		 * THERE SHOULD BE //ANOTHER CLASS WITH NAME 'PostNotFoundException' //HELPING
		 * IN BETTER CLARITY }
		 * 
		 * return required_Post.get();
		 */

	    // Retrieve the post by postId and userId
	    Optional<Post> post = postRepository.findByIdAndUserId(postId, id);
	    if (post.isEmpty()) {
	        throw new UserNotFoundException("Post not found with id: " + postId + " for user with id: " + id);
	    }
	    
	    return post.get();

	}
}



















