package de.tudortmund.webtech2.dto;

import de.tudortmund.webtech2.entity.Post;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class
PostDTO {
    private long id;

    private int steps;

    private String description;

    private String date;

    private String time;

    private String username;

    private PostReactionsDTO reactions;

    public PostDTO(){}

    public PostDTO(Post post, Boolean alreadyReacted){
        id = post.getId();
        steps = post.getSteps();
        description = post.getDescription();
        username = post.getUser().getName();

        Calendar cal = post.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        date = sdf.format(cal.getTime());

        sdf = new SimpleDateFormat("hh:mm");
        time = sdf.format(cal.getTime());


        reactions = new PostReactionsDTO(post.getReactions());
        reactions.setAlreadyReacted(alreadyReacted);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PostReactionsDTO getReactions() {
        return reactions;
    }

    public void setReactions(PostReactionsDTO reactions) {
        this.reactions = reactions;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
