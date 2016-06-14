/**
 * Created by alex on 6/9/16.
 */

//import statements
import java.util.Scanner;
import java.io.File;

public class main {
    public static void main(String[] args)
    {
        //name of the commands file for processing by the class!
        final String cmd_file = "commands.txt";

        //welcome messages and directions
        System.out.println("Welcome, lets get started with the day!");
        System.out.println("Let us help you get ready!");
        System.out.println("An input file may also be specified given an absolute path to the file!\n\n");

        System.out.print("Please enter temperature HOT/COLD and steps to proceed! ");

        //set up scanner object for input from standard in
        Scanner keyboard = new Scanner(System.in);

        //construct a file object
        File input_file;

        //scanner object to rad an input file if passed by user
        Scanner in_file = null;


        //get the input value from the user - file path or command
        String user_input = keyboard.nextLine();

        //check if input file was provided
        if(user_input.contains("/"))
        {

            //construct the day object
            Day today = new Day();

            //process the commands file properly
            today.process_commands_file(cmd_file);

            //get the input file
            input_file = new File(user_input);

            try {

                //set the input file scanner object for io
                in_file = new Scanner(input_file);
            }
            catch(Exception exc)
            {
                //if opening the file fails print error and exit
                System.out.println("Unable to open file: " +
                        user_input.substring(user_input.lastIndexOf("/")+1));

                System.exit(-1);
            }

            //announce to the user that the file is open successfully
            System.out.println("File: " + user_input.substring(user_input.lastIndexOf("/")+1) +
                    " has been opened successfully!");

            //stores a line from the file
            String line;

            //go through each line of the input file
            while(in_file.hasNextLine())
            {
                //get a line from the file
                line = in_file.nextLine();

                //if a new line character is consumed continue to next iteration
                if(line.equals(""))
                    continue;

                //print out the requested line to the console
                System.out.println("Requested: " + line);

                //collect any exceptions from the evaluate function and
                //display them to the console
                try {
                    today.evaluate(line);
                }
                catch(Exception except)
                {
                    //if any problems occur report them standard out
                    System.out.println(except.getMessage());
                }

                System.out.println("\n\n");

            }
        }
        else
        {
            //construct the day object
            Day today = new Day();

            //process the commands file properly
            today.process_commands_file(cmd_file);

            while (!user_input.toLowerCase().equals("quit") && !user_input.toLowerCase().equals("exit")) {

                //handle exceptions resulting in unsuccessful commands
                try
                {
                    today.evaluate(user_input);
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }

                //Ask for new set of commands or quit
                System.out.print("\n\nEnter the next set of commands or type Exit or Quit: " );

                //obtain user input
                user_input = keyboard.nextLine();
            }
        }
    }
}
