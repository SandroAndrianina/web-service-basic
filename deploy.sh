#!/bin/bash
CATALINA_HOME=/opt/homebrew/opt/tomcat/libexec   # adapt if needed
APP=java-app/src/main/webapp
NAME=web-service-basic

rm -rf $APP/WEB-INF/classes && mkdir -p $APP/WEB-INF/classes
javac -cp "$CATALINA_HOME/lib/servlet-api.jar:$APP/WEB-INF/lib/*" \
      -d $APP/WEB-INF/classes $(find java-app/src/main/java -name "*.java") || exit 1

rm -rf $CATALINA_HOME/webapps/$NAME
cp -R $APP $CATALINA_HOME/webapps/$NAME
echo "Deployed: http://localhost:8080/$NAME/students"