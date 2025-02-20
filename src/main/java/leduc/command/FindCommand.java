/* @@author rshah918 */
package leduc.command;

import leduc.exception.EmptyArgumentException;
import leduc.storage.Storage;
import leduc.ui.Ui;
import leduc.task.Task;
import leduc.task.TaskList;
import java.lang.*;
import java.util.ArrayList;
/**
 * Represents a Find Command.
 * Allow to find a specific task from the task list.
 */
public class FindCommand extends Command {
    /**
     * static variable used for shortcut
     */
    private static String findShortcut = "find";
    /**
     * Constructor of FindCommand.
     * @param userInput String which represent the input string of the user.
     */
    public FindCommand(String userInput){
        super(userInput);
    }
    /**
     * Finds the index of the maximum value in the arraylist
     * @param scores ArrayList containing relevance scores for all tasks in the list
     * @return returns index of the index of the maximum score in the list.
     */
    public int findMaxIndex(ArrayList<Double> scores){
        //index of scores correspond to the index of tasks in TaskList. To preserve indices,
            //processed scores are assigned a null value
        double nullDouble = -99.0;
        int nullInt = -99;
        double max = 0.0;
        int max_index = nullInt;

        for (int j = 0; j < scores.size(); j++) {
            if(scores.get(j) == 0.0){//tasks with no common characters will be flagged as null
                scores.set(j, nullDouble);
            }
            if (scores.get(j) > max) {//update max
                max = scores.get(j);
                max_index = j;
            }
        }
        return max_index;
    }
    /**
     * Compares each task description with the user query, generates a score from 0-1 based on how close the match is.
     * @param tasks leduc.task.TaskList which is the list of task.
     * @param find String that contains the user's query
     * @return returns an ArrayList containing relevance scores for each task description
     */
    public ArrayList<Double> generateRelevanceScores(String find, TaskList tasks){
        ArrayList<Double> scores = new ArrayList<Double>();
        double relevanceScore = 0.0;
        for (int i = 0; i < tasks.size(); i++) {
            double numMatches = 0;
            Task task = tasks.get(i);
            String description = task.getTask();
            double shortestStringLength = Math.min(description.length(), find.length());
            double longestStringLength = Math.max(description.length(), find.length());
            //use nested for loop to compare query and task description elementwise
            for (int j = 0; j < shortestStringLength; j++) {
                for (int k = 0; k < description.length(); k++) {
                    //compare characters, if they match, increment nummatches then break
                    if (find.charAt(j) == description.charAt(k)) {
                        numMatches += 1.0;
                        break;
                    }
                }
            }
            relevanceScore = numMatches / longestStringLength;
            double matchThreshold = 0.5;
            if(relevanceScore <= matchThreshold){
                relevanceScore = 0.0;
            }
            scores.add(relevanceScore);
        }
        return scores;
    }
    /**
     * Allow to find top relevant tasks from the task list by utilizing fuzzy matching algorithm.
     * @param tasks leduc.task.TaskList which is the list of task.
     * @param ui leduc.ui.Ui which deals with the interactions with the user.
     * @param storage leduc.storage.Storage which deals with loading tasks from the file and saving tasks in the file.
     * @throws EmptyArgumentException Exception caught when there is no argument
     */

    public void execute(TaskList tasks, Ui ui, Storage storage) throws EmptyArgumentException {
        String userSubstring;
        if(isCalledByShortcut){
            userSubstring = userInput.substring(FindCommand.findShortcut.length());
        }
        else {
            userSubstring = userInput.substring(4);
        }
        if(userSubstring.isBlank()){
            throw new EmptyArgumentException();
        }
        String find = userInput.substring(FindCommand.findShortcut.length()+1);
        ArrayList<Double> scores;
        //populate list of relevance scores
        scores = generateRelevanceScores(find, tasks);

        String result = "";
        int numResults = 5;
        //Add tasks to "String result" in the order of relevance.
        for(int i = 0; i < scores.size() && i < numResults; i++) {
            double nullDouble = -99.0;
            //find the index of the task that is most similar to the user query
            int max_index = findMaxIndex(scores);
            if(max_index > nullDouble) {
                result += tasks.displayOneElementList(max_index);
                //To preserve indices, previously sorted scores are replaced with a null value
                scores.set(max_index, nullDouble);
            }
            else{//all tasks are sorted
                break;
            }
        }
        if (result.isEmpty()) {
            ui.showFindNotMatching();

        } else {
            ui.showFindMatching(result);
        }
    }
/* @@author */
    /**
     * getter because the shortcut is private
     * @return the shortcut name
     */
    public static String getFindShortcut() {
        return findShortcut;
    }
    /**
     * used when the user want to change the shortcut
     * @param findShortcut the new shortcut
     */
    public static void setFindShortcut(String findShortcut) {
        FindCommand.findShortcut = findShortcut;
    }
}