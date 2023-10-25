package de.tudortmund.webtech2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

/**
 * Entity representing a user
 */
@Entity
public class User{

    private String name;

    private byte[] password;

    private boolean isAdmin;

    private Set<User> followers;

    private Set<User> followed;

    private Set<Post> posts;

    private Calendar joined;

    /**
     * Creates a new user object
     */
    public User() {
        this.followers = new HashSet<>();
        this.followed = new HashSet<>();
        this.posts = new HashSet<>();
        this.joined = Calendar.getInstance(TimeZone.getDefault());
        this.isAdmin = false;
    }

    /**
     * Outputs the name of this user
     * @return name of the user
     */
    @Id
    public String getName(){
        return name;
    }

    /**
     * Sets the name of this user
     * @param name name for the user
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Outputs the users password
     * @return password of the user
     */
    public byte[] getPassword(){
        return password;
    }

    /**
     * Sets the password of the user
     * @param password password to set
     */
    public void setPassword(byte[] password){
        this.password = password;
    }


    /**
     * Sets the Set of followers. May be just used with JPA
     * @param followers Set of Follower elements to set
     */
    public void setFollowers(Set<User> followers){
        this.followers = followers;
    }

    /**
     * Returns the Set of Follower elements
     * @return Set of Followers of this user
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_followers",
            joinColumns = @JoinColumn(name = "followed_name"),
            inverseJoinColumns = @JoinColumn(name = "follower_name"))
    @JsonIgnore
    public Set<User> getFollowers() {
        return followers;
    }

    /**
     * Sets the Set of followed users by this user. May just be used by JPA
     * @param followed The Set of followers to set
     */
    public void setFollowed(Set<User> followed){
        this.followed = followed;
    }

    /**
     * Returns the Set of users this user is following
     * @return Set of User's this user is following
     */
    @ManyToMany(mappedBy = "followers", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<User> getFollowed() {
        return followed;
    }

    /**
     * Sets the Set of Posts this user has created
     * @param posts Set of Posts to set
     */
    public void setPosts(Set<Post> posts){
        this.posts = posts;
    }

    /**
     * Returns the Set of posts this user has created
     * @return Set of posts by this user
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    public Set<Post> getPosts(){
        return posts;
    }


    //TODO: Maybe these functions may be put into the service.

    /**
     * Adds a new follower to the Set of followers by this user
     * @param user the user to add
     */
    private void addFollower(User user){
        followers.add(user);
    }

    private void removeFollower(User user){
        followers.remove(user);
    }

    /**
     * Adds a new followed user to the list of followed users
     * @param user the user to add
     */
    public void addFollowed(User user){
        user.addFollower(this);
        followed.add(user);
    }

    public void removeFollowed(User followed){
        followed.removeFollower(this);
        this.followed.remove(followed);
    }



    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    @Temporal(TemporalType.DATE)
    public Calendar getJoined() {
        return joined;
    }

    public void setJoined(Calendar joined) {
        this.joined = joined;
    }
}
