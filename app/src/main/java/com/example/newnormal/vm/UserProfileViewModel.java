package com.example.newnormal.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.newnormal.data.models.User;
import com.example.newnormal.data.repositories.UserRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserProfileViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public void insert(User user) {
        userRepository.insert(user);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void deleteAllUsers(User user) {
        userRepository.deleteAllUsers();
    }

    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        return userRepository.getAllUsers();
    }
}