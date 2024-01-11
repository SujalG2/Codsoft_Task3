package Practice;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

class Student implements Serializable {
    private String name;
    private int rollNumber;
    private char grade;

    public Student(String name, int rollNumber, char grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public char getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade;
    }
}

class StudentManagementSystem implements Serializable {
    private java.util.List<Student> students;

    public StudentManagementSystem() {
        this.students = new java.util.ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
    }

    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    public void displayAllStudents() {
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            students = (java.util.List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

public class StudentManagementSystemGUI extends JFrame {
    private StudentManagementSystem system = new StudentManagementSystem();
    private JTextField nameField, rollNumberField, gradeField;
    private JTextArea outputArea;

    public StudentManagementSystemGUI() {
        super("Student Management System");
        setLayout(new GridLayout(8, 2, 10, 10));

        nameField = new JTextField();
        rollNumberField = new JTextField();
        gradeField = new JTextField();
        outputArea = new JTextArea();

        JButton addButton = new JButton("Add Student");
        JButton removeButton = new JButton("Remove Student");
        JButton searchButton = new JButton("Search Student");
        JButton displayButton = new JButton("Display All Students");
        JButton saveButton = new JButton("Save to File");
        JButton loadButton = new JButton("Load from File");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int rollNumber = Integer.parseInt(rollNumberField.getText());
                char grade = gradeField.getText().charAt(0);

                system.addStudent(new Student(name, rollNumber, grade));
                outputArea.append("Student added: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade + "\n");
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rollToRemove = Integer.parseInt(rollNumberField.getText());
                system.removeStudent(rollToRemove);
                outputArea.append("Student removed: Roll Number " + rollToRemove + "\n");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rollToSearch = Integer.parseInt(rollNumberField.getText());
                Student foundStudent = system.searchStudent(rollToSearch);
                if (foundStudent != null) {
                    outputArea.append("Student found: " + foundStudent + "\n");
                } else {
                    outputArea.append("Student not found.\n");
                }
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
                system.displayAllStudents();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String saveFileName = rollNumberField.getText(); // Use Roll Number as the file name for simplicity
                system.saveToFile(saveFileName + ".dat");
                outputArea.append("Data saved to file: " + saveFileName + ".dat\n");
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loadFileName = rollNumberField.getText(); // Use Roll Number as the file name for simplicity
                system.loadFromFile(loadFileName + ".dat");
                outputArea.append("Data loaded from file: " + loadFileName + ".dat\n");
            }
        });

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Roll Number:"));
        add(rollNumberField);
        add(new JLabel("Grade:"));
        add(gradeField);

        add(addButton);
        add(removeButton);
        add(searchButton);
        add(displayButton);
        add(saveButton);
        add(loadButton);

        add(new JLabel("Output:"));
        add(outputArea);
    }

    public static void main(String[] args) {
        StudentManagementSystemGUI gui = new StudentManagementSystemGUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(500, 400);
        gui.setVisible(true);
    }
}
