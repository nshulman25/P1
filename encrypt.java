import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class encrypt{
    static String key = null;
    static String initial_input = null;
    
    public static void main (String[] args) throws IOException{
        initial_input = readFile("input.txt");
        key = readFile("key.txt");

        String cleaned_input = preProcessing(initial_input);
        writeFile("PreProcesing: \n" + cleaned_input + "\n \n", false);
        
        String encryption = vignereCipher(cleaned_input, key);
        writeFile("Subsitution: \n" + encryption + "\n \n", true);

        String padded_Input[][] = padding(encryption);
        String shift_Row[][] = shiftRows(padded_Input);
    }

    // readFile method
    // Reads desired file based on param and converts its content to a string and returns it.
    private static String readFile(String fileName){
        
        String data = null;
       
        // Takes the pathname of the file finds it and scans through it and returns the data
        try {
            File pathname = new File(fileName);
            Scanner read = new Scanner(pathname);
            while (read.hasNextLine()){
                data = read.nextLine();
            }
            read.close();
        } catch (Exception e) {
            System.out.println("no input file found");
            e.printStackTrace();
        }
        
        return data;
    }

    // writeFile method
    // writes the desired string to a file named "output.txt"
    private static File writeFile(String input, Boolean append) throws IOException{
        
        // Creates a file named output.txt, a file writer, and a print writer
        File output = new File("ouput.txt");
        FileWriter fw_output = new FileWriter(output, append);
        PrintWriter pw_output = new PrintWriter(fw_output);

        // prints the desiered input then closes
        pw_output.print(input);
        pw_output.close();

        return null;
    }

    // preProccessng method
    // Removes all white space and punctuations from desired string and returns it.
    private static String preProcessing(String input){
        
        // replaces all whitespace
        input = input.replaceAll("\\s", "");
        
        // removes all punctuations
        input = input.replaceAll("\\p{Punct}", "");
        
        return input;
    }

    // vignereCipher method
    // Encrypts the cleaned plain text using the provided key through the vignere cipher and returns it.
    private static String vignereCipher(String input, String key){
        
        String encipher = "";
        
        
        // loops through both the plain text input and the key 
        for(int i = 0; i < input.length(); i++){
            
            // if the input length exceded key length appened the same string to the key
            if(key.length() == i){
                key += key;
            }
            
            // adds the chars in both the key and input together and modulos them by 26
            // (i.e. a = 0, b = 1, etc)
            int ASCII_Value = (input.charAt(i) + key.charAt(i)) %26;
            
            // adds the place in the alphabet to the ASCII value of A to get its true ASCII value
            ASCII_Value += 'A';

            // converts all the ASCII values to chars and appends to the encrypted text
            encipher += (char) ASCII_Value;
        }


        return encipher;
    }

    // padding method
    // takes the encrypted message and pads A till the length is divisible by 16.
    // and add its to a 2d array.
    private static String[][] padding(String input) throws IOException{
        
        // padds the string with 'A' to have a length divisible by 16
        int padding = 16 - (input.length() %16);
        for(int i = 0; i < padding; i++){
            input += 'A';
        }
        
        // creates a 2D array wiith 4 columns and (string length/4) rows
        String[][] padded_input = new String [input.length()/4][4];
        int pointer = 0;

        writeFile("Padding:" + "\n", true);

        // loops through the 2d array (rows, right to left; then down one column)
        for(int row = 0; row < padded_input.length; row++){
            for(int column = 0; column < padded_input[row].length; column++){
                
                // adds character in the string at the pointer location to the array
                char temp = input.charAt(pointer);

                padded_input[row][column] = Character.toString(temp);
                pointer++;
                writeFile(padded_input[row][column], true);

            }
            writeFile("\n", true);
            
            // if pointer is divisible by 16 creates a new line to make 4x4 block seperate
            if(pointer%16 == 0){
                writeFile("\n", true);
            }
        }
        return padded_input;
    } 
    
    private static String[][] shiftRows(String[][] input){
        
        int counter = 0;
       
        String[][] temp = new String[input.length][];
        for(int row = 0; row < input.length; row++){
            temp[row] = new String[input[row].length];

            for(int column = 0; column < input[row].length; column++){
                temp[row][column] = input[row][column];
            }
        }



        // loops through the 2d array (rows, right to left; then down one column)
        for(int row = 0; row < input.length; row++){
            for(int column = 0; column < input[row].length; column++){
                // 2nd row shift
                if(counter == 1 ){
                    
                    
                    if(column == 3){
                        input[row][column] = temp[row][0];
                        System.out.print(input[row][column]);
                        break;
                    }

                    input[row][column] = input[row][column+1];
                    System.out.print(input[row][column]);
                }
                
                // 3rd row shift
                else if(counter == 2){
                    
                    if(column == 2){
                        input[row][column] = temp[row][0];
                        System.out.print(input[row][column]);
                        continue;
                    }
                    if(column == 3){
                        input[row][column] = temp[row][1];
                        System.out.print(input[row][column]);
                        continue;
                    }
                    
                    input[row][column] = input[row][column+2];
                    System.out.print(input[row][column]);
                }

                // 4th tow shift
                else if(counter ==3){
                   
                    if(column == 1){
                        input[row][column] = temp[row][0];
                        System.out.print(input[row][column]);
                        continue;
                    }
                    if(column == 2){
                        input[row][column] = temp[row][1];
                        System.out.print(input[row][column]);
                        continue;
                    }
                    if(column == 3){
                        input[row][column] = temp[row][2];
                        System.out.println(input[row][column]);
                        continue;
                    }
                    
                    input[row][column] = input[row][column+3];
                    System.out.print(input[row][column]);
                
                // 1st row shift    
                }else{
                    System.out.print(input[row][column]);
                }

            }
            System.out.println();
            counter++;

            if (counter > 3){
                counter = 0;
            }
        }
 
        return null;
    }
}
