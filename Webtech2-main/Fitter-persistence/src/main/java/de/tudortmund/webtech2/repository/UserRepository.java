package de.tudortmund.webtech2.repository;

import de.tudortmund.webtech2.entity.Post;
import de.tudortmund.webtech2.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByName(String name);

    User findByName(String name);

    void deleteUserByName(String username);

    List<User> findUsersByFollowedIsOrderByName(User user, Pageable pageable);

    List<User> findUsersByFollowersIsOrderByName(User user, Pageable pageable);

    List<User> findUsersByNameContaining(String name, Pageable pageable);

    @Query("SELECT u, SUM(p.steps) as totalSteps FROM User u JOIN u.posts p GROUP BY u ORDER BY totalSteps DESC")
    Page<Object[]> findUsersByTotalSteps(Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.name = :potFollowerName AND :user MEMBER OF u.followed")
    boolean isUserFollowing(String potFollowerName, User user);

    @Query("SELECT COUNT(p) > 0 FROM User u JOIN u.posts p WHERE u.name = :username AND TRUNC(p.date) = TRUNC(CURRENT_TIMESTAMP)")
    boolean hasUserPostedToday(String username);

    @Query("SELECT p FROM Post p WHERE p.user IN :users ORDER BY p.date DESC")
    List<Post> findFollowedPosts(Set<User> users, Pageable pageable);





}
