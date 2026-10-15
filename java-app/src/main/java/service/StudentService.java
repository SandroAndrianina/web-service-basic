package main.java.service;

import main.java.dao.StudentDao;
import main.java.model.Province;
import main.java.model.Student;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StudentService {

    // ---------- READ ----------
    public static List<Student> getAll() throws SQLException {
        return StudentDao.selectAll();
    }

    public static Student getById(int id) throws SQLException {
        return StudentDao.selectById(id);
    }

    public static List<Province> getProvinces() throws IOException, InterruptedException {
        return ProvinceClient.getAll();
    }

    // ---------- CREATE ----------
    public static void create(Student std)
            throws SQLException, IOException, InterruptedException {
        validate(std);
        StudentDao.insert(std);
    }

    // ---------- UPDATE ----------
    public static void update(Student std)
            throws SQLException, IOException, InterruptedException {
        if (StudentDao.selectById(std.getId()) == null) {
            throw new IllegalArgumentException("Student not found.");
        }
        validate(std);
        StudentDao.update(std);
    }

    // ---------- DELETE ----------
    public static void delete(int id) throws SQLException {
        StudentDao.deleteById(id);
    }

    // ---------- VALIDATION ----------
    private static void validate(Student std) throws IOException, InterruptedException {
        if (std.getName() == null || std.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (std.getAge() <= 0 || std.getAge() > 120) {
            throw new IllegalArgumentException("Age must be between 1 and 120.");
        }
        boolean exists = false;
        for (Province p : ProvinceClient.getAll()) {
            if (p.getId() == std.getProvinceId()) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new IllegalArgumentException("Province does not exist.");
        }
    }
}