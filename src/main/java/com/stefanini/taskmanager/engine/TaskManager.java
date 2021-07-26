package com.stefanini.taskmanager.engine;

import com.stefanini.taskmanager.exceptions.UserNotFoundException;
import com.stefanini.taskmanager.model.User;
import com.stefanini.taskmanager.repo.UserFileRepositoryImpl;
import com.stefanini.taskmanager.service.UserService;
import com.stefanini.taskmanager.service.UserServiceImpl;

import java.util.Arrays;
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
    //    -deleteTask -un='UserName' -tt='TaskTitle'
    public static final String DELETE_TASK_COMMAND = "-deleteTask";

    public static final int COMMAND = 0;

    private final UserFileRepositoryImpl userFileRepository = new UserFileRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userFileRepository);

    public void parseCommandArguments(String[] arguments) {
        String joinedArguments = String.join(" ", arguments); //so we can have arguments with spaces
        String[] commandAndParameters = joinedArguments.split(" -"); //split by ' -' again so we have the command and its parameters

        if (commandAndParameters.length < 1) {
            System.out.println("Oops. Please use one of the following commands: -createUser -showAllUsers -addTask -showTasks -deleteTask");
            System.exit(0);
        }
        final String command = commandAndParameters[COMMAND];
        switch (command) {
            case CREATE_USER_COMMAND:
                if (commandAndParameters.length < 4) {
                    System.out.println("Oops. Please refer to the usage of the command : -createUser -fn='FirstName' -ln='LastName' -un='UserName'");
                    break;
                }
                String firstName = getStringValue(commandAndParameters[1]);
                String lastName = getStringValue(commandAndParameters[2]);
                String username = getStringValue(commandAndParameters[3]);
                userService.createUser(username, firstName, lastName);
                break;
            case SHOW_ALL_USERS_COMMAND:
                System.out.println(formatUsers(userService.getAllUsers()));
                break;
            case ADD_TASK_COMMAND:
                if (commandAndParameters.length < 4) {
                    System.out.println("Oops. Please refer to the usage of the command : -addTask -un='UserName' -tt='TaskTitle' -td='TaskDescription'");
                    break;
                }
                String taskToAddUsername = getStringValue(commandAndParameters[1]);
                String taskToAddTitle = getStringValue(commandAndParameters[2]);
                String taskToAddDescription = getStringValue(commandAndParameters[3]);
                try {
                    userService.addTaskFor(taskToAddTitle, taskToAddDescription, taskToAddUsername);
                } catch (UserNotFoundException e) {
                    System.out.println("Oops. User with username " + taskToAddUsername + " not found!");
                }
                break;
            case DELETE_TASK_COMMAND:
                String taskToDeleteTitle = getStringValue(commandAndParameters[2]);
                String taskToDeleteUsername = getStringValue(commandAndParameters[1]);
                try {
                    userService.deleteTaskByTitleFor(taskToDeleteTitle, taskToDeleteUsername);
                } catch (UserNotFoundException e) {
                    System.out.println("Oops. User with username " + taskToDeleteUsername + " not found!");
                }
                break;
            case SHOW_TASKS_COMMAND:
                if (commandAndParameters.length < 2) {
                    System.out.println("Oops. Please refer to the usage of the command : -showTasks -un='UserName'");
                    break;
                }
                String showTasksUsername = getStringValue(commandAndParameters[1]);
                try {
                    System.out.println(userService.getTasksFor(showTasksUsername));
                } catch (UserNotFoundException e) {
                    System.out.println("Oops. User with username " + showTasksUsername + " not found!");
                }
                break;
            default:
                System.out.println("Unknown command. Please use one of the following commands: -createUser -showAllUsers -addTask -showTasks -deleteTask");
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
