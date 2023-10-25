package de.tudortmund.webtech2.dto;

import de.tudortmund.webtech2.entity.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserDTO {

    private String username;

    private boolean following;

    private boolean isAdmin;

    private long totalSteps;

    private String joined;

    private int followed;

    private int follower;

    private int posts;

    public UserDTO(User user, boolean following, long totalSteps){
        this.username = user.getName();
        this.isAdmin = user.getIsAdmin();
        this.followed = user.getFollowed().size();
        this.follower = user.getFollowers().size();
        this.posts = user.getPosts().size();
        this.following = following;
        this.totalSteps = totalSteps;

        Calendar cal = user.getJoined();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        this.joined = sdf.format(cal.getTime());
    }


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public int getFollowed() {
        return followed;
    }

    public void setFollowed(int followed) {
        this.followed = followed;
    }

    public int getFollowers() {
        return follower;
    }

    public void setFollowers(int followers) {
        this.follower = followers;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public long getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(long totalSteps) {
        this.totalSteps = totalSteps;
    }
}
