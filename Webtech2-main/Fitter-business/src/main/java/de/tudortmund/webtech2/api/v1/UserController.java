package de.tudortmund.webtech2.api.v1;

import de.tudortmund.webtech2.dto.PostListDTO;
import de.tudortmund.webtech2.dto.UserDTO;
import de.tudortmund.webtech2.dto.UserListDTO;
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

import java.util.*;


@Transactional
@RestController
@RequestMapping(path = "api/v1")
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final UtilService utilService;
    private final SecurityService securityService;

    /**
     * Controller used to handle user related requests
     * @param userService userService
     * @param postService userService
     * @param utilService utilService
     * @param securityService securityService
     */
    @Autowired
    public UserController(UserService userService, PostService postService, UtilService utilService, SecurityService securityService){
        this.userService = userService;
        this.postService = postService;
        this.utilService = utilService;
        this.securityService = securityService;
    }

    /**
     * Get a user list
     * @param limit limit (page request)
     * @param page page (page request)
     * @param q search query
     * @return 200 OK | 401 not logged in | 400 illegal request | 500 server error
     */
    @GetMapping(path = "users")
    public ResponseEntity<UserListDTO> getUsers(@RequestParam int limit, @RequestParam @Nullable Integer page,
                                                @RequestParam @Nullable String q){
        try {
            String principalName = securityService.getPrincipalName();
            q = q == null ? "" : q;
            page = page == null ? 0 : page;
            List<User> users = userService.getUsers(limit, page, q);
            UserListDTO userList = utilService.createUserListDTOFromUsersList(users, principalName);
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException | IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for getting information about a user. You need to either use the id or username as request parameter
     * to get this information
     * @param username The username
     * @return 200 OK | 401 not logged in | 400 request wrong | 208 user does not exist | 500 server error
     */
    @GetMapping(path = "user/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username){
        try {
            String principalName = securityService.getPrincipalName();
            if(username == null){
                throw new IllegalArgumentException();
            }
            User user = userService.getUser(username);
            if(user == null){
                throw new IllegalStateException();
            }
            return new ResponseEntity<>(utilService.createUserDTO(user, principalName), HttpStatus.OK);
        }catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get information about the user logged in
     * @return 200 OK | 401 not logged in | 500 server error
     */
    @GetMapping(path= "user/me")
    public ResponseEntity<UserDTO> getMe(){
        try {
            String principalName = securityService.getPrincipalName();
            User user = userService.getUser(principalName);
            return new ResponseEntity<>(utilService.createUserDTO(user, principalName), HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * edit user
     * @param username name of user to edit
     * @param body request body: String password: new password
     * @return 200 OK | 401 not logged in | 400 request wrong | 500 server error
     */
    @PutMapping(path = "user/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> modifyUser(@PathVariable String username, @RequestBody Map<String, String> body){
        try {
            User principal = securityService.getPrincipal();
            User userToEdit = userService.getUser(username);
            if(userToEdit == null){
                throw new IllegalArgumentException();
            }
            if (!userToEdit.getName().equals(principal.getName()) && !principal.getIsAdmin()) {
                throw new AuthenticationException();
            }
            String bodyPassword = body.get("password");
            if(!securityService.sanitizePassword(bodyPassword)){
                throw new IllegalArgumentException("Password does not match requirements!");
            }
            User edited = userService.editUser(userToEdit, bodyPassword);
            return new ResponseEntity<>(utilService.createUserDTO(edited, principal.getName()),HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for deleting a user by id.
     * @param username name of the user to delete
     * @return 200 OK | 401 not logged in | 400 request wrong | 500 server error
     */
    @DeleteMapping(path = "user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username){
        try {
            User principal = securityService.getPrincipal();
            // Check whether callee is either the user to delete itself or admin
            if (!principal.getIsAdmin() && !principal.getName().equals(username)) {
                throw new AuthenticationException();
            }
            deleteUserAndPosts(username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void deleteUserAndPosts(String username){
        User user = userService.getUser(username);
        user.getPosts().forEach(postService::deletePost);
        userService.deleteUser(username);
    }


    /**
     * Follow a user
     * @param username name of user to follow
     * @return 200 OK | 401 not logged in | 400 request wrong | 500 server error
     */
    @PostMapping(path = "user/{username}/follow")
    public ResponseEntity<String> addFollowing(@PathVariable String username){
        try {
            User principal = securityService.getPrincipal();
            if(username == null){
                throw new IllegalArgumentException();
            }
            User following = userService.getUser(username);
            // Check following and user
            if(following == null || principal == null){
                // Some User not found
                throw new IllegalArgumentException();
            }
            // Add following to user and follower to followed user.
            userService.addFollowing(principal, following);
            // Return OK
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException | IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Unfollows a user
     * @param username name of user to unfollow
     * @return 200 OK | 401 not logged in | 400 request wrong | 500 server error
     */
    @DeleteMapping(path = "user/{username}/follow")
    public ResponseEntity<String> removeFollowing(@PathVariable String username){
        try {
            User principal = securityService.getPrincipal();
            if (null == username) {
                // No data for user to add as following
                throw new IllegalArgumentException();
            }
            User following = userService.getUser(username);
            // Check following and user
            if(following == null || principal == null){
                // Some User not found
                throw new IllegalArgumentException();
            }
            // Remove following from user and follower from followed user.
            userService.removeFollowing(principal, following);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException | IllegalStateException e){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the follower of a specified user
     * @param username user to get the followers
     * @param limit limit (page request)
     * @param page page (page request)
     * @return 200 OK | 401 not logged in | 400 request wrong | 208 user does not exist | 500 server error
     */
    @GetMapping(path = "user/{username}/follower")
    public ResponseEntity<UserListDTO> getFollower(@PathVariable String username, @RequestParam int limit,
                                                   @RequestParam @Nullable Integer page){
        try {
            String principalName = securityService.getPrincipalName();
            page = page == null ? 0 : page;
            if(limit <= 0 || page < 0){
                throw new IllegalArgumentException();
            }
            if(!userService.exists(username)){
                throw new IllegalStateException();
            }
            User user = userService.getUser(username);
            List<User> follower = userService.getFollowers(user, page, limit);
            UserListDTO dto = utilService.createUserListDTOFromUsersList(follower, principalName);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the followed user of a specified user
     * @param username name of user to get the followed users
     * @param limit limit (page request)
     * @param page page (page request)
     * @return 200 OK | 401 not logged in | 400 request wrong | 208 user does not exist | 500 server error
     */
    @GetMapping(path = "user/{username}/following")
    public ResponseEntity<UserListDTO> getFollowing(@PathVariable String username, @RequestParam int limit,
                                                    @RequestParam @Nullable Integer page){
        try {
            String principalName = securityService.getPrincipalName();
            page = page == null ? 0 : page;
            if(limit <= 0 || page < 0){
                throw new IllegalArgumentException();
            }
            if(!userService.exists(username)){
                throw new IllegalStateException();
            }
            User user = userService.getUser(username);
            List<User> users = userService.getFollowing(user, page, limit);
            UserListDTO dto = utilService.createUserListDTOFromUsersList(users, principalName);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get posts of a specified user
     * @param username name of user to get the posts
     * @param limit limit (page request)
     * @param page page (page request)
     * @return 200 OK | 401 not logged in | 400 request wrong | 208 user does not exist | 500 server error
     */
    @GetMapping("user/{username}/posts")
    public ResponseEntity<PostListDTO> getPostsForUser(@PathVariable String username, @RequestParam int limit,
                                                       @RequestParam @Nullable Integer page){
        try {
            String principalName = securityService.getPrincipalName();
            page = page == null ? 0 : page;
            if(!userService.exists(username)){
                throw new IllegalStateException();
            }
            User user = userService.getUser(username);
            List<Post> posts = userService.getPostsByUser(user, page, limit);
            PostListDTO dto = utilService.createPostListDTO(posts, principalName);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
