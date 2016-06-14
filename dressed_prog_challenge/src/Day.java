
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by alex on 6/10/16.
 */
public class Day {

    //data structure class which takes in the information from the input file
    //and stores it into 3 strings. This class is placed into an arrayList structure
    //in order for the public class to do the work it needs
    private class Entry
    {
        private String command;
        private String ht_responce;
        private String cd_responce;
        private String description;
    }

    private String type;                //hot or cold identifier

    private final int min_command = 1;
    private int max_command;            //set based on the type

    //entries added ready for output if they pass the criterion
    //holds the working commands which were entered by the user
    ArrayList<Entry> output;

    //Entries that come from the file -- maintained
    //container which holds all of the commands read from the input file
    ArrayList<Entry> file_input;

    //class default constructor
    //creates the instances of the containers
    public Day()
    {
        //instantiate the arrays
        output = new ArrayList<>();
        file_input = new ArrayList<>();
    }

    //function opens the commands file and process all of the
    //information from it filling up the file_input array list
    public void process_commands_file(String file_name)
    {
        //get a file handle
        File commands_file = new File(file_name);

        Scanner cmd_input = null;
        try{
            cmd_input = new Scanner(commands_file);
        }
        catch(Exception ex)
        {
            System.out.println("Input file for commands cannot be opened! Exiting!!!");
            System.exit(-1);
        }

        System.out.println("Commands file successfully opened and processed!");

        //temporary storage for each line from the file
        String line;

        String tokens[];

        while(cmd_input.hasNext()) {

            line = cmd_input.nextLine();

            if (line.equals(""))
                continue;

            //split the line from the file
            tokens = line.split("[,]");

            //construct the new entry
            Entry temp = new Entry();

            //set the data
            temp.command = tokens[0];
            temp.description = tokens[1];
            temp.ht_responce = tokens[2];
            temp.cd_responce = tokens[3];

            //add it to the array
            file_input.add(temp);
        }

    }

    //function takes in the command from the user and parses it into tokens
    //it first fills in the type in the class (hot/cold)
    //and returns a string array of parsed tokens
    private String[] parse_command(String user_command)
    {
        //make sure type is reset to an empty string
        type = "";

        //make sure output array is clear before evaluation starts
        output.clear();

        try
        {
            type = user_command.substring(0, user_command.indexOf(" ")).toLowerCase();

            if (type.equals("hot"))
            {
                //the maximum number of garments is 6 for warm w
                max_command = 6;
            }
            else if (type.equals("cold"))
            {
                //maximum garments 8
                max_command = 8;
            }
            else
            {
                throw new IllegalArgumentException("Incorrect day type entered! Weather can be only hot or cold!");
            }
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Command must begin with HOT or COLD designation!");
        }

        //removing hot/cold identifier
        user_command = user_command.substring(user_command.indexOf(" ")+1);

        //split the commma delimited list
        String [] tokens = user_command.split("[,]");

        //return the parsed array
        return tokens;
    }

    //function takes in a command and retrieves an entry from the file_input
    //arrayList so that it can be placed in to the output arrayList
    private Entry get_entry(String cmd)
    {
        for(Entry element: file_input)
        {
            if(element.command.equals(cmd))
            {
                return element;
            }
        }

        return null;
    }

    //contains function checks if the output arraylist contains a specific
    //command given by the user this is used when checks are needed for
    //whether specific clothing items have been put on before others
    private boolean contains(String command)
    {
        //if output vector is empty return false
        if(output.size() == 0)
            return false;

        //step through each one of the entries in the output vector
        //evaluate if the command is already present and return true
        //if it is
        for(Entry element: output)
        {
            if(element.command.equals(command))
            {
                return true;
            }

        }

        //return false by default
        return false;
    }

    //print function prints the data successfully added to the output list
    //it takes in a boolean value if the logic has run into an error
    //this means that the items are printed just before an error is thrown
    private void print(boolean error)
    {
        //go through the accumulated output repsonces
        //array and print the results -- error will
        //be true if there will be an error thrown right after
        //the print or false if the logic was successfully navigated

        for (int i = 0; i < output.size(); i++)
        {
            if(type.equals("hot")) {
                System.out.print(output.get(i).ht_responce);
            }
            else
            {
                System.out.print(output.get(i).cd_responce);
            }

            //do not print the last comma in the output
            if(i <= output.size()-2)
            {
                System.out.print(", ");
            }
        }

        //if error will be thrown print the error
        if(error)
           System.out.println(", fail");
    }

    //evaluate takes a user command string and performs every and all checks
    //which are necessary in order to find out if the command is valid
    //this function does throw errors if if there are problems with the
    //required items when getting dressed. They are caught by main and displayed
    //just before the program asks for another line of input
    public void evaluate(String user_command)
    {
        //parse the tokens
        String [] tokens = parse_command(user_command);

        //temporary entry to use as a handle on entries returned from methods
        Entry temp;

        for(int i = 0; i < tokens.length; i++)
        {
            temp = get_entry(tokens[i]);

            //if command was not found -- incorrect user input throw an error
            if (temp == null)
                throw new IllegalArgumentException("Incorrect command: valid command 1 - 8, one at a time!");

            //check for duplicate commands
            if(contains(temp.command))
                throw new IllegalArgumentException("Only one layer of the same clothing is allowed!");

            //first command remove pjs
            if(!tokens[0].equals("8"))
            {
                throw new IllegalArgumentException("Pajamas must be taken off first before anything");
            }

            switch (tokens[i])
            {
                case "1":
                    //if the command was to add socks!
                    if(type.equals("hot") && !contains("6"))
                    {
                        print(true);
                        throw new IllegalArgumentException("Cannot put shoes until pants have been put on!");
                    }
                    else if (type.equals("cold") && !contains("3"))
                    {
                        print(true);
                        throw new IllegalArgumentException("Shoes need to be on after socks and pants!");
                    }

                    break;

                case "2":
                    //if command is headwear
                    if(!contains("4"))
                    {
                        print(true);
                        throw new IllegalArgumentException("Cannot put headwear on before shirt is on!");
                    }

                    break;

                case "3":
                    //no restriction on anything to be put before socks
                    break;

                case "4":
                    //no restriction on anything to be put before the shirt
                    break;

                case "5":

                    if(!contains("4"))
                    {
                        print(true);
                        throw new IllegalArgumentException("Cannot put jacket on before shirt is on!");
                    }

                    break;

                case "6":
                    //no restriction on anything to be put before the pants
                    break;

                case "7":
                    //leaving house make sure that when hot there are 6 items that are on
                    //when it is cold there are 8 items that are on
                    if(output.size() != max_command-1)
                    {
                        print(true);
                        throw new IllegalArgumentException("Cannot leave the house without all of the items!");
                    }
                    break;

                case "8":
                    //nothing to do for removing pjs break out
                    break;

                default:
                    //if flow reaches default a command that is not in the list was given
                    //throw an error
                    throw new IllegalArgumentException("Incorrect command: valid command 1 - 8, one at a time!");
            }

            //add the entry to output vector
            output.add(temp);
        }

        //successful completion of the input - print the output and wait for the next output
        print(false);

    }


}
