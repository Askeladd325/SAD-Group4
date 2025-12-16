compile jdbc in terminal:
javac -d bin -cp ".;lib/mysql-connector-j-8.4.0.jar" *.java    //compile
java -cp "bin;lib/mysql-connector-j-8.4.0.jar" DBTest     //test if it works



-----------FOR CLINIC MANAGEMENT ONLY! -------------
download java fx sdk and extract
download maven sdk and extract - > open the extracted folder find lib and copy path
search "edit system evironment variables in windows" -> environment variables -> in user variables click path then edit, add and paste the maven's lib path click ok
double check if it worked by going to cmd type: mvn -version
to run go to vscode terminal, cd ClinicManagement then type mvn clean install, after that type: mvn javafx:run
if you can't run compile first(ask chatgpt :D)

