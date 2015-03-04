package itinerary.test;

import java.util.Calendar;
import java.util.List;

import itinerary.main.*;

// @author A0121409R

/**
 * Demonstrates the likely behavior of FileStorage. Can also be considered as an
 * "unofficial" test of ProtoFileStorage.
 */
public class ProtoFileStorageDemo {

    // Starting state:

    static Task task1 = new Task(1, "ExampleText1", "ExampleCategory1", true,
                                 true);

    static Task task2 = new ScheduleTask(2, "ExampleText2", "ExampleCategory2",
                                         true, true, Calendar.getInstance(),
                                         Calendar.getInstance());

    static Task task3 = new DeadlineTask(3, "ExampleText3", "ExampleCategory3",
                                         true, true, Calendar.getInstance());

    static Command command1 = new Command(task1, CommandType.ADD, null); // Add
    // command
    // for Task1

    static Command command2 = new Command(task2, CommandType.ADD, null); // Add
    // command
    // for Task2

    static Command command3 = new Command(task3, CommandType.ADD, null); // Add

    // Command
    // for Task3

    public static void main(String args[]) {

        ProtoFileStorage fileStorage = new ProtoFileStorage("mytestfile.txt");

        // Clear any old content (for this test only)
        fileStorage.clearAll();

        // Create empty list (since file is empty)
        List<Task> listTask = fileStorage.getAllTasks();

        // Add Tasks to file
        listTask = fileStorage.addTask(command1).getTasks();
        listTask = fileStorage.addTask(command2).getTasks();
        listTask = fileStorage.addTask(command3).getTasks();

        // Delete Task 1
        Command command4 =
                           new Command(listTask.get(0), CommandType.DELETE,
                                       null);

        listTask = fileStorage.deleteTask(command4).getTasks();

        System.out.println("Make a copy of fileStorage: ");
        ProtoFileStorage fileStorage2 = new ProtoFileStorage(fileStorage);

        displayFileStorages(fileStorage, fileStorage2);

        List<Task> listTask2 = fileStorage2.getAllTasks();

        System.out.println("Delete Line 1 in fileStorage: ");
        Command command5 =
                           new Command(listTask.get(0), CommandType.DELETE,
                                       null);

        listTask = fileStorage.deleteTask(command5).getTasks();

        displayFileStorages(fileStorage, fileStorage2);

        System.out.println("Delete Line 2 in fileStorage2: ");
        Command command6 =
                           new Command(listTask2.get(1), CommandType.DELETE,
                                       null);

        listTask = fileStorage.getAllTasks();
        listTask2 = fileStorage2.deleteTask(command6).getTasks();

        displayFileStorages(fileStorage, fileStorage2);

        System.out.println("Add Line 1 from fileStorage2 into fileStorage1: ");
        Command command7 =
                           new Command(fileStorage2.getAllTasks().get(0),
                                       CommandType.ADD, null);

        listTask = fileStorage.addTask(command7).getTasks();

        displayFileStorages(fileStorage, fileStorage2);

        // BUG
        System.out.println("To demonstrate duplicate Command entry failure: ");
        System.out.println("Add Line 1 from fileStorage2 into fileStorage1: ");
        listTask = fileStorage.addTask(command7).getTasks();

        displayFileStorages(fileStorage, fileStorage2);

        System.out.println("Delete malfunctioning line: ");
        Command command8 =
                           new Command(listTask.get(0), CommandType.DELETE,
                                       null);
        listTask = fileStorage.deleteTask(command8).getTasks();

        displayFileStorages(fileStorage, fileStorage2);

        System.out.println("Edit line 1 of fileStorage: ");
        Task toEdit = listTask.get(0);
        toEdit.setText("Changed the Text");
        Command command9 = new Command(toEdit, CommandType.EDIT, null);
        listTask = fileStorage.editTask(command9).getTasks();

        displayFileStorages(fileStorage, fileStorage2);
    }

    public static void displayFileStorages(ProtoFileStorage fileStorage,
                                           ProtoFileStorage fileStorage2) {
        System.out.println("Items in fileStorage: ");
        System.out.println(fileStorage.currentListTaskString(true));

        System.out.println("Items in fileStorage2: ");
        System.out.println(fileStorage2.currentListTaskString(true));
    }

    public static void printList(List<Task> listTask) {

        for (Task item : listTask) {

            System.out.println(JsonIOHandler.stringFormatter(item));
        }
    }
}
