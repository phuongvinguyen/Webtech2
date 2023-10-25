package de.tudortmund.webtech2.service;

import de.tudortmund.webtech2.entity.Post;
import de.tudortmund.webtech2.entity.User;
import de.tudortmund.webtech2.repository.PostRepository;
import de.tudortmund.webtech2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    /**
     * Service containing main functions for handling user related requests
     * @param userRepository userRepository
     * @param postRepository postRepository
     */
    @Autowired
    public UserService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    /**
     * Creates a new user in the database
     * @param username username of the user
     * @param password password to set
     * @param isAdmin flag if this user has admin permissions
     * @return  User object which was created
     * @throws IllegalArgumentException Username is already taken
     */
    public User createUser(String username, String password, boolean isAdmin) throws IllegalArgumentException{
        if(exists(username)){
            throw new IllegalArgumentException("This user does already exist!");
        }
        User user = new User();
        user.setName(username);
        setPassword(user, password);
        user.setIsAdmin(isAdmin);
        return userRepository.save(user);
    }

    /**
     * Gets the user object with a specified username
     * @param username username of the user to get
     * @return User object to get
     */
    public User getUser(String username){
        return userRepository.findByName(username);
    }

    /**
     * Returns a list of users sorted alphabetically. If a query parameter is given this function can be used as a search.
     * @param limit limit of users in this list (page request)
     * @param page page of this user list (page request)
     * @param query search query. leave blank if just a user list is wanted
     * @return List of User Objects
     */
    public List<User> getUsers(int limit, int page, String query){
        Pageable pageable = PageRequest.of(page, limit);
        List<User> users;
        if(query.equals("")){ // Not a search. Just return all users
            users = userRepository.findAll(pageable).getContent();
        } else {
            users = userRepository.findUsersByNameContaining(query, pageable);
        }
        return users;
    }

    /**
     * Edits a users password
     * @param user user to edit
     * @param password new password
     * @return new user object
     */
    @Transactional
    public User editUser(User user, String password) throws IllegalArgumentException{
        if(!password.equals("")){
            setPassword(user, password);
        }
        return user;
    }

    /**
     * Deletes a user from the database
     * @param username user to delete
     */
    public void deleteUser(String username){
        if(!userRepository.existsByName(username)){
            throw new IllegalArgumentException("This user does not exist");
        }
        userRepository.deleteUserByName(username);
    }

    /**
     * Checks whether a user is existent in the database
     * @param username user to check
     * @return boolean value whether the user is existent
     */
    public boolean exists(String username){
        return userRepository.existsByName(username);
    }

    /**
     * Checks whether a given user has posted toady
     * @param username user to check
     * @return boolean value whether the user has posted today or not
     */
    public boolean hasPostedToday(String username){
        return userRepository.hasUserPostedToday(username);
    }

    /**
     * Used to create a following relationship
     * @param user user to follow
     * @param following follower
     */
    @Transactional
    public void addFollowing(User user, User following){
        user.addFollowed(following);
    }

    /**
     * Used to remove a following relationship
     * @param user followed user
     * @param following follower
     */
    @Transactional
    public void removeFollowing(User user, User following){
        user.removeFollowed(following);
    }

    /**
     * Creates a user list of all followers for a specified user
     * @param user user to get followers for
     * @param page page (page request)
     * @param limit limit (page request)
     * @return user list of followers
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class) // https://stackoverflow.com/questions/11746499/how-to-solve-the-failed-to-lazily-initialize-a-collection-of-role-hibernate-ex
    public List<User> getFollowers(User user, int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        return userRepository.findUsersByFollowedIsOrderByName(user, pageable);
    }

    /**
     * Creates a user list of all followed for a specified user
     * @param user user to get followed for
     * @param page page (page request)
     * @param limit limit (page request)
     * @return user list of followed
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class) // https://stackoverflow.com/questions/11746499/how-to-solve-the-failed-to-lazily-initialize-a-collection-of-role-hibernate-ex
    public List<User> getFollowing(User user, int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        return userRepository.findUsersByFollowersIsOrderByName(user, pageable);
    }

    /**
     * Creates a list of all posts for a spec
     * @param user creator of the posts to get
     * @param page page (page request)
     * @param limit limit (page request)
     * @return PostListPageDTO including posts of the specified user
     * @throws IllegalArgumentException if the page request values are invalid or the user is null
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public List<Post> getPostsByUser(User user, int page, int limit) throws IllegalArgumentException {
        if(limit <= 0 || page < 0){
            throw new IllegalArgumentException("Amount can't be less or equal than zero. Offset must be positive or zero.");
        }

        if(user == null){
            throw new IllegalArgumentException("User is null!");
        }
        // Get list of posts
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        return postRepository.findPostsByUserIs(user, pageable);
    }


    /**
     * Creates the timeline for a specified user
     * @param username user to get the timeline for
     * @param page page (page request)
     * @param limit limit (page request)
     * @return list of posts including the posts for the specified users timeline
     * @throws IllegalArgumentException user does not exist
     */
    @Transactional
    public List<Post> getTimeline(String username, int page, int limit) throws IllegalArgumentException{
        // Check for illegal username
        if(!exists(username)){
            throw new IllegalArgumentException("The user with the username " + username + " does not exist!");
        }
        User user = getUser(username);
        Set<User> followed = user.getFollowed();
        followed.add(user);
        Pageable pageable = PageRequest.of(page, limit);
        return userRepository.findFollowedPosts(followed, pageable);
    }

    /**
     * creates a scoreboard used to display statistics for all users. It creates a list of users sorted by their total amount of steps
     * @param limit limit (page request)
     * @param page page (page request)
     * @return list of pairs containing users and steps sorted by total steps amount
     */
    @Transactional
    public List<Pair<User, Long>> getScoreboard(int limit, int page){
        Pageable pageable = PageRequest.of(page, limit);
        List<Object[]> objects = userRepository.findUsersByTotalSteps(pageable).getContent();
        return objects.stream().map(
                obj -> Pair.of((User)obj[0], (Long)obj[1])).collect(Collectors.toList());
    }


    /**
     * Sets the password of a user (without checks!!)
     * @param user user to set the password for
     * @param password password to set
     */
    public void setPassword(User user, String password){
        user.setPassword(SecurityService.hashPassword(password));
    }

    /**
     * Checks whether a username password combination is correct or not.
     * @param username the username of the user to check
     * @param password the password of the user
     * @return true if the password and username are matching, otherwise false.
     * @throws IllegalArgumentException if there is no user with this username
     */
    public boolean checkPassword(String username, String password) throws IllegalArgumentException{
        // Get user with username
        User user = getUser(username);
        if(user == null){
            throw new IllegalArgumentException("user is not valid!");
        }
        return Arrays.equals(user.getPassword(), (SecurityService.hashPassword(password)));
    }

}
