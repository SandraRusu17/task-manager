package com.stefanini.taskmanager.engine;

import com.stefanini.taskmanager.model.User;
import com.stefanini.taskmanager.repo.UserFileRepositoryImpl;
import com.stefanini.taskmanager.service.UserService;
import com.stefanini.taskmanager.service.UserServiceImpl;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class TaskManager {

    //    -createUser -fn='FirstName' -ln='LastName' -un='UserName'
    public static final String CREATE_USER_COMMAND = "-createUser";
    //    -showAllUsers
    public static final String SHOW_ALL_USERS_COMMAND = "-showAllUsers";
    //    -addTask -un='UserName' -tt='TaskTitle' -td='TaskDescription'
    public static final String ADD_TASK_COMMAND = "-addTask";
    //    -showTasks -un='UserName'
    public static final String SHOW_TASKS_COMMAND = "-showTasks";

    private final UserFileRepositoryImpl userFileRepository = new UserFileRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userFileRepository);

    public void parseCommandLine(String[] commandLine) {
        final String command = commandLine[0];
        switch (command) {
            case CREATE_USER_COMMAND:
                userService.createUser(
                        getStringValue(commandLine[3]),
                        getStringValue(commandLine[2]),
                        getStringValue(commandLine[1]));
                break;
            case SHOW_ALL_USERS_COMMAND:
                System.out.println(formatUsers(userService.getAllUsers()));
                break;
            case ADD_TASK_COMMAND:
                userService.addTaskFor(
                        getStringValue(commandLine[2]),
                        getStringValue(commandLine[3]),
                        getStringValue(commandLine[1]));
                break;
            case SHOW_TASKS_COMMAND:
                System.out.println(userService.getTasksFor(getStringValue(commandLine[1])));
                break;
            default:
                System.out.println("Unknown command");
        }
    }

    private String formatUsers(final List<User> users) {
        StringJoiner joiner = new StringJoiner(", \n");
        for (User user : users) {
            String formattedDetails = user.getFormattedDetails();
            joiner.add(formattedDetails);
        }
        return joiner.toString();
    }

    private String getStringValue(final String s) {
        return s.substring(s.indexOf('\'') + 1, s.lastIndexOf('\''));
    }
}
