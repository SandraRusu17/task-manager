package com.stefanini.taskmanager.repo;

import com.stefanini.taskmanager.model.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class UserFileRepositoryImpl implements UserRepository {

    public static final String FILE_LOCATION = "taskmanager.ser";

    @Override
    public void save(final User user) {
        try (
                FileOutputStream fout = new FileOutputStream(FILE_LOCATION);
                ObjectOutputStream oos = new ObjectOutputStream(fout)
        ) {
            List<User> users = findAll();
            users.add(user);
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return findAll()
                .stream()
                .filter(u -> u.getUserName().equals(username)).findFirst();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findAll() {
        try (
                FileInputStream fin = new FileInputStream(FILE_LOCATION);
                ObjectInputStream oos = new ObjectInputStream(fin)
        ) {
            return (List<User>) oos.readObject();
        } catch (Exception e) {
            //first time usage
            return new ArrayList<>();
        }
    }

    @Override
    public void update(User user) {
        List<User> users = findAll();
        try (
                FileOutputStream fout = new FileOutputStream(FILE_LOCATION);
                ObjectOutputStream oos = new ObjectOutputStream(fout)
        ) {
            ListIterator<User> iterator = users.listIterator();
            while (iterator.hasNext()){
                if(iterator.next().getUserName().equals(user.getUserName())){
                    iterator.set(user);
                }
            }
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
