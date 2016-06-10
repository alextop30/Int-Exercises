/**
 * Created by alex on 6/9/16.
 */

//import statements
import java.util.Scanner;
import java.io.File;

public class main {
    public static void main(String[] args)
    {
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
        Scanner in_file;


        //get the input value from the user - file path or command
        String user_input = keyboard.nextLine();

        //check if input file was provided
        if(user_input.contains("/"))
        {
            try
            {
                //get the input file
                input_file = new File(user_input);

                //set the input file scanner object for io
                in_file = new Scanner(input_file);

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

                    //construct the temperature object
                    temp today = new temp();

                    try {
                        //start the day with the input
                        today.start_day(line);
                    }
                    catch(Exception except)
                    {
                        //if any problems occur report them standard out
                        System.out.println(except.getMessage());
                    }

                    System.out.println("\n\n");

                }
            }
            catch(Exception ex)
            {
                //handle all exceptions with exit message
                System.out.println("File does not exist or is unreadable!");

                //print the trace
                ex.printStackTrace();

                //exit with error
                System.exit(-1);
            }

        }
        else
        {
            while (!user_input.toLowerCase().equals("quit") && !user_input.toLowerCase().equals("exit")) {

                //construct the temperature object
                temp today = new temp();

                //handle exceptions resulting in unsuccessful commands
                try {
                    today.start_day(user_input);
                } catch (Exception e) {
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
