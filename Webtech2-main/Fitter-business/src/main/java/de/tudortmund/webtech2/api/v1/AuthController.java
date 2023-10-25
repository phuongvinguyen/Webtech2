package de.tudortmund.webtech2.api.v1;

import de.tudortmund.webtech2.dto.UserDTO;
import de.tudortmund.webtech2.entity.User;
import de.tudortmund.webtech2.service.SecurityService;
import de.tudortmund.webtech2.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Transactional
@RestController
@RequestMapping(path = "api/v1")
public class AuthController {

    private final UserService userService;

    private final SecurityService securityService;

    @Autowired
    public AuthController(UserService userService, SecurityService securityService){
        this.userService = userService;
        this.securityService = securityService;
    }

    /**
     * Endpoint for creating a new user from form data.
     * @param body form data element containing the information about the user to create
     * @return status: 200 OK; 401 input wrong; 500 server error
     */
    @PostMapping(path = "register",
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@RequestBody Map<String, String> body){
        try {
            String username = body.get("username");
            String password = body.get("password");
            if(username == null ||
                    password == null ||
                    !securityService.sanitizeUsername(username) ||
                    !securityService.sanitizePassword(password)){
                throw new IllegalArgumentException();
            }
            User user = userService.createUser(username, password, false);
            return new ResponseEntity<>(new UserDTO(user, false, 0), HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Logs in the user if credentials are correct
     * @param body request body containing username and password
     * @return 200 OK; 400 credentials missing; 401 credentials wrong; 500 something with the server code
     */
    @PostMapping(path = "login",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody Map<String, String> body){
        try {
            String username = body.get("username");
            String password = body.get("password");

            if(username == null || password == null){
                throw new IllegalArgumentException();
            }
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            SecurityUtils.getSubject().login(token);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
