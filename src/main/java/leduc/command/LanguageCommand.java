package leduc.command;

import leduc.Ui;
import leduc.exception.DukeException;
import leduc.exception.EmptyArgumentException;
import leduc.storage.Storage;
import leduc.task.TaskList;

public class LanguageCommand extends Command {

    private static String languageShortcut = "language";

    /**
     * Constructor of Command.
     *
     * @param user String which represent the input string of the user.
     */
    public LanguageCommand(String user) {
        super(user);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        String userSubstring;
        if(callByShortcut){
            userSubstring = user.substring(LanguageCommand.languageShortcut.length());
        }
        else {
            userSubstring = user.substring(8);
        }
        if(userSubstring.isBlank()){
            throw new EmptyArgumentException();
        }
        if(userSubstring.equals("fr")){
            storage.setLanguage(userSubstring);
        }
        else{
            storage.setLanguage("en");
        }
        storage.saveConfig();
    }

    /**
     * getter because the shortcut is private
     * @return the shortcut name
     */
    public static String getLanguageShortcut() {
        return languageShortcut;
    }

    /**
     * used when the user want to change the shortcut
     * @param languageShortcut the new shortcut
     */
    public static void setLanguageShortcut(String languageShortcut) {
        LanguageCommand.languageShortcut = languageShortcut;
    }
}
