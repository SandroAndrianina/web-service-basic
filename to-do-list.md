**Milestones (CI4 + Servlet/JSP + HttpClient + JDBC/MySQL)**

**Phase 1 – PHP (data provider)**
1. Create the CI4 project and run it locally.
2. Create the file `provinces.csv` (id, name) in `writable/`.
3. Build a Provinces controller that reads the CSV and returns JSON.
4. Add the route `GET /api/provinces` (optionally `GET /api/provinces/{id}`).
5. Test the endpoint in the browser or Postman.

**Phase 2 – Database (MySQL)**
6. Create the database and the table `students(id, name, age, province_id)`.

**Phase 3 – Java (business logic)**
7. Create the Tomcat web project (Servlet + JSP, JDBC driver added).
8. Model: `Student` class.
9. DAO layer: JDBC connection + CRUD (create, read all, read one, update, delete).
10. Service for the PHP call: `HttpClient` that fetches `/api/provinces` and parses the JSON into a list.
11. Business service: validation (name not empty, age valid, province exists) and links DAO and the province service.
12. Servlet controller: routes list / new / edit / delete / save.

**Phase 4 – JSP (client)**
13. `list.jsp`: table of students (show the province name, resolved from the PHP data).
14. `form.jsp`: create and edit form, with the province dropdown filled from the PHP API.
15. Navigation and basic error messages.

**Phase 5 – Integration and tests**
16. Start PHP and Tomcat together; test full CRUD.
17. Test failure cases (PHP down, empty CSV, invalid input).

Want me to detail any phase, or start with Phase 1?