package de.tudortmund.webtech2;

import de.tudortmund.webtech2.entity.Post;
import de.tudortmund.webtech2.entity.User;
import de.tudortmund.webtech2.service.PostService;
import de.tudortmund.webtech2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class StartupBean {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;


    @PostConstruct
    public void init(){
        userService.createUser("admin", "admin", true);
        userService.createUser("nick", "nick", false);
        userService.createUser("jonah", "jonah", false);
        userService.createUser("mick", "mick", false);
        userService.createUser("vi", "vi", false);

        createPosts();
    }


    public void createPosts(){
        User nick = userService.getUser("nick");
        User jonah = userService.getUser("jonah");
        User vi = userService.getUser("vi");

        postService.createPost(nick, 200, "Hallo ich bin eine Beschreibung!🥸", true);
        postService.createPost(nick, 201230, "Hallo ich bin andere Beschreibung!🐮", true);
        postService.createPost(jonah, 32200, "Hallo ich bin eine Beschreibung!👹", true);
        postService.createPost(nick, 3230, "Hallo ich bin andere Beschreibung!🐮", true);
        postService.createPost(vi, 32, "Hallo ich bin eine Beschreibung!👹", true);
        postService.createPost(nick, 541230, "Hallo ich bin andere Beschreibung!🐮", true);
        postService.createPost(nick, 6751230, "Hallo ich bin andere Beschreibung!🐮", true);
    }


}
