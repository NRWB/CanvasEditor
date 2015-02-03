# CanvasEditor
Help in parsing files
Quick Start Steps:

# Step 1
Place the CanvasEditor.java file and the submissions zip file into a folder. Extract the files from the zip folder (as seen in picture below).
![Alt Step 1](https://raw.githubusercontent.com/NRWB/CanvasEditor/master/resources/Step%20001.png)

# Step 2
Open up a command prompt (for windows users) or a terminal (for unix users).
![Alt Step 2](https://raw.githubusercontent.com/NRWB/CanvasEditor/master/resources/Step%20002.png)

# Step 3
With the given prompt/terminal, change to the current directory of the CanvasEditor.java file, the submissions.zip file, and the submissions folder (that was extracted in earlier steps). For example, "cd /home/usr/Documents/"
![Alt Step 3](https://raw.githubusercontent.com/NRWB/CanvasEditor/master/resources/Step%20003.png)

# Step 4
The prompt/terminal should now display the directory changed to earlier.
![Alt Step 4](https://raw.githubusercontent.com/NRWB/CanvasEditor/master/resources/Step%20004.png)

# Step 5
On assumption that java is present on system, type into the prompt/terminal, "javac CanvasEditor.java" in order to use the Java Programming Language Compiler.
![Alt Step 5](https://raw.githubusercontent.com/NRWB/CanvasEditor/master/resources/Step%20005.png)

# Step 6
After entering the last step, there should be no output. In this step, type into the prompt/terminal, "java CanvasEditor <path to file>" (if "java CanvasEditor" is typed, this command defaults to the working directory. This will not execute correctly with the given example, and the current CanvasEditor program)
![Alt Step 6](https://raw.githubusercontent.com/NRWB/CanvasEditor/master/resources/Step%20006.png)

# Step 7
Again, no output response after entering the command into the prompt/terminal is a good. That means there were no errors using that command.
![Alt Step 7](https://raw.githubusercontent.com/NRWB/CanvasEditor/master/resources/Step%20007.png)

# Step 8
Now, enter into the submissions folder and there should be a folder containing the newly edited files.
![Alt Step 8](https://raw.githubusercontent.com/NRWB/CanvasEditor/master/resources/Step%20008.png)

# Step 9
This shows a number of student folders created, each of which contains their respective files, renamed appropiately.
![Alt Step 9](https://raw.githubusercontent.com/NRWB/CanvasEditor/master/resources/Step%20009.png)

# *Notes:
-There are extra command line arguments currently implemented, but provide little benefits. This should change in later releases.

-This is currently coded for java source files only.

-This program does not check for if the renamed files match the internal class name (but this shouldn't matter unless the student does not name the internal class as the same name of the file).

-This program does not support extrating submitted zip file(s) and parsing the extracted compressed file(s) from a given student into their own respective directory.
