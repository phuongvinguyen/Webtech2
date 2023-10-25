package de.tudortmund.webtech2.dto;

import de.tudortmund.webtech2.entity.User;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class UserListDTO {
    private List<UserDTO> users;

    public UserListDTO(){
        users = new ArrayList<>();
    }

    public UserListDTO(List<UserDTO> users){
        this.users = new ArrayList<>();
        this.users.addAll(users);
    }

    public void addUser(UserDTO user){
        users.add(user);
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
