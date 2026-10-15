package main.java.dao;

import main.java.util.DbConnection;
import main.java.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    // create
    public static void insert(Student std) throws SQLException {
        String sql = "INSERT INTO students (name, age, province_id) VALUES (?, ?, ?)";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, std.getName());
            ps.setInt(2, std.getAge());
            ps.setInt(3, std.getProvinceId());

            ps.executeUpdate();
        }
    }

    // read all
    public static List<Student> selectAll() throws SQLException {
        String sql = "SELECT id, name, age, province_id FROM students";
        List<Student> list = new ArrayList<>();

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getInt("province_id")
                ));
            }
        }
        return list;
    }

    // read by id
    public static Student selectById(int id) throws SQLException {
        String sql = "SELECT id, name, age, province_id FROM students WHERE id = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getInt("province_id")
                    );
                }
            }
        }
        return null; // not found
    }

    // update
    public static void update(Student std) throws SQLException {
        String sql = "UPDATE students SET name = ?, age = ?, province_id = ? WHERE id = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, std.getName());
            ps.setInt(2, std.getAge());
            ps.setInt(3, std.getProvinceId());
            ps.setInt(4, std.getId());

            ps.executeUpdate();
        }
    }

    // delete by id
    public static void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}