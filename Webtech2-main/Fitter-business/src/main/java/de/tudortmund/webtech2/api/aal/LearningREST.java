package de.tudortmund.webtech2.api.aal;

import java.util.List;
import de.tudortmund.webtech2.api.v1.UserController;
import de.tudortmund.webtech2.entity.User;
import de.tudortmund.webtech2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Transactional
@RestController
@RequestMapping(path = "rest")
public class LearningREST {

    private final UserController userController;
    private final UserRepository userRepository;

    @Autowired
    public LearningREST(UserController userController, UserRepository userRepository) {
        this.userController = userController;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "reset")
    public void reset() {
        List<User> users = userRepository.findAll();
        users.forEach(user ->{
            if(!user.getIsAdmin()){
            userController.deleteUserAndPosts(user.getName());
            }
        });
    }
}

