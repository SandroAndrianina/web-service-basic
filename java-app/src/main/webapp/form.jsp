<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="java.util.*, main.java.model.Student, main.java.model.Province" %>
<%!
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    Student student = (Student) request.getAttribute("student");
    List<Province> provinces = (List<Province>) request.getAttribute("provinces");
    String error = (String) request.getAttribute("error");

    boolean editing = student != null && student.getId() > 0;
    int id = student != null ? student.getId() : 0;
    String name = student != null ? student.getName() : "";
    String age = (student != null && student.getAge() > 0) ? String.valueOf(student.getAge()) : "";
    int selectedProvince = student != null ? student.getProvinceId() : 0;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= editing ? "Edit" : "New" %> student</title>
    <style>
        body { font-family: sans-serif; margin: 40px; }
        label { display: block; margin-top: 12px; }
        input, select { padding: 6px; width: 260px; }
        .error { color: #b00020; margin-bottom: 16px; }
        button { margin-top: 20px; padding: 8px 16px; }
    </style>
</head>
<body>
    <h1><%= editing ? "Edit student" : "New student" %></h1>

    <% if (error != null) { %>
        <div class="error"><%= esc(error) %></div>
    <% } %>

    <form method="post" action="students">
        <input type="hidden" name="id" value="<%= id %>">

        <label>Name
            <input type="text" name="name" value="<%= esc(name) %>" required>
        </label>

        <label>Age
            <input type="number" name="age" min="1" max="120" value="<%= age %>" required>
        </label>

        <label>Province
            <select name="province_id" required>
                <option value="">-- Select --</option>
                <% if (provinces != null) {
                    for (Province p : provinces) { %>
                    <option value="<%= p.getId() %>"
                        <%= p.getId() == selectedProvince ? "selected" : "" %>>
                        <%= esc(p.getName()) %>
                    </option>
                <% } } %>
            </select>
        </label>

        <button type="submit">Save</button>
        <a href="students">Cancel</a>
    </form>
</body>
</html>