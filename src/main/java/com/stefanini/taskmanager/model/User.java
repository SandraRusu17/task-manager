package com.stefanini.taskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String firstName;
    private String lastName;

    private List<Task> tasks;

    public User(final String userName, final String firstName, final String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public String getFormattedDetails(){
        return "User {" + firstName +
                ", " + lastName +
                ", nr. of tasks=" + tasks.size() +
                '}';
    }
}
