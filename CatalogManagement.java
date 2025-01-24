// Adding a test line
import java.io.*;
import java.util.*;

public class CatalogManagement {

    private static final String FILE_NAME = "catalog.csv";
    private static List<Item> catalog = new ArrayList<>();

    public static void main(String[] args) {
        loadCatalog();
        Scanner scanner = new Scanner(System.in);

        if (!authenticateUser(scanner)) {
            System.out.println("Authentication failed. Exiting.");
            return;
        }

        boolean running = true;

        while (running) {
            System.out.println("\nCatalog Management System");
            System.out.println("1. View Items");
            System.out.println("2. Add Item");
            System.out.println("3. Edit Item");
            System.out.println("4. Save and Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewItems();
                case 2 -> addItem(scanner);
                case 3 -> editItem(scanner);
                case 4 -> {
                    saveCatalog();
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static boolean authenticateUser(Scanner scanner) {
        final String USERNAME = "admin";
        final String PASSWORD = "password123";

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        return USERNAME.equals(username) && PASSWORD.equals(password);
    }

    private static void loadCatalog() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    catalog.add(new Item(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading catalog file: " + e.getMessage());
        }
    }

    private static void viewItems() {
        if (catalog.isEmpty()) {
            System.out.println("No items in the catalog.");
            return;
        }

        System.out.println("\nCatalog Items:");
        for (int i = 0; i < catalog.size(); i++) {
            System.out.println((i + 1) + ". " + catalog.get(i));
        }
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Enter item ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        System.out.print("Enter item description: ");
        String description = scanner.nextLine();

        if (id.isEmpty() || name.isEmpty() || description.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        catalog.add(new Item(id, name, description));
        System.out.println("Item added successfully.");
    }

    private static void editItem(Scanner scanner) {
        viewItems();

        System.out.print("Enter the number of the item to edit: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (index < 0 || index >= catalog.size()) {
            System.out.println("Invalid item number.");
            return;
        }

        Item item = catalog.get(index);

        System.out.print("Enter new name (current: " + item.getName() + "): ");
        String name = scanner.nextLine();

        System.out.print("Enter new description (current: " + item.getDescription() + "): ");
        String description = scanner.nextLine();

        if (!name.isEmpty()) {
            item.setName(name);
        }
        if (!description.isEmpty()) {
            item.setDescription(description);
        }

        System.out.println("Item updated successfully.");
    }

    private static void saveCatalog() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Item item : catalog) {
                bw.write(item.toCSV());
                bw.newLine();
            }
            System.out.println("Catalog saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving catalog: " + e.getMessage());
        }
    }

    static class Item {
        private String id;
        private String name;
        private String description;

        public Item(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String toCSV() {
            return id + "," + name + "," + description;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Description: " + description;
        }
    }
}
