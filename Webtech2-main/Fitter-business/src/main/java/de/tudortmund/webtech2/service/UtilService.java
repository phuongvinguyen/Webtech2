package de.tudortmund.webtech2.service;

import com.vdurmont.emoji.EmojiManager;
import de.tudortmund.webtech2.dto.PostDTO;
import de.tudortmund.webtech2.dto.PostListDTO;
import de.tudortmund.webtech2.dto.UserDTO;
import de.tudortmund.webtech2.dto.UserListDTO;
import de.tudortmund.webtech2.entity.Post;
import de.tudortmund.webtech2.entity.User;
import de.tudortmund.webtech2.repository.PostRepository;
import de.tudortmund.webtech2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UtilService {

    static final int UNICODE_EMOJI_LENGTH = 2;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    /**
     * Utility service used for utility functions other services or controllers need.
     * Also provides a set of functions to enhance the creation of dtos.
     * @param userRepository userRepository
     * @param postRepository postRepository
     */
    @Autowired
    public UtilService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    /**
     * Checks whether a user has already reacted.
     * @param post post to check
     * @param username name of user to check
     * @return true if the user can react, false otherwise
     */
    public Boolean canReact(Post post, String username){
        if (username == null){
            return null;
        }
        return post.getReactions().containsKey(username);
    }

    /**
     * Computes the sum of all steps a user has commited
     * @param user user to check
     * @return total steps of a user
     */
    public int getStepsSum(User user){
        Integer sum = postRepository.sumStepsForUser(user);
        if(sum == null){
            sum = 0;
        }
        return sum;
    }

    /**
     * Checks whether a user is following another one
     * @param followerName name of the potential follower to check
     * @param user user followed
     * @return true if the user with name followerName is following user
     */
    public boolean isFollowing(String followerName, User user){
        if(Objects.equals(user.getName(), followerName)){
            return false;
        }
        return userRepository.isUserFollowing(followerName, user);
    }

    /**
     * Creates a PostDTO
     * @param post post to create
     * @param principalName name of callee
     * @return postDTO
     */
    public PostDTO createPostDTO(Post post, String principalName){
        return new PostDTO(post, canReact(post, principalName));
    }

    /**
     * Creates a PostListDTO
     * @param posts posts to create
     * @param principalName name of callee
     * @return postListDTO
     */
    public PostListDTO createPostListDTO(List<Post> posts, String principalName){
        PostListDTO postListDTO = new PostListDTO();
        posts.forEach(post -> postListDTO.addPost(createPostDTO(post, principalName)));
        return postListDTO;
    }

    /**
     * Creates a UserDTO. Total steps will be computed here
     * @param user user to create
     * @param principalName name of callee
     * @return userDTO
     */
    public UserDTO createUserDTO(User user, String principalName){
        return new UserDTO(user, isFollowing(principalName, user), getStepsSum(user));
    }

    /**
     * creates a UserDTO with already known total steps
     * @param user user to create
     * @param principalName name of callee
     * @param totalSteps total steps of this user
     * @return userDTO
     */
    public UserDTO createUserDTO(User user, String principalName, long totalSteps){
        return new UserDTO(user, isFollowing(principalName, user), totalSteps);
    }

    /**
     * Creates a userListDTO from a List of user-totalSteps pairs
     * @param usersData users to create
     * @param principalName name of callee
     * @return  userListDTO
     */
    public UserListDTO createUserListDTOFromPairList(List<Pair<User, Long>> usersData, String principalName){
        UserListDTO userListDTO = new UserListDTO();
        usersData.forEach(pair -> userListDTO.addUser(createUserDTO(pair.getFirst(), principalName, pair.getSecond())));
        return userListDTO;
    }

    /**
     * Creates a UserListDTO. Steps will be computed here for each user
     * @param users users to create
     * @param principalName name of callee
     * @return userListDTO
     */
    public UserListDTO createUserListDTOFromUsersList(List<User> users, String principalName){
        UserListDTO userList = new UserListDTO();
        users.forEach(user -> userList.addUser(createUserDTO(user, principalName)));
        return userList;
    }

    /**
     * Checks a given string for containing just a single emoji.
     * @param emoji string to check
     * @return true if single emoji, false otherwise
     */
    public boolean isSingleEmoji(String emoji){
        return EmojiManager.isOnlyEmojis(emoji) && emoji.length() == UNICODE_EMOJI_LENGTH;
    }
}

