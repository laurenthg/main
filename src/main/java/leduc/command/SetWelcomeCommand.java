package leduc.command;

import leduc.Ui;
import leduc.exception.FileException;
import leduc.storage.Storage;
import leduc.task.TaskList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class SetWelcomeCommand extends Command{

    public SetWelcomeCommand(String user){
       super(user);
    }
    /**
     * Returns the String which will be displayed as the welcome message.
     * @return the String representing the next line of command of the user.
     */
    public static File openFile(String filepath) throws FileException {
        //open file, throw exception if the file doesnt exist.
        File file;
        file = new File(filepath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
    /**
     * Allow to change the welcome message.
     * @param tasks leduc.task.TaskList which is the list of task.
     * @param ui leduc.Ui which deals with the interactions with the user.
     * @param storage leduc.storage.Storage which deals with loading tasks from the file and saving tasks in the file.
     */
    public void execute(TaskList tasks, Ui ui , Storage storage) throws FileException {
        FileWriter fileWriter = null;
        String filepath = System.getProperty("user.dir")+ "/data/welcome.txt";//get location of welcome message file
        File file = openFile(filepath);
        //write the new message to the file
        try {
            fileWriter = new FileWriter(file);
            try {

                fileWriter.write(String.join(" ", Arrays.copyOfRange(user.split(" "), 1, user.split( " ").length)));
            }
            finally{
                fileWriter.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new FileException();
        }
    }
}
