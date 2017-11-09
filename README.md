# Java Menu Utility       


Description
-----------
A Java library used to create, save and load text based menus for use in other text based applications.
This Menu library is meant to be as open ended as possible to allow the developer the most amount of flexibility.
The saving and loading of Menus is done using JSON, in order to easily create and edit Menus without touching code.

TextMenu-1-1-0.jar contains the Menu and InvalidMemberException classes necessary to use this library.
javax.json-1-0-4.jar contains the JSON library necessary to save and load Menus.


Example
-------
```java
//Menu(String title, String[] options, boolean exitingAllowed, boolean exitOption, String exitText)
Menu m = new Menu("Title", new String[] {"Option 1", "Option 2", "Option 3"}, true, 0, "Exit");
do {
  m.display().getInput();
  System.out.println("User chose option: " + m.getChoice());
} while (!m.didChooseExit());
```
  
In the code above an exitable Menu is created with three options where its exit option is option 0.
The menu is then printed to the standard output and waits for user input from standard input.
It prints out the option chosen and repeats as long as the exit option was not chosen.


Licenses
--------
Text Menu: GNU GPL v3.0
JSON: http://www.json.org/license.html


Getting The Library
-------------------
Download the full library and its dependencies [here](http://download1516.mediafireuserdownload.com/bkw715ztktgg/bawb3bav2z7muz7/TextMenu-1-0-0.zip)

###Includes
* Java Menu Library (TextMenu-1-0-0.jar)
* Java Menu Sources (TextMenu-1-0-0-sources.jar)
* External Libraries
  * JSON Library (javax.json-1.0.4.jar)
  * JSON Sources (javax.json-api-1.0-sources.jar)

Using The Library
-----------------
To include this utility in an existing Java project, add TextMenu-1-0-0.jar and its external dependencies (javax.json-1.0.4.jar) to the Java build path. If desired attach their sources as well.


Feedback
--------
Report bugs or any other issues at  https://github.com/HotPocketAdventure/Java-Menu/issues
Follow the development on Trello at  https://trello.com/b/FepK5x2n/java-menu


Connect
-------
Email me with feedback or anything else at  mbrad94@gmail.com
~~Visit my website at  http://hotpocketadventure.tk/~~
Follow my development blog at  http://mikedevving.tumblr.com/
