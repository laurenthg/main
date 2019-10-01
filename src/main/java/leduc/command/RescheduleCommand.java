package leduc.command;

import leduc.Ui;
import leduc.exception.*;
import leduc.storage.Storage;
import leduc.task.EventsTask;
import leduc.task.Task;
import leduc.task.TaskList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents Reschedule command which reschedule the period of a event task.
 */
public class RescheduleCommand extends Command {
    /**
     * Constructor of RescheduleCommand.
     * @param user String which represent the input string of the user.
     *
     */
    public RescheduleCommand(String user){
        super(user);
    }

    /**
     *
     * Allows to reschedule the period of a event task.
     *
     * @param tasks leduc.task.TaskList which is the list of task.
     * @param ui leduc.Ui which deals with the interactions with the user.
     * @param storage leduc.storage.Storage which deals with loading tasks from the file and saving tasks in the file.
     * @throws EmptyEventDateException Exception caught when the period of the event task is not given by the user.
     * @throws NonExistentTaskException Exception caught when the task does not exist.
     * @throws EventTypeException Exception caught when the task is not a event task while it should be.
     * @throws NonExistentDateException Exception caught when the date given does not exist.
     * @throws DateComparisonEventException Exception caught when the second date is before the first one.
     * @throws FileException Exception caught when the file doesn't exist or cannot be created or cannot be opened.
     */
    public void execute(TaskList tasks, Ui ui , Storage storage) throws EmptyEventDateException,
            NonExistentTaskException, EventTypeException, NonExistentDateException,
            DateComparisonEventException, FileException {
        String[] rescheduleString = user.substring(11).split("/at");
        if (rescheduleString.length == 1) { // no /by in input
            throw new EmptyEventDateException();
        }
        int index = -1;
        try {
            index = Integer.parseInt(rescheduleString[0].trim()) - 1;
        }
        catch(Exception e){
            throw new NonExistentTaskException();
        }
        if (index > tasks.size() - 1 || index < 0) {
            throw new NonExistentTaskException();
        }
        else { // the tasks exist
            Task rescheduleTask = tasks.get(index);
            if (!rescheduleTask.isEvent()){
                throw new EventTypeException();
            }
            EventsTask rescheduleEventTask = (EventsTask) rescheduleTask;
            String[] dateString = rescheduleString[1].split(" - ");
            if(dateString.length == 1){
                throw new EmptyEventDateException();
            }
            else if(dateString[0].isBlank() || dateString[1].isBlank()){
                throw new EmptyEventDateException();
            }
            LocalDateTime d1 = null;
            LocalDateTime d2 = null;
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.ENGLISH);
                d1 = LocalDateTime.parse(dateString[0].trim(), formatter);
                d2 = LocalDateTime.parse(dateString[1].trim(), formatter);
            }catch(Exception e){
                throw new NonExistentDateException();
            }

            rescheduleEventTask.reschedule(d1,d2);
            storage.save(tasks.getList());
            ui.display("\t Noted. I've rescheduled this task: \n" +
                    "\t\t "+rescheduleEventTask.getTag() + rescheduleEventTask.getMark() + " " +
                    rescheduleEventTask.getTask()+ " at:" + rescheduleEventTask.getDateFirst() +
                    " - " + rescheduleEventTask.getDateSecond() + "\n");
        }
    }

    /**
     * Returns a boolean false as it is a RescheduleCommand.
     * @return a boolean false.
     */
    public boolean isExit(){
        return false;
    }

}