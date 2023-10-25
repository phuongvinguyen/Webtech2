package de.tudortmund.webtech2.api.v1;

import de.tudortmund.webtech2.dto.PostDTO;
import de.tudortmund.webtech2.dto.PostListDTO;
import de.tudortmund.webtech2.entity.Post;
import de.tudortmund.webtech2.entity.User;
import de.tudortmund.webtech2.service.SecurityService;
import de.tudortmund.webtech2.service.UtilService;
import de.tudortmund.webtech2.service.PostService;
import de.tudortmund.webtech2.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Transactional
@RestController
@RequestMapping(path = "api/v1/")
public class PostController {


    private final PostService postService;
    private final UserService userService;
    private final UtilService utilService;
    private final SecurityService securityService;

    /**
     * Controller used to handle post related requests
     * @param postService postService
     * @param userService userService
     * @param utilService utilService
     * @param securityService securityService
     */
    @Autowired
    public PostController(PostService postService, UserService userService, UtilService utilService, SecurityService securityService){
        this.postService = postService;
        this.userService = userService;
        this.utilService = utilService;
        this.securityService = securityService;
    }

    /**
     * Creates a new post for a user
     * @param body int steps: amount of steps for this post; String description: description for this post
     * @return 200 OK | 401 not logged in | 400 illegal request | 406 post was already created today | 500 server error
     */
    @PostMapping(path="post",
                consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDTO> createPost(@RequestBody Map<String, String> body){
        try {
            User user = securityService.getPrincipal();
            int steps = Integer.parseInt(body.get("steps"));
            String description = body.get("description");
            Post post = postService.createPost(user, steps, description, false);
            if(post == null){
                throw new IllegalArgumentException();
            }
            return new ResponseEntity<>(new PostDTO(post, false), HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns information about a post for a given id
     * @param postId id of the post to get
     * @return 200 OK | 401 not logged in | 206 no post with this id | 500 server error
     */
    @GetMapping(path = "post/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId){
        try {
            String principalName = securityService.getPrincipalName();
            Post post = postService.getPost(postId);
            if(post == null) {
                throw new IllegalStateException();
            }
            return new ResponseEntity<>(new PostDTO(post, utilService.canReact(post, principalName)), HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns a list of (all) posts. Can just be accessed by an admin
     * @param limit limit (page request)
     * @param page page (page request)
     * @return 200 OK | 401 not logged in/no admin | 400 illegal request | 500 server error
     */
    @GetMapping(path = "posts")
    public ResponseEntity<PostListDTO> getPosts(@RequestParam int limit,
                                                @RequestParam @Nullable Integer page){
        try {
            User principal = securityService.getPrincipal();
            if(!principal.getIsAdmin()){
                throw new AuthenticationException();
            }
            page = page == null ? 0 : page;
            if(limit <= 0){
                throw new IllegalArgumentException();
            }
            List<Post> posts = postService.getPosts(limit, page);
            PostListDTO dto = utilService.createPostListDTO(posts, principal.getName());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Modifies a given post: Can just be accessed by post owner or admin
     * @param postId id of the post to modify
     * @param body request body: steps: amount of steps; description: description of the post
     * @return 200 OK | 401 not logged in/no rights to modify post | 400 illegal request | 500 server error
     */
    @PutMapping(path = "post/{postId}",
                consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDTO> editPost(@PathVariable Long postId, @RequestBody Map<String, String> body){
        try {
            User principal = securityService.getPrincipal();
            Post post = postService.getPost(postId);
            if(principal == null || post == null) {
                throw new IllegalArgumentException();
            }
            if(!post.getUser().getName().equals(principal.getName()) && !principal.getIsAdmin()){
                throw new AuthenticationException();
            }
            String bodySteps = body.get("steps");
            String bodyDescription = body.get("description");
            Post newPost = postService.editPost(post, bodySteps, bodyDescription);
            PostDTO dto = new PostDTO(newPost, utilService.canReact(newPost, principal.getName()));
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException | IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a certain post for a given id from the database. Can only be called by the post owner or admin
     * @param postId id of the post to remove
     * @return 200 OK | 401 not logged in/no access rights | 400 illegal request | 500 server error
     */
    @DeleteMapping(path="post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId){
        try {
            User principal = securityService.getPrincipal();
            Post post = postService.getPost(postId);
            if(principal == null || post == null){
                throw new IllegalArgumentException();
            }
            if (!principal.getIsAdmin() && !principal.getName().equals(post.getUser().getName())) {
                throw new AuthenticationException();
            }
            postService.deletePost(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalStateException  | IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a reaction for a given post
     * @param postId id of the post to react to
     * @param reaction reaction to set. Must be a single unicode formatted emoji, e.g. 🥸
     * @return 200 OK | 206 reaction already set by this user | 401 not logged in
     *                  | 400 illegal request, e.g. wrong formatted emoji | 500 server error
     */
    @PostMapping(path = "post/{postId}/reaction")
    public ResponseEntity<PostDTO> reactToPost(@PathVariable Long postId, @RequestParam("reaction") String reaction){
        try {
            User principal = securityService.getPrincipal();
            Post post = postService.reactToPost(postId, principal, reaction);
            return new ResponseEntity<>(new PostDTO(post, utilService.canReact(post, principal.getName())), HttpStatus.OK);
        }catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Remove a reaction from a given post
     * @param postId id of the post to remove reaction
     * @return 200 OK | 401 not logged in | 400 illegal request | 500 server error
     */
    @DeleteMapping(path = "post/{postId}/reaction")
    public ResponseEntity<PostDTO> removeReactionFromPost(@PathVariable Long postId){
        try {
            User user = securityService.getPrincipal();
            Post post = postService.removeReaction(postId, user);
            return new ResponseEntity<>(new PostDTO(post, utilService.canReact(post, user.getName())), HttpStatus.OK);
        }catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException | IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get timeline for principal
     * @param limit limit (page request
     * @param page page (page request)
     * @return 200 OK | 401 not logged in | 400 illegal request | 500 server error
     */
    @GetMapping(path = "timeline")
    public ResponseEntity<PostListDTO> getTimeline(@RequestParam @Nullable Integer page, @RequestParam int limit){
        try {
            page = page == null ? 0 : page;
            if(limit <= 0){
                throw new IllegalArgumentException();
            }
            String principalName = securityService.getPrincipalName();
            List<Post> posts = userService.getTimeline(principalName, page, limit);
            PostListDTO dto = utilService.createPostListDTO(posts, principalName);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
