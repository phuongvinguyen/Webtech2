package de.tudortmund.webtech2.dto;

import de.tudortmund.webtech2.entity.Post;

import java.util.ArrayList;
import java.util.List;

public class PostListDTO {

    private List<PostDTO> posts;

    public PostListDTO(){
        posts = new ArrayList<>();
    }

    public PostListDTO(List<PostDTO> posts){
        this.posts = posts;
    }

    public List<PostDTO> getPosts(){
        return posts;
    }

    public void setPosts(List<PostDTO> posts){
        this.posts = posts;
    }

    public void addPost(PostDTO post){
        posts.add(post);
    }
}
