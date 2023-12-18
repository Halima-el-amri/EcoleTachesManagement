import config.DataBase;
import interfaces.ITaskManager;
import model.classes.History;
import model.classes.Task;
import model.classes.User;
import model.enums.Category;
import model.enums.Priority;
import model.enums.Status;
import services.TaskManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String line = "----------------------------------------------------------------------------------------";
    public static final Scanner scanner = new Scanner(System.in);
    public static String email;


    public static int showMenu(Scanner scanner) {
        System.out.println(line);
        System.out.println("                Bienvenu dans TitanTask Votre gestionnaire de tâches");
        System.out.println(line);
        System.out.println("1- Ajouter une tâche");
        System.out.println("2- Afficher les tâches");
        System.out.println("3- Modifier une tâche");
        System.out.println("4- Supprimer une tâche");
        System.out.println("5- trier les tâches");
        System.out.println("6- filtrer les tâches");
        System.out.println("7- Afficher l'historique");
        System.out.println("8- Export les Taches dans fichier csv");
        System.out.println("9- Import les Taches de un fichier csv a une base donné");
        System.out.println("0- Quitter");
        System.out.println(line);
        System.out.print("Veuillez choisir une option : ");
        return scanner.nextInt();
    }

    public static void main(String[] args) throws Exception {
        // database connection
        DataBase.connection();
        ITaskManager iTaskManager = new TaskManager();
        boolean loggedIn = false;
        while (!loggedIn) {
            // login or register
            System.out.println();
            System.out.println();
            System.out.println(line);
            System.out.println("                Bienvenu dans TitanTask Votre gestionnaire de tâches");
            System.out.println(line);
            System.out.println("1- Login");
            System.out.println("2- Register");
            System.out.println("0- Quitter");
            System.out.println(line);
            System.out.print("Veuillez choisir une option : ");
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.println(line);
                    System.out.println("Login :");
                    System.out.println(line);
                    System.out.print("Veuillez saisir votre email : ");
                    email = scanner.next();
                    System.out.print("Veuillez saisir votre mot de passe : ");
                    String password = scanner.next();
                    if (iTaskManager.loginUser(email, password)) {
                        System.out.println(line);
                        System.out.println("login success");
                        System.out.println(line);
                        //todo:rj3 lhna
                        try {
                            User owner = iTaskManager.getUser(email);
                            List<Task> tasks = iTaskManager.displayTasks(owner);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        loggedIn = true;
                        // TODO : SHOW THE USER MENU
                        while (loggedIn) {
                            int userOption = showMenu(scanner);
                            switch (userOption) {

                                case 1:
                                    System.out.println(line);
                                    System.out.println("Ajouter une tâche :");
                                    System.out.println(line);
                                    System.out.print("Veuillez saisir le titre de la tâche : ");
                                    scanner.nextLine();
                                    String title = scanner.nextLine();
                                    System.out.print("Veuillez saisir la description de la tâche : ");
                                    String description = scanner.nextLine();
                                    System.out.print("Veuillez saisir la catégorie de la tâche (HOMEWORK, EXAM, PROJECT) : ");
                                    Category category = Category.valueOf(scanner.next().toUpperCase());
                                    System.out.print("Veuillez saisir la priorité de la tâche (HIGH, MEDUIM, LOW) : ");
                                    Priority priority = Priority.valueOf(scanner.next().toUpperCase());
                                    System.out.print("Veuillez saisir le statut de la tâche (TODO, DOING, DONE) : ");
                                    Status status = Status.valueOf(scanner.next().toUpperCase());
                                    //TODO : SHOW THE LIST OF USERS
                                    System.out.print("Veuillez saisir l'email de l'utilisateur pour qui la tâche est créée : ");
                                    String ownerEmail = scanner.next();
                                    User owner = null;
                                    User createdBy = null;
                                    try {
                                        owner = iTaskManager.getUser(ownerEmail);
                                        createdBy = iTaskManager.getUser(email);
                                    } catch (Exception e) {
                                        System.out.println("Une erreur s'est produite lors de la récupération de l'utilisateur: " + e.getMessage());
                                    }
                                    Task task = new Task(title, description, category, priority, status, LocalDate.now(), owner, new ArrayList<>());
                                    History history = new History("ajouter une task", LocalDate.now(), task, createdBy);
                                    iTaskManager.addTaskWithHistory(task, owner, createdBy, history);
                                    System.out.println(line);
                                    System.out.println("La tâche a été ajoutée avec succès");
                                    System.out.println(line);
                                    break;
                                case 2:
                                    System.out.println(line);
                                    System.out.println("Afficher les tâches :");
                                    System.out.println(line);
                                    System.out.println("1- Afficher toutes les tâches");
                                    System.out.println("2- Afficher vos tâches");
                                    System.out.println("0- Retour");
                                    System.out.println(line);
                                    System.out.print("Veuillez choisir une option : ");
                                    int showTasksOption = scanner.nextInt();
                                    switch (showTasksOption) {
                                        case 1:
                                            List<Task> allTasks = iTaskManager.getAllTasks();
                                            System.out.println(line);
                                            System.out.println("Toutes les tâches :");
                                            System.out.println(line);
                                            for (Task task2 : allTasks) {
                                                System.out.println(task2);
                                            }
                                            System.out.println(line);
                                            break;
                                        case 2:
                                            try {
                                                User currentUser = iTaskManager.getUser(email);
                                                List<Task> userTasks = iTaskManager.getAllTasksByUser(currentUser);
                                                System.out.println(line);
                                                System.out.println("Vos tâches :");
                                                System.out.println(line);
                                                for (Task task2 : userTasks) {
                                                    System.out.println(task2);
                                                }
                                                System.out.println(line);
                                            } catch (Exception e) {
                                                System.out.println("Une erreur s'est produite lors de la récupération de l'utilisateur: " + e.getMessage());
                                            }
                                            break;
                                        case 0:
                                            break;
                                        default:
                                            System.out.println("Veuillez choisir une option valide");
                                    }
                                    break;
                                case 3:
                                    System.out.println(line);
                                    System.out.println("Modifier une tâche");
                                    System.out.println(line);
                                    System.out.print("Veuillez saisir l'id de la tâche : ");
                                    int idtache = scanner.nextInt();
                                    System.out.print("Veuillez saisir le titre de la tâche : ");
                                    String title1 = scanner.next();
                                    System.out.print("Veuillez saisir la description de la tâche : ");
                                    String description1 = scanner.next();
                                    System.out.print("Veuillez saisir la catégorie de la tâche (HOMEWORK, EXAM, PROJECT) : ");
                                    Category category1 = Category.valueOf(scanner.next().toUpperCase());
                                    System.out.print("Veuillez saisir la priorité de la tâche (HIGH, MEDUIM, LOW) : ");
                                    Priority priority1 = Priority.valueOf(scanner.next().toUpperCase());
                                    System.out.print("Veuillez saisir le statut de la tâche (TODO, DOING, DONE) : ");
                                    Status status1 = Status.valueOf(scanner.next().toUpperCase());
                                    System.out.print("Veuillez saisir l'email de l'utilisateur pour qui la tâche est créée : ");
                                    String ownerEmail1 = scanner.next();
                                    User owner1 = null;
                                    User createdBy1 = null;
                                    try {
                                        owner1 = iTaskManager.getUser(ownerEmail1);
                                        createdBy1 = iTaskManager.getUser(email);
                                    } catch (Exception e) {
                                        System.out.println("Une erreur s'est produite lors de la récupération de l'utilisateur: " + e.getMessage());
                                    }
                                    Task task2 = new Task(title1, description1, category1, priority1, status1, LocalDate.now(), owner1, new ArrayList<>());
                                    History history1 = new History("modiffier une task", LocalDate.now(), task2, createdBy1);
                                    iTaskManager.updateTask(task2, history1, idtache, owner1, createdBy1);
                                    System.out.println(line);
                                    System.out.println("La tâche a été modiffiée avec succès");
                                    System.out.println(line);
                                    break;
                                case 4:
                                    System.out.println(line);
                                    System.out.println("Supprimer une tâche :");
                                    System.out.println(line);
                                    System.out.print("Veuillez saisir l'id de la tâche à supprimer : ");
                                    int taskId = scanner.nextInt();
                                    Task taskToDelete = null;
                                    try {
                                        taskToDelete = iTaskManager.getTask(taskId);
                                    } catch (Exception e) {
                                        System.out.println("Une erreur s'est produite lors de la récupération de la tâche: " + e.getMessage());
                                    }
                                    if (taskToDelete != null) {
                                        User createdBys = null;
                                        try {
                                            createdBys = iTaskManager.getUser(email);
                                        } catch (Exception e) {
                                            System.out.println("Une erreur s'est produite lors de la récupération de l'utilisateur: " + e.getMessage());
                                        }
                                        if (createdBys != null) {
                                            History historyDeleted = new History("supprimer une task", LocalDate.now(), taskToDelete, createdBys);
                                            try {
                                                iTaskManager.deleteTask(taskToDelete);
                                                System.out.println(line);
                                                System.out.println("La tâche a été supprimée avec succès");
                                                System.out.println(line);
                                            } catch (Exception e) {
                                                System.out.println("Une erreur s'est produite lors de la suppression de la tâche: " + e.getMessage());
                                            }
                                        }
                                    } else {
                                        System.out.println("La tâche avec l'ID donné n'existe pas");
                                    }
                                    break;
                                case 5:
                                    System.out.println(line);
                                    System.out.println("Trier les tâches :");
                                    System.out.println(line);
                                    System.out.println("1- Trier les tâches par priorité (ascendant)");
                                    System.out.println("2- Trier les tâches par priorité (descendant)");
                                    System.out.println("3- Trier les tâches par date de création (ascendant)");
                                    System.out.println("4- Trier les tâches par date de création (descendant)");
                                    System.out.println("0- Retour");
                                    System.out.println(line);
                                    System.out.print("Veuillez choisir une option : ");
                                    int sortOption = scanner.nextInt();
                                    System.out.println(line);

                                    switch (sortOption) {

                                        case 1:
                                            System.out.println(iTaskManager.sortByPriority("lowToHigh"));
                                            break;
                                        case 2:
                                            System.out.println(iTaskManager.sortByPriority("highToLow"));
                                            break;
                                        case 3:
                                            System.out.println(iTaskManager.sortByCretionDate("lowToHigh"));
                                            break;
                                        case 4:
                                            System.out.println(iTaskManager.sortByCretionDate("highToLow"));
                                            break;
                                        default:
                                            System.out.println("votre choix est invalid");
                                    }

                                    break;
                                case 6:

                                    System.out.println(line);
                                    System.out.println("Select your category");
                                    System.out.println(line);
                                    System.out.println("1- HOMEWORK");
                                    System.out.println("2- EXAM");
                                    System.out.println("3- PROJECT");
                                    System.out.print("Votre choix : ");
                                    int filterOption = scanner.nextInt();
                                    User owner5 = iTaskManager.getUser(email);
                                    List<Task> tasks = iTaskManager.displayTasks(owner5);
                                    List<Task> filteredTasks;


                                    switch (filterOption) {

                                        case 1:
                                            filteredTasks = iTaskManager.filterTasksByCategory(tasks, Category.HOMEWORK);
                                            if (filteredTasks.size() != 0) {
                                                System.out.println(line);
                                                System.out.println("Filtered tasks:");
                                                System.out.println(line);
                                                for (Task tsk : filteredTasks) {
                                                    System.out.println(tsk);

                                                }
                                            } else {
                                                System.out.println(line);
                                                System.out.println("aucune Taches dans cette category");
                                                System.out.println(line);
                                            }
                                            break;
                                        case 2:
                                            filteredTasks = iTaskManager.filterTasksByCategory(tasks, Category.EXAM);
                                            if (filteredTasks != null) {
                                                System.out.println(line);
                                                System.out.println("Filtered tasks:");
                                                System.out.println(line);
                                                for (Task tsk : filteredTasks) {
                                                    System.out.println(tsk);
                                                    System.out.println(line);

                                                }
                                            } else {
                                                System.out.println(line);
                                                System.out.println("aucune Taches dans cette category");
                                                System.out.println(line);
                                            }
                                            break;
                                        case 3:
                                            filteredTasks = iTaskManager.filterTasksByCategory(tasks, Category.PROJECT);
                                            if (filteredTasks != null) {
                                                System.out.println(line);
                                                System.out.println("Filtered tasks:");
                                                System.out.println(line);
                                                for (Task tsk : filteredTasks) {
                                                    System.out.println(tsk);
                                                    System.out.println(line);
                                                }
                                            } else {
                                                System.out.println(line);
                                                System.out.println("aucune Taches dans cette category");
                                                System.out.println(line);
                                            }
                                            break;
                                        default:
                                            System.out.println("votre choix est invalid");
                                    }

                                    break;


                                case 7:
                                    System.out.println(line);
                                    System.out.println("Afficher l'historique :");
                                    System.out.println(line);
                                    List<History> histories = iTaskManager.getAllHistory();
                                    System.out.println(line);
                                    System.out.println("Toutes les Historiques :");
                                    System.out.println(line);
                                    for (History history2 : histories) {
                                        System.out.println(history2);
                                    }
                                    System.out.println(line);
                                    break;
                                case 8:
                                    TaskManager.exportTask();
                                    break;
                                case 9:
                                    TaskManager.importTask();
                                    break;
                                case 0:
                                    loggedIn = false;
                                    break;
                                default:
                                    System.out.println("Veuillez choisir une option valide");
                            }
                        }
                    } else {
                        System.out.println(line);
                        System.out.println("login failed");
                        System.out.println(line);
                    }
                    break;
                case 2:
                    System.out.println(line);
                    System.out.println("Register :");
                    System.out.println(line);
                    System.out.print("Veuillez saisir votre nom : ");
                    String name = scanner.next();
                    System.out.print("Veuillez saisir votre email : ");
                    email = scanner.next();
                    System.out.print("Veuillez saisir votre mot de passe : ");
                    password = scanner.next();
                    System.out.print("Veuillez saisir votre role : ");
                    String role = scanner.next();
                    User user = new User(name, email, password, role, new ArrayList<Task>());
                    try {
                        iTaskManager.registerUser(user);

                    } catch (Exception e) {
                        System.out.println("Une erreur s'est produite lors de la création de votre compte: " + e.getMessage());
                    }
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Veuillez choisir une option valide");

            }
        }
    }
}