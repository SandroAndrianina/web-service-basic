Since the goal is to test yourself, you could read only the steps and try the code on your own first. Here is everything.

## Step 1: PHP route (`app/Config/Routes.php`)

```php
$routes->post('api/province', 'ProvinceController::add');
```

## Step 2: PHP method (in `ProvinceController.php`)

```php
public function add(): ResponseInterface
{
    $data = $this->request->getJSON(true);
    $name = mb_strtoupper(trim($data['name'] ?? ''));

    if ($name === '') {
        return $this->response->setStatusCode(400)
            ->setJSON(['error' => 'Name is required.']);
    }

    $handle = fopen(WRITEPATH . 'provinces.txt', 'c+');
    flock($handle, LOCK_EX); // lock: no two writes at the same time

    $lastId = 0;
    fgetcsv($handle, 0, ';', '"', ''); // skip the header
    while (($row = fgetcsv($handle, 0, ';', '"', '')) !== false) {
        if (count($row) < 2) continue;

        if (mb_strtoupper($row[1]) === $name) {
            flock($handle, LOCK_UN);
            fclose($handle);
            return $this->response->setStatusCode(409)
                ->setJSON(['error' => 'This province already exists.']);
        }
        $lastId = max($lastId, (int) $row[0]);
    }

    $newId = $lastId + 1;

    // make sure the file ends with a newline before appending
    fseek($handle, -1, SEEK_END);
    if (fread($handle, 1) !== "\n") {
        fwrite($handle, "\n");
    }
    fseek($handle, 0, SEEK_END);
    fputcsv($handle, [$newId, $name], ';', '"', '');

    flock($handle, LOCK_UN);
    fclose($handle);

    return $this->response->setStatusCode(201)
        ->setJSON(['id' => $newId, 'name' => $name]);
}
```

The lock is also the bonus: two simultaneous POSTs can't get the same id.

## Step 3: test PHP alone, before Java

```bash
curl -i -X POST http://127.0.0.1:8081/api/province -H "Content-Type: application/json" -d '{"name":"sava"}'
curl -i -X POST http://127.0.0.1:8081/api/province -H "Content-Type: application/json" -d '{"name":"SAVA"}'
curl -i -X POST http://127.0.0.1:8081/api/province -H "Content-Type: application/json" -d '{"name":""}'
```

Expected: `201`, then `409`, then `400`.

## Step 4: Java (`ProvinceClient.java`)

Add the imports `com.google.gson.JsonParser` and `java.util.Map`, then add this method (it reuses a client built once):

```java
private static final HttpClient CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .build();

public static Province add(String name) throws IOException, InterruptedException {
    String body = new Gson().toJson(Map.of("name", name == null ? "" : name));

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

    HttpResponse<String> response =
            CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    int status = response.statusCode();

    if (status == 201) {
        return new Gson().fromJson(response.body(), Province.class);
    }
    if (status == 400 || status == 409) {
        String msg = JsonParser.parseString(response.body())
                .getAsJsonObject().get("error").getAsString();
        throw new IllegalArgumentException(msg);
    }
    throw new IOException("PHP API returned " + status);
}
```

## Step 5: Servlet (`StudentServlet.java`)

In `doGet`, add this case in the `switch`:

```java
case "province-new":
    req.getRequestDispatcher("/province-form.jsp").forward(req, resp);
    break;
```

In `doPost`, put this at the very top, right after `req.setCharacterEncoding("UTF-8");`:

```java
if ("province-save".equals(req.getParameter("action"))) {
    try {
        ProvinceClient.add(req.getParameter("name"));
        resp.sendRedirect(req.getContextPath() + "/students?action=new");
    } catch (Exception e) {
        req.setAttribute("error", e.getMessage());
        req.getRequestDispatcher("/province-form.jsp").forward(req, resp);
    }
    return;
}
```

Add `import main.java.service.ProvinceClient;` at the top. After a success, the user lands on the new-student form, where the dropdown already shows the new province, since it is fetched from PHP on each load.

## Step 6: `province-form.jsp` (in `webapp/`)

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>New province</title>
    <style>
        body { font-family: sans-serif; margin: 40px; }
        input { padding: 6px; width: 260px; }
        .error { color: #b00020; margin-bottom: 16px; }
        button { padding: 8px 16px; }
    </style>
</head>
<body>
    <h1>New province</h1>

    <% if (error != null) { %>
        <div class="error"><%= esc(error) %></div>
    <% } %>

    <form method="post" action="students">
        <input type="hidden" name="action" value="province-save">
        <input type="text" name="name" placeholder="Province name" required>
        <button type="submit">Add</button>
        <a href="students">Cancel</a>
    </form>
</body>
</html>
```

## Step 7: link and full test

- In `form.jsp`, under the dropdown, add: `<a href="students?action=province-new">+ New province</a>`.
- Run `./deploy.sh`. PHP doesn't need a restart.
- Check the 4 criteria: add "sava" (appears in the file and dropdown), add "sava" again (error shown), add an empty name (error shown; the browser's `required` may block it first, so test it with curl), restart PHP (nothing lost).

**Notes**
- The name is stored in uppercase to match your existing data (ANTANANARIVO...), and the duplicate check ignores case.
- I passed the 5th argument `''` to `fgetcsv` and `fputcsv` because PHP 8.4+ deprecates leaving it out. Your `showHeaders` still works, but you can add it there too.