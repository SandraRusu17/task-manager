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
                userService.createUser(
                        getUsername(commandAndParameters),
                        getFirstName(commandAndParameters),
                        getLastName(commandAndParameters));
                break;
            case SHOW_ALL_USERS_COMMAND:
                System.out.println(formatUsers(userService.getAllUsers()));
                break;
            case ADD_TASK_COMMAND:
                if (commandAndParameters.length < 4) {
                    System.out.println("Oops. Please refer to the usage of the command : -addTask -un='UserName' -tt='TaskTitle' -td='TaskDescription'");
                    break;
                }
                try {
                    userService.addTaskFor(
                            getTaskTitle(commandAndParameters),
                            getTaskDescription(commandAndParameters),
                            getUsername(commandAndParameters));
                } catch (UserNotFoundException e) {
                    System.out.println("Oops. User with username " + getUsername(commandAndParameters) + " not found!");
                }
                break;
            case DELETE_TASK_COMMAND:
                try {
                    userService.deleteTaskByTitleFor(getTaskTitle(commandAndParameters), getUsername(commandAndParameters));
                } catch (UserNotFoundException e) {
                    System.out.println("Oops. User with username " + getUsername(commandAndParameters) + " not found!");
                }
                break;
            case SHOW_TASKS_COMMAND:
                if (commandAndParameters.length < 2) {
                    System.out.println("Oops. Please refer to the usage of the command : -showTasks -un='UserName'");
                    break;
                }
                try {
                    System.out.println(userService.getTasksFor(getUsername(commandAndParameters)));
                } catch (UserNotFoundException e) {
                    System.out.println("Oops. User with username " + getUsername(commandAndParameters) + " not found!");
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
        int startIndexOfQuote = s.indexOf('\'');
        int endIndexOfQuote = s.lastIndexOf('\'');
        if ((startIndexOfQuote == -1 || endIndexOfQuote == -1) || (startIndexOfQuote == endIndexOfQuote)) {
            System.out.println("Oops. Invalid parameter [" + s + "] please refer to the usage of the command : -command -un='param'. Every parameter must be enclosed in single quotes.");
            System.exit(0);
        }
        return s.substring(startIndexOfQuote + 1, endIndexOfQuote);
    }

    private String getUsername(String[] commandParameters) {
        for (String parameter : commandParameters) {
            if (parameter.startsWith("un")) {
                return getStringValue(parameter);
            }
        }
        System.out.println("Oops. Username parameter not found, please refer to the usage : -un='UserName'");
        System.exit(0);
        return "";
    }

    private String getFirstName(String[] commandParameters) {
        for (String parameter : commandParameters) {
            if (parameter.startsWith("fn")) {
                return getStringValue(parameter);
            }
        }
        System.out.println("Oops. FirstName parameter not found, please refer to the usage : -fn='FirstName'");
        System.exit(0);
        return "";
    }

    private String getLastName(String[] commandParameters) {
        for (String parameter : commandParameters) {
            if (parameter.startsWith("ln")) {
                return getStringValue(parameter);
            }
        }
        System.out.println("Oops. LastName parameter not found, please refer to the usage : -ln='LastName'");
        System.exit(0);
        return "";
    }

    private String getTaskDescription(String[] commandParameters) {
        for (String parameter : commandParameters) {
            if (parameter.startsWith("td")) {
                return getStringValue(parameter);
            }
        }
        System.out.println("Oops. Task description parameter not found, please refer to the usage : -td='TaskDescription'");
        System.exit(0);
        return "";
    }

    private String getTaskTitle(String[] commandParameters) {
        for (String parameter : commandParameters) {
            if (parameter.startsWith("tt")) {
                return getStringValue(parameter);
            }
        }
        System.out.println("Oops. Task title parameter not found, please refer to the usage : -tt='TaskTitle'");
        System.exit(0);
        return "";
    }
}
