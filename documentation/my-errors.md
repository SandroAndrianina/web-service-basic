# ERROR1

    [ErrorException]
    Undefined variable $route
    at APPPATH/Config/Routes.php:8

    Lesson:
    ->toujours verifiers les noms de VARIABLE (ex: un petite erreur d'un caractere peut tout detruire)

# ERROR2

    ne pas inventer des types qui n'existe pas
    Lesson: 
    ->se documenter et verifier

# ERROR3

    fermer les ressources que l'on a ouvert 
    fopen(...) -> fclose(...)

# ERROR4 : je repetes toujours ce meme erreur

    erreur de syntaxe : meme un petit "$" peut tout bloquer
    ne rien oublie et bien regarder chaque caractere

# ERROR5 : erreur dans JDBC

    -pas de "this."" dans "main"
    -une fonction static utilise des valeurs static
    -try : contient une declaration de variable -> afficher l'erreur avec "e.getMessage()" avec un print
    -reference le chemin des librairies
