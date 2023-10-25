package de.tudortmund.webtech2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
public class Post {

    private long id;

    private int steps;

    private String description;

    private Calendar date;

    private User user;

    private Map<String, String> reactions;

    /**
     * Creates a new Post
     */
    public Post() {
        reactions = new HashMap<>();
    }

    /**
     * Sets the id. WARNING: Should not be used to set own id's!
     * @param id the id to be set.
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * Sets the amount of steps in this post
     * @param steps the amount of steps to set
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * Sets the description text of this post
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the date of this post
     * @param date the date to set
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     * sets the user which is owning this post
     * @param user the owner of this post
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Sets the reactions of the post
     * @param reactions as a Map of String (Reaction) and Integer (Amount)
     */
    public void setReactions(Map<String, String> reactions) {
        this.reactions = reactions;
    }

    /**
     * Outputs the id of this database element.
     * @return the id as Long.
     */
    @Id
    @GeneratedValue
    public long getId() {
        return this.id;
    }

    /**
     * returns the amount of steps in this post
     * @return the amount of steps
     */
    public int getSteps() {
        return steps;
    }

    /**
     * returns the description of this post
     * @return the description of this post as String
     */
    public String getDescription() {
        return description;
    }

    /**
     * returns the date of this post
     * @return the date of this post as Date object
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDate() {
        return date;
    }

    /**
     * returns the owner of this post
     * @return a User object which is the owner of this post.
     */
    @ManyToOne()
    public User getUser(){
        return this.user;
    }

    /**
     * returns the reactions of this post
     * @return a map which is storing the reaction as a key with its amount as value
     */
    @ElementCollection
    public Map<String, String> getReactions() {
        return reactions;
    }
}
