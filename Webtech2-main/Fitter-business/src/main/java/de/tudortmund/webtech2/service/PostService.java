package de.tudortmund.webtech2.service;

import de.tudortmund.webtech2.entity.Post;
import de.tudortmund.webtech2.entity.User;
import de.tudortmund.webtech2.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    private final UtilService utilService;

    /**
     * Service containing main functions for handling post related requests
     *
     * @param postRepository postRepository
     * @param userService    userService
     * @param utilService    utilService
     */
    @Autowired
    public PostService(PostRepository postRepository,
                       UserService userService, UtilService utilService){
        this.postRepository = postRepository;
        this.userService = userService;
        this.utilService = utilService;
    }

    /**
     * Creates a new Post for a user. There can only be one post created by a user per day
     *
     * @param user          user creating this post
     * @param steps         step entry for this post
     * @param description   description of this post
     * @param sudo          super-do: will ignore the date-check. It's main purpose is for admin and debug usage.
     * @return              created Post
     * @throws IllegalStateException There is already a post created on this day.
     */
    public Post createPost(User user, int steps, String description, boolean sudo) throws IllegalStateException{
        if(!sudo && userService.hasPostedToday(user.getName())){
            throw new IllegalStateException("This user has already posted today");
        }
        Post post = new Post();
        post.setUser(user);
        post.setSteps(steps);
        post.setDescription(description);
        post.setDate(Calendar.getInstance(TimeZone.getDefault()));
        return postRepository.save(post);
    }

    /**
     * Getter for a post by id
     * @param id id of the post to get
     * @return requested post
     */
    public Post getPost(Long id){
        return postRepository.findById(id).orElse(null);
    }

    /**
     * Getter for a list of all posts sorted by id
     * @param limit limit (page request)
     * @param page page (page request)
     * @return list of post objects
     */
    public List<Post> getPosts(int limit, int page){
        Pageable pageable = PageRequest.of(page, limit);
        return postRepository.findAllByOrderByIdDesc(pageable).getContent();
    }

    /**
     * Manipulates a post object with given information
     * @param post post to edit
     * @param steps steps to set, set null if steps are fine
     * @param description description to set, set null if fine
     * @return edited post
     */
    @Transactional
    public Post editPost(Post post, String steps, String description){
        try {
            if(steps != null){
                post.setSteps(Integer.parseInt(steps));
                System.out.println("Setting steps");
            }
            if(description != null){
                post.setDescription(description);
            }
            return post;
        } catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Removes a post from database
     * @param post post to remove
     */
    @Transactional
    public void deletePost(Post post){
        postRepository.delete(post);
    }

    /**
     * Adds a reaction to a given post
     * @param postId post to react to
     * @param reactor user reacting to the post
     * @param reaction reaction for this post as string representation e.g. 🥸
     * @return Post object with reaction
     * @throws IllegalStateException User has already reacted to this post or Post does not exist
     * @throws IllegalArgumentException String is not a single emoji
     */
    public Post reactToPost(long postId, User reactor, String reaction) throws IllegalStateException, IllegalArgumentException{
        Post post = postRepository.findById(postId).orElse(null);
        String reactorName = reactor.getName();

        if(post == null){
            throw new IllegalStateException();
        }
        if(post.getReactions().containsKey(reactorName)){
            throw new IllegalStateException();
        }
        if(!utilService.isSingleEmoji(reaction)){
            throw new IllegalArgumentException();
        }
        post.getReactions().put(reactorName, reaction);
        return post;
    }

    /**
     * Removes a reaction from a given post
     * @param postId post to remove reaction from
     * @param user user removing its reaction
     * @return post with removed reaction
     * @throws IllegalStateException post does not exist
     * @throws IllegalArgumentException user has no reaction on this post
     */
    public Post removeReaction(long postId, User user) throws IllegalStateException, IllegalArgumentException{
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null){
            throw new IllegalArgumentException("This post does not exist!");
        }
        String username = user.getName();
        if(!post.getReactions().containsKey(username)){
            throw new IllegalStateException("There is no reaction from this user!");
        }
        post.getReactions().remove(username);
        return post;
    }

}
