# Listo! Your Assistant in Academics.
Version 1.3

Developers: Gabriel Rodriguez, Sharoon Thabal

This is a CPSC 233 Final Project. The project is a school work tracker inspired by the viral TikTok video 
that showcases how to manage your college workload using an excel sheet. With this app, you don't need to 
know any spreadsheets to manage your college life!  Listo will be enough and the best pick for you!

The zip package should include a jar file of the program, a javafx library for quick compilation, JUnit Tests, 
and the source code of the software. In the same zip package, you shall also find the UML, GitLab Link, sample input file, and the GUI Sketch of the program. Enjoy!

## Terminology
**Project** - represents an academic workload. (e.g. Assignments, Midterms, Quizzes). It is the basic unit
of everything in this program. You can assign names, dates, statuses, courses, etc. to a Project.

**Listo** - is a list of Projects. It is another unit in this program and it can have its own name, author, and description.
You are to create, load, update, and save a Listo in order to save your Projects. I suggest treating Listo's as if
they are semester files. A Listo can be optimally used as a unit for semestral duties. You can have a Listo for the Fall 2022
semester, Winter 2023 semester, etc. and you can even name the author of the Listo to truly personalize your experience!

## How to Use:
There are two ways in which you can this program:

You can either:
***
[1] Simply open the 'run.bat' file.
***
or:
[2] Go to command prompt and type the following commands:
```
java --module-path ".\javafx-sdk-18\lib" --add-modules javafx.controls,javafx.fxml -jar ListoApp.jar
```
Note that, to run the code above, your current working directory **MUST** be the same as where the ListoApp.jar file is located.


If not, then you can also consider running the following code:
```
java --module-path "PATHNAME FOR JAVAFX SDK" --add-modules javafx.controls,javafx.fxml -jar ListoApp.jar
```
This way, in order for the program to work, you must replace PATHNAME FOR JAVAFX SDK into the path that leads to your javafx-sdk folder.
***
### Run in Terminal with Arguments
[3] This program is also capable of being run with an argument passed in on the terminal.
The program can accept one or no arguments to be able to run. If you pass in more than one arguments,
then you should expect the program to not run at all and throw an error message.

The argument should be a .txt file that contains the data of a Listo in the save format. The program
will not run if the data is incompatible or missing, or simply if you have selected a non-txt file.

You can run the program through the following code:
As an example, we've provided you a sample input file named 'sampleInput.txt' but this can be any compatible input text files you want to load.
```
java --module-path ".\javafx-sdk-18\lib" --add-modules javafx.controls,javafx.fxml -jar ListoApp.jar sampleInput.txt
```

***
Cherry Rock Studios &copy; 2022