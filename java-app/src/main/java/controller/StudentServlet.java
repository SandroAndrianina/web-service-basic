package main.java.controller;

import main.java.model.Student;
import main.java.service.StudentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    // ---------- GET: list / new / edit / delete ----------
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    req.setAttribute("provinces", StudentService.getProvinces());
                    req.getRequestDispatcher("/form.jsp").forward(req, resp);
                    break;

                case "edit":
                    int editId = Integer.parseInt(req.getParameter("id"));
                    req.setAttribute("student", StudentService.getById(editId));
                    req.setAttribute("provinces", StudentService.getProvinces());
                    req.getRequestDispatcher("/form.jsp").forward(req, resp);
                    break;

                case "delete":
                    StudentService.delete(Integer.parseInt(req.getParameter("id")));
                    resp.sendRedirect(req.getContextPath() + "/students");
                    break;

                default: // list
                    req.setAttribute("students", StudentService.getAll());
                    req.setAttribute("provinces", StudentService.getProvinces());
                    req.getRequestDispatcher("/list.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/list.jsp").forward(req, resp);
        }
    }

    // ---------- POST: save (create or update) ----------
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String idParam = req.getParameter("id");
        String name = req.getParameter("name");
        String ageParam = req.getParameter("age");
        String provinceParam = req.getParameter("province_id");

        Student std = new Student(0, name, 0, 0);

        try {
            int id = (idParam == null || idParam.isEmpty()) ? 0 : Integer.parseInt(idParam);
            int age = Integer.parseInt(ageParam);
            int provinceId = Integer.parseInt(provinceParam);
            std = new Student(id, name, age, provinceId);

            if (id == 0) StudentService.create(std);
            else StudentService.update(std);

            resp.sendRedirect(req.getContextPath() + "/students");

        } catch (Exception e) {
            // back to the form with the error and the entered values
            try {
                req.setAttribute("provinces", StudentService.getProvinces());
            } catch (Exception ignored) { }
            req.setAttribute("student", std);
            req.setAttribute("error", e instanceof NumberFormatException
                    ? "Age and province must be valid numbers." : e.getMessage());
            req.getRequestDispatcher("/form.jsp").forward(req, resp);
        }
    }
}