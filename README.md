## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies
- currently the only dependency is the msql jdbc driver

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).



## STEPS TO GET PROJECT RUNNINIG:
1. You need to create a database called `museum_db` in your database software of choice on your machine.

2. In the 'DatabaseConnection.java' file in the src folder, setup your database connection variables, i.e. `USER` and `PASSWORD`.

3. Proceed to run the application from either the jar file or compile it from the InventoryManagement class which is the driver class.

4. If you want to test that the database connection is working properly, you can run the `TestDatabaseConnection.java` class and look in the terminal to see the message. If you have set up the project correctly you should see: 'Database connection successful!'