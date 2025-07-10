import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class LibraryGUI extends JFrame {
    ArrayList<Book> books = new ArrayList<>();
    ArrayList<BorrowRecord> records = new ArrayList<>();
    JTextArea displayArea = new JTextArea(12, 50);

    public LibraryGUI() {
        initGUI();
    }

    void initGUI() {
        setTitle("Library Management System");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton addBtn = new JButton("Add Book");
        JButton viewBtn = new JButton("View Books");
        JButton borrowBtn = new JButton("Borrow Book");
        JButton returnBtn = new JButton("Return Book");
        JButton searchBtn = new JButton("Search Book");
        JButton recordsBtn = new JButton("View Borrow Records");
        JButton sortBtn = new JButton("Sort Books");

        add(addBtn);
        add(viewBtn);
        add(borrowBtn);
        add(returnBtn);
        add(searchBtn);
        add(recordsBtn);
        add(sortBtn);
        add(new JScrollPane(displayArea));

        addBtn.addActionListener(e -> addBook());
        viewBtn.addActionListener(e -> viewBooks(books));
        borrowBtn.addActionListener(e -> borrowBook());
        returnBtn.addActionListener(e -> returnBook());
        searchBtn.addActionListener(e -> searchBook());
        recordsBtn.addActionListener(e -> viewRecords());
        sortBtn.addActionListener(e -> sortBooks());

        setVisible(true);
    }

    void addBook() {
        String id = JOptionPane.showInputDialog("Enter Book ID:");
        String title = JOptionPane.showInputDialog("Enter Book Title:");
        String author = JOptionPane.showInputDialog("Enter Author:");
        String category = JOptionPane.showInputDialog("Enter Category:");
        int qty = Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity:"));

        for (Book b : books) {
            if (b.id.equals(id)) {
                b.quantity += qty;
                JOptionPane.showMessageDialog(this, "Book already exists. Quantity updated.");
                return;
            }
        }

        books.add(new Book(id, title, author, category, qty));
        JOptionPane.showMessageDialog(this, "Book Added Successfully!");
    }

    void viewBooks(List<Book> list) {
        displayArea.setText("");
        if (list.isEmpty()) {
            displayArea.setText("No books available.");
            return;
        }
        for (Book b : list) displayArea.append(b.toString() + "\n");
    }

    void borrowBook() {
        String student = JOptionPane.showInputDialog("Enter Your Name:");
        String id = JOptionPane.showInputDialog("Enter Book ID to borrow:");
        for (Book b : books) {
            if (b.id.equals(id)) {
                if (b.quantity > 0) {
                    b.quantity--;
                    records.add(new BorrowRecord(student, id));
                    JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "No copies available.");
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Book ID not found.");
    }

    void returnBook() {
        String student = JOptionPane.showInputDialog("Enter Your Name:");
        String id = JOptionPane.showInputDialog("Enter Book ID to return:");
        for (Book b : books) {
            if (b.id.equals(id)) {
                b.quantity++;
                for (BorrowRecord r : records) {
                    if (r.bookID.equals(id) && r.studentName.equalsIgnoreCase(student)) {
                        long days = ChronoUnit.DAYS.between(r.borrowDate, LocalDate.now());
                        long fine = days > 7 ? (days - 7) * 2 : 0;
                        JOptionPane.showMessageDialog(this, "Book returned. Fine: ₹" + fine);
                        records.remove(r);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "No record found for this student and book.");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Book ID not found.");
    }

    void searchBook() {
        String keyword = JOptionPane.showInputDialog("Enter title/author/category to search:");
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.title.toLowerCase().contains(keyword.toLowerCase()) ||
                b.author.toLowerCase().contains(keyword.toLowerCase()) ||
                b.category.toLowerCase().contains(keyword.toLowerCase())) {
                results.add(b);
            }
        }
        viewBooks(results);
    }

    void viewRecords() {
        displayArea.setText("");
        if (records.isEmpty()) {
            displayArea.setText("No borrow records.");
            return;
        }
        for (BorrowRecord r : records) {
            displayArea.append(r.toString() + "\n");
        }
    }

    void sortBooks() {
        String[] options = { "Title", "Author", "Quantity" };
        String choice = (String) JOptionPane.showInputDialog(null, "Sort books by:", "Sort",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == null) return;

        Comparator<Book> comp = switch (choice) {
            case "Title" -> Comparator.comparing(b -> b.title.toLowerCase());
            case "Author" -> Comparator.comparing(b -> b.author.toLowerCase());
            case "Quantity" -> Comparator.comparingInt(b -> b.quantity);
            default -> null;
        };

        if (comp != null) {
            books.sort(comp);
            viewBooks(books);
        }
    }

    public static void main(String[] args) {
        new LibraryGUI();
    }
}