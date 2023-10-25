package de.tudortmund.webtech2.api.v0;

import de.tudortmund.webtech2.entity.Post;
import de.tudortmund.webtech2.entity.User;
import de.tudortmund.webtech2.service.PostService;
import de.tudortmund.webtech2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * Can be used to create some tests for the backend
 */

@Transactional
@RestController
@RequestMapping(path="api/v0")
public class TestController {

    private UserService userService;
    private PostService postService;

    @PostMapping(path="echo")
    public ResponseEntity<Map<String, Object>> EchoController(@RequestBody MultiValueMap<String, Object> request){
        return new ResponseEntity<>(request.toSingleValueMap(), HttpStatus.OK);
    }

    @Autowired
    public TestController(UserService userService,
                          PostService postService){
        this.userService = userService;
        this.postService = postService;
    }
}
