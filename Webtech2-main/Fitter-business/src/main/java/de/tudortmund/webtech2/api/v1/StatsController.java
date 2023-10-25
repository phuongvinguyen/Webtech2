package de.tudortmund.webtech2.api.v1;

import de.tudortmund.webtech2.dto.UserListDTO;
import de.tudortmund.webtech2.entity.User;
import de.tudortmund.webtech2.service.SecurityService;
import de.tudortmund.webtech2.service.UserService;
import de.tudortmund.webtech2.service.UtilService;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path ="/api/v1/")
public class StatsController {

    private final UserService userService;

    private final SecurityService securityService;

    private final UtilService utilService;

    /**
     * Controller with main purpose to deliver statistics about all users (e.g. a tier list) or a single user.
     * @param userService userService
     * @param securityService securityService
     * @param utilService utilService
     */
    @Autowired
    public StatsController(UserService userService, SecurityService securityService, UtilService utilService) {
        this.userService = userService;
        this.securityService = securityService;
        this.utilService = utilService;
    }

    /**
     * Used to get a tier list of users with the most totalSteps
     * @param limit limit (page request)
     * @param page page (page request)
     * @return 200 OK | 401 not logged in | 400 illegal request | 500 server error
     */
    @GetMapping(path = "statistics")
    public ResponseEntity<UserListDTO> getStats(@RequestParam int limit, @RequestParam @Nullable Integer page){
        try {
            if(page == null){
                page = 0;
            }
            if(limit < 1 || limit > 50){
                throw new IllegalArgumentException();
            }
            String principalName = securityService.getPrincipalName();
            List<Pair<User, Long>> data = userService.getScoreboard(limit, page);
            UserListDTO dto = utilService.createUserListDTOFromPairList(data, principalName);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
