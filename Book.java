public class Book {
    String id, title, author, category;
    int quantity;

    public Book(String id, String title, String author, String category, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
    }

    public String toString() {
        return id + " | " + title + " | " + author + " | " + category + " | Copies: " + quantity;
    }
}