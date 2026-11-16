package com.aston.userservice;

import com.aston.userservice.config.HibernateUtil;
import com.aston.userservice.dao.UserDao;
import com.aston.userservice.dao.UserDaoImpl;
import com.aston.userservice.entity.User;
import com.aston.userservice.exception.DataAccessException;

import java.util.List;
import java.util.Scanner;

public class UserServiceApplication {

    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в user-service!");

        int choice;
        do {
            printMenu();
            choice = readInt(scanner);
            System.out.println();

            try {
                if (choice == 1) {
                    createUser(scanner, userDao);
                } else if (choice == 2) {
                    findUser(scanner, userDao);
                } else if (choice == 3) {
                    printAll(userDao);
                } else if (choice == 4) {
                    updateUser(scanner, userDao);
                } else if (choice == 5) {
                    deleteUser(scanner, userDao);
                } else if (choice != 0) {
                    System.out.println("Нет такого пункта.");
                }
            } catch (DataAccessException e) {
                System.out.println("Ошибка базы данных: " + e.getMessage());
            }

            System.out.println();
        } while (choice != 0);

        scanner.close();
        HibernateUtil.shutdown();
        System.out.println("Пока!");
    }

    private static void printMenu() {
        System.out.println("--- Меню ---");
        System.out.println("1 - создать");
        System.out.println("2 - найти по id");
        System.out.println("3 - показать всех");
        System.out.println("4 - изменить");
        System.out.println("5 - удалить");
        System.out.println("0 - выход");
        System.out.print("Ваш выбор: ");
    }

    private static void createUser(Scanner scanner, UserDao userDao) {
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Возраст: ");
        int age = readInt(scanner);

        User user = new User(name, email, age);
        userDao.save(user);
        System.out.println("Готово. " + user);
    }

    private static void findUser(Scanner scanner, UserDao userDao) {
        System.out.print("Id: ");
        long id = readLong(scanner);
        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("Такого пользователя нет.");
        } else {
            System.out.println(user);
        }
    }

    private static void printAll(UserDao userDao) {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Пока никого нет.");
            return;
        }
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void updateUser(Scanner scanner, UserDao userDao) {
        System.out.print("Id пользователя: ");
        long id = readLong(scanner);
        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("Такого пользователя нет.");
            return;
        }

        System.out.print("Новое имя [" + user.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }

        System.out.print("Новый email [" + user.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        System.out.print("Новый возраст [" + user.getAge() + "]: ");
        String ageLine = scanner.nextLine();
        if (!ageLine.isEmpty()) {
            user.setAge(Integer.parseInt(ageLine));
        }

        userDao.update(user);
        System.out.println("Изменения сохранены.");
    }

    private static void deleteUser(Scanner scanner, UserDao userDao) {
        System.out.print("Id: ");
        long id = readLong(scanner);
        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("Такого пользователя нет.");
            return;
        }
        userDao.delete(id);
        System.out.println("Удалено.");
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Введите число: ");
            }
        }
    }

    private static long readLong(Scanner scanner) {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Введите число: ");
            }
        }
    }
}