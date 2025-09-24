package com.tutorials;

import com.tutorials.dao.TutorialDAO;
import com.tutorials.dao.impl.TutorialDAOImpl;
import com.tutorials.entity.Tutorial;
import com.tutorials.exceptions.DataBaseOperationException;
import com.tutorials.exceptions.TutorialNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TutorialManagementApp {
        private static TutorialDAO tutorialDAO = new TutorialDAOImpl();
        private static Scanner scanner = new Scanner(System.in);

        public static void main(String[] args) {
            int choice;
            do {
                displayMenu();
                choice = getUserChoice();
                try {
                    switch (choice) {
                        case 1 -> addTutorial();
                        case 2 -> viewAllTutorials();
                        case 3 -> viewTutorialById();
                        case 4 -> updateTutorial();
                        case 5 -> deleteTutorial();
                        case 0 -> System.out.println("Exiting Tutorial Management System. Goodbye!");
                        default -> System.out.println("Invalid choice. Please try again.");
                    }
                } catch (DataBaseOperationException | TutorialNotFoundException e) {
                    System.err.println("Error: " + e.getMessage());
                } catch (InputMismatchException e) {
                    System.err.println("Invalid input. Please enter the correct data type.");
                    scanner.nextLine();
                } catch (Exception e) {
                    System.err.println("An unexpected error occurred: " + e.getMessage());
                }
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            } while (choice != 0);
            scanner.close();
        }

        private static void displayMenu() {
            System.out.println("\n--- Tutorial Management System ---");
            System.out.println("1. Add New Tutorial");
            System.out.println("2. View All Tutorials");
            System.out.println("3. View Tutorial by ID");
            System.out.println("4. Update Tutorial");
            System.out.println("5. Delete Tutorial");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
        }

        private static int getUserChoice() {
            try {
                int ch = Integer.parseInt(scanner.nextLine().trim());
                return ch;
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a number.");
                return -1;
            }
        }

        private static void addTutorial() throws DataBaseOperationException {
            System.out.println("\n--- Add New Tutorial ---");
            System.out.print("Enter Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Author: ");
            String author = scanner.nextLine();
            System.out.print("Enter URL: ");
            String url = scanner.nextLine();
            System.out.print("Enter Published Date (YYYY-MM-DD, leave blank if unknown): ");
            String dateStr = scanner.nextLine();
            LocalDate publishedDate = null;
            if (!dateStr.trim().isEmpty()) {
                try { publishedDate = LocalDate.parse(dateStr); } catch (DateTimeParseException e) { System.err.println("Invalid date format. Date will be set to null."); }
            }
            Tutorial newTutorial = new Tutorial(title, author, url, publishedDate);
            tutorialDAO.addTutorial(newTutorial);
            System.out.println("Tutorial added successfully! ID: " + newTutorial.getId());
        }

        private static void viewAllTutorials() throws DataBaseOperationException {
            System.out.println("\n--- All Tutorials ---");
            ArrayList<Tutorial> tutorials = tutorialDAO.getAllTutorials();
            if (tutorials.isEmpty()) System.out.println("No tutorials found.");
            else tutorials.forEach(System.out::println);
        }

        private static void viewTutorialById() throws TutorialNotFoundException, DataBaseOperationException {
            System.out.println("\n--- View Tutorial by ID ---");
            System.out.print("Enter Tutorial ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Tutorial tutorial = tutorialDAO.getTutorialById(id);
            System.out.println("Found Tutorial: " + tutorial);
        }

        private static void updateTutorial() throws TutorialNotFoundException, DataBaseOperationException {
            System.out.println("\n--- Update Tutorial ---");
            System.out.print("Enter Tutorial ID to update: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            Tutorial existingTutorial = tutorialDAO.getTutorialById(id);
            System.out.println("Existing Tutorial: " + existingTutorial);

            System.out.print("Enter New Title (leave blank to keep current: '" + existingTutorial.getTitle() + "'): ");
            String newTitle = scanner.nextLine();
            if (!newTitle.trim().isEmpty()) existingTutorial.setTitle(newTitle);

            System.out.print("Enter New Author (leave blank to keep current: '" + existingTutorial.getAuthor() + "'): ");
            String newAuthor = scanner.nextLine();
            if (!newAuthor.trim().isEmpty()) existingTutorial.setAuthor(newAuthor);

            System.out.print("Enter New URL (leave blank to keep current: '" + existingTutorial.getUrl() + "'): ");
            String newUrl = scanner.nextLine();
            if (!newUrl.trim().isEmpty()) existingTutorial.setUrl(newUrl);

            System.out.print("Enter New Published Date (YYYY-MM-DD, leave blank to keep current: " + existingTutorial.getPublishedDate() + "): ");
            String newDateStr = scanner.nextLine();
            if (!newDateStr.trim().isEmpty()) {
                try { existingTutorial.setPublishedDate(LocalDate.parse(newDateStr)); } catch (DateTimeParseException e) { System.err.println("Invalid date format. Keeping current date."); }
            }

            tutorialDAO.updateTutorial(existingTutorial);
            System.out.println("Tutorial updated successfully!");
        }

        private static void deleteTutorial() throws TutorialNotFoundException, DataBaseOperationException {
            System.out.println("\n--- Delete Tutorial ---");
            System.out.print("Enter Tutorial ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            tutorialDAO.deleteTutorial(id);
            System.out.println("Tutorial with ID " + id + " deleted successfully!");
        }
}
