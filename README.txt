compile jdbc in terminal:
javac -d bin -cp ".;lib/mysql-connector-j-8.4.0.jar" *.java    //compile
java -cp "bin;lib/mysql-connector-j-8.4.0.jar" DBTest     //test if it works
javac -encoding UTF-8 -cp "lib/jcalendar-1.4.jar" -d bin *.java