/**
 * Created by alex on 6/8/16.
 */

import java.io.File;
import java.util.*;

/*
    Class temperature, reaches out file commands.txt -- constant in_commands. It reads
    all of the commands from the file and stores them in an ArrayList of private classes
    containing all of the data members as needed. Private class can be altered very easily
    should the need arise.

    Private class constructor strips the input line on the comma delimiter and saves the information
    into each one of the data members.

    Private class get command_function (private function) returns the command as a string from the given class

    Private class get response function (private function) returns the response given
    weather type and command as arguments

    temp class public functions are the constructor and the start day function. The start day function is a
    wrapper for the check function (private) which make all of the considerations for getting dressed.
 */

public class temp
{

    //command data class is used to store the command information which is
    //read from a comma delimited list called commands.txt
    //class is utility holding the data information
    private class command_data{
        private String command;
        private String description;
        private String hot_responce;
        private String cold_responce;

        private command_data(String line)
        {
            String [] token = line.split("[,]");
            command = token[0];
            description = token[1];
            hot_responce = token[2];
            cold_responce = token[3];
        }
        private String get_command() { return command; }
        private String get_responce(String weather, String command) {
            if(weather.toLowerCase().equals("hot"))
                return hot_responce;
            else if(weather.toLowerCase().equals("cold"))
                return cold_responce;
            else {
                System.out.println("The two choices are hot or cold!");
                System.exit(-1);
                return "";
            }
        }

    }

    //data members

    private String in_commands = "commands.txt";

    //max and min of commands allowed anything above and below
    //is invalid command
    private final int MAX_COMMAND = 8;

    private final int MIN_COMMAND = 1;

    private final int HOT_READY_LEAVE = 6;

    //array of commands that were read from the file
    private ArrayList <command_data> data;

    //methods

    //opens the input file and reads it in, in order to compare commands
    private void read_commands(){

        //initialize a file object to read with
        File infile = new File(in_commands);

        //initialize the data
        data = new ArrayList<>();

        try {
            //open the file
            Scanner input_file = new Scanner(infile);

            //read in the file
            while(input_file.hasNextLine())
            {
                //get a line
                String line = input_file.nextLine();

                //if white space is not consumed
                //split the line in tokens on , create new class and save it in to array
                if(!line.equals(""))
                {
                    //construct the new class
                    command_data temp = new command_data(line);

                    //add it into the data private variable
                    data.add(temp);
                }

            }
        }
        catch(Exception ex)
        {
            System.out.println("commands.txt Input file does not exist!");
            ex.printStackTrace();
            System.exit(-1);
        }

    }

    //goes through the Array list and looks for the command passed in as argument
    //after the command is found the index number (int) is returned out of the function
    private int get_data_ind(String cmd)
    {
        for (int i = 0; i < data.size(); i++)
        {
            if(data.get(i).get_command().equals(cmd))
                return i;
        }

        return -1;

    }

    //given the temperature initialize values 2 different ways or report an error
    //class constructor
    public temp() {

        //read the commands inputfile
        read_commands();

    }

    //prints the resulting Array by going through a for loop and printing each of the
    //elements which is the clothing items in english followed by a comma
    public void print_results(ArrayList<String> result)
    {

            for(int i = 0; i < result.size(); i++)
            {
                System.out.print(result.get(i));

                if(i != result.size()-1)
                    System.out.print(",");
            }
    }


    //Check function formats the user input string into tokens
    //And performs checks on the bases of the rules provided for the exercise
    private void check(String user_input) throws IllegalAccessException
    {
        //to hold HOT/COLD part of the command
        String temperature;

        //obtain hot/cold from command
        //if input is formatted incorrectly signal and go back to main
        try {
            temperature = user_input.substring(0, user_input.indexOf(" ")).toLowerCase();
        }
        catch(Exception ex)
        {
            throw new IllegalArgumentException("Incorrect input");
        }

        user_input = user_input.substring(user_input.indexOf(" ")+1);

        String [] tokens = user_input.split( "[,]");

        //initialize a new array list
        ArrayList<String> user_command = new ArrayList<>();

        //check if hot or cold command is issued and is in the correct place
        if (!(temperature.equals("hot")) &&
                !(temperature.equals("cold")))
        {
            throw new IllegalArgumentException("User command must begin with weather type with is hot or cold!");
        }

        //goo through each token after the hot/cold command
        for(int i = 0; i < tokens.length; i++)
        {
            //is the command given by the user valid
            if(Integer.parseInt(tokens[i]) < MIN_COMMAND || Integer.parseInt(tokens[i]) > MAX_COMMAND)
            {
                System.out.println("fail");
                throw new IllegalArgumentException("Commands must be between " + MIN_COMMAND + " and " + MAX_COMMAND);
            }

            //if the first command is not taking off pajamas nothing else can be achieved
            //exit and show the error
            if (!tokens[0].equals("8"))
            {
                System.out.println("fail");
                throw new IllegalArgumentException("Pajamas have to be taken off first!");
            }

            //check for duplicate command that has already been registered
            if(user_command.contains(data.get(get_data_ind(tokens[i])).get_responce(temperature,tokens[i])))
            {
                print_results(user_command);
                System.out.println(",fail");
                throw new IllegalArgumentException("Duplicate command entered, only one layer of clothing is allowed");
            }

            //if socks are requested but weather is warm -- report fail
            if(data.get(get_data_ind(tokens[i])).get_responce(temperature,tokens[i]).equals("fail"))
            {
                print_results(user_command);
                System.out.println(",fail");
                throw new IllegalArgumentException("Socks or Jacket are not allowed in warm weather");
            }

            //if adding shoes, socks and pants need to be added already
            if(tokens[i].equals("1"))
            {
                //if pants are not on and it is hot (no socks required)
                if(!user_command.contains(data.get(get_data_ind("6")).get_responce(temperature, "6"))
                        && temperature.toLowerCase().equals("hot"))
                {
                    print_results(user_command);
                    System.out.println(",fail");
                    throw new IllegalArgumentException("Cannot put shoes until pants have been put on!");
                }
                else if ((!user_command.contains(data.get(get_data_ind("6")).get_responce(temperature, "6")) ||
                        !user_command.contains(data.get(get_data_ind("3")).get_responce(temperature, "3")))
                        && temperature.toLowerCase().equals("cold"))
                {
                    print_results(user_command);
                    System.out.println(",fail");
                    throw new IllegalArgumentException("Shoes need to be on after socks and pants!");
                }

            }

            //If attempting to add headwear or jacket, make sure shirt has been added first
            if(tokens[i].equals("2") || tokens[i].equals("5"))
            {
                if(!user_command.contains(data.get(get_data_ind("4")).get_responce(temperature, "4")))
                {
                    print_results(user_command);
                    System.out.println(",fail");
                    throw new IllegalArgumentException("Cannot put jacket or headwear before shirt is on!");
                }

            }

            //check is all items have been acquired before leaving the house
            if(tokens[i].equals("7"))
            {
                if(temperature.equals("hot") && user_command.size() != HOT_READY_LEAVE-1)
                {
                    print_results(user_command);
                    System.out.println(",fail");
                    throw new IllegalArgumentException("Cannot leave the house without all items!");
                }
                else if(temperature.equals("cold") && user_command.size() != MAX_COMMAND-1)
                {
                    print_results(user_command);
                    System.out.println(",fail");
                    throw new IllegalArgumentException("Cannot leave the house without all items!");
                }

            }

            //successful command which is added to the command
            // d array in english (not by number)
            user_command.add(data.get(get_data_ind(tokens[i])).get_responce(temperature,tokens[i]));
        }

        //check
        print_results(user_command);

    }

    //public wrapper for private check function
    public void start_day(String user_input)
    {

        try {
            check(user_input);
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
