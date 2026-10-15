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
    List<Student> students = (List<Student>) request.getAttribute("students");
    List<Province> provinces = (List<Province>) request.getAttribute("provinces");
    String error = (String) request.getAttribute("error");

    // province id -> province name (data coming from PHP)
    Map<Integer, String> provinceNames = new HashMap<>();
    if (provinces != null) {
        for (Province p : provinces) provinceNames.put(p.getId(), p.getName());
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Students</title>
    <style>
        body { font-family: sans-serif; margin: 40px; }
        table { border-collapse: collapse; width: 100%; max-width: 700px; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background: #f2f2f2; }
        .error { color: #b00020; margin-bottom: 16px; }
        a.btn { display: inline-block; margin-bottom: 16px; }
    </style>
</head>
<body>
    <h1>Students</h1>

    <% if (error != null) { %>
        <div class="error"><%= esc(error) %></div>
    <% } %>

    <a class="btn" href="students?action=new">+ New student</a>

    <table>
        <tr>
            <th>ID</th><th>Name</th><th>Age</th><th>Province</th><th>Actions</th>
        </tr>
        <% if (students == null || students.isEmpty()) { %>
        <tr><td colspan="5">No students.</td></tr>
        <% } else {
            for (Student s : students) {
                String pName = provinceNames.get(s.getProvinceId());
                if (pName == null) pName = "Unknown (" + s.getProvinceId() + ")";
        %>
        <tr>
            <td><%= s.getId() %></td>
            <td><%= esc(s.getName()) %></td>
            <td><%= s.getAge() %></td>
            <td><%= esc(pName) %></td>
            <td>
                <a href="students?action=edit&id=<%= s.getId() %>">Edit</a> |
                <a href="students?action=delete&id=<%= s.getId() %>"
                   onclick="return confirm('Delete this student?');">Delete</a>
            </td>
        </tr>
        <% } } %>
    </table>
</body>
</html>