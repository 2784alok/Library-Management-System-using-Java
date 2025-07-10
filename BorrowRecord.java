import java.time.LocalDate;

public class BorrowRecord {
    String studentName;
    String bookID;
    LocalDate borrowDate;

    public BorrowRecord(String studentName, String bookID) {
        this.studentName = studentName;
        this.bookID = bookID;
        this.borrowDate = LocalDate.now();
    }

    public String toString() {
        return studentName + " borrowed " + bookID + " on " + borrowDate;
    }
}