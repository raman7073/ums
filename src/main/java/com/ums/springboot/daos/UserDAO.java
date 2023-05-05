package com.ums.springboot.daos;

import com.ums.springboot.entities.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class UserDAO {

    HashMap<UUID, User> hashMap = new HashMap<>();

    public Optional<User> findById(UUID userId) {

        return Optional.ofNullable(hashMap.get(userId));
    }

    public User save(User user) {

        UUID userId = UUID.randomUUID();
        user.setUserId(userId);
        hashMap.put(userId, user);
        return hashMap.get(userId);
    }

    public User update(UUID userId, User user) {

        hashMap.put(userId, user);
        return hashMap.get(userId);
    }

    public List<User> findAll() {

        List<User> userList = new ArrayList<>();
        for (User user : hashMap.values()) {
            if (!user.isDeleted()) userList.add(user);
        }
        return userList;
    }

    public boolean existByUserName(String userName) {

        if (hashMap.isEmpty()) {
            return false;
        }
        for (User user : hashMap.values()) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public List<User> findByUserName(String userName) {

        List<User> userList = new ArrayList<>();
        for (User user : hashMap.values()) {
            if (user.getUserName().equals(userName)) userList.add(user);
        }
        return userList;
    }

    public boolean deleteById(UUID userId) {

        if (hashMap.containsKey(userId)) {
            hashMap.get(userId).setDeletedAt(LocalDateTime.now());
            hashMap.get(userId).setDeleted(true);
            return true;
        }
        return false;
    }
}
