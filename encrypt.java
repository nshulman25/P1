import java.io.*;
import java.util.Scanner;

public class encrypt{
    static String key = null;
    static String initial_input = null;
    
    public static void main (String[] args) throws IOException{
        initial_input = readFile("input.txt");
        key = readFile("key.txt");

        String cleaned_input = preProcessing(initial_input);
        writeFile("PreProcesing: \n" + cleaned_input + "\n", false);
        
        String encryption = vignereCipher(cleaned_input, key);
        writeFile("Subsitution: \n" + encryption + "\n", true);

        writeFile(padded_input[][], true);
        String padded_Input[][] = padding(encryption);
    }

    /** readFile method
     * 
     * Reads desired file based on param and converts its content to a string and returns it.
     * @param fileName
     * @return
     */
    private static String readFile(String fileName){
        
        String data = null;
       
        // Takes the name of the file finds its pathname and scans through it and returns the data
        try {
            File input = new File(fileName);
            Scanner read = new Scanner(input);
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

    private static File writeFile(String input, Boolean append) throws IOException{
        File output = new File("ouput.txt");
        FileWriter fw_output = new FileWriter(output, append);
        PrintWriter pw_output = new PrintWriter(fw_output);

        pw_output.println(input);
        pw_output.close();

        return null;
    }

    /** preProccessng method
     * 
     * Removes all white space and punctuations from desired string and returns it.
     * @param input
     * @return
     */ 
    
    private static String preProcessing(String input){
        
        // replaces all whitespace
        input = input.replaceAll("\\s", "");
        
        // removes all punctuations
        input = input.replaceAll("\\p{Punct}", "");
        
        return input;
    }

    /** vignereCipher method
     * 
     * Encrypts the cleaned plain text using the provided key through the vignere cipher and returns it.
     * @param input
     * @param key
     * @return
     */
    private static String vignereCipher(String input, String key){
        
        String encipher = "";
        
        
        // loops through both the plain text input and the key 
        for(int i = 0; i < input.length(); i++){
            
            // if the input length exceded key length appened the same string to the key
            if(key.length() == i){
                key += key;
            }
            
            // adds the characters in both the key and input together and modulos them by 26
            // this  equals its place in the alphabet
            int ASCII_Value = (input.charAt(i) + key.charAt(i)) %26;
            
            // adds the place in the alphabet to the ASCII value of A to get its true ASCII value
            ASCII_Value += 'A';

            // converts all the ASCII values to chars and appends to the encrypted text
            encipher += (char) ASCII_Value;
        }


        return encipher;
    }

    /** padding method
     * takes the encrypted message and pads A, till the length is divisible by 16.
     * @param encryption
     * @return
     */
    private static String[][] padding(String encryption){
        
        int padding = 16 - (encryption.length() %16);

        for(int i = 0; i < padding; i++){
            encryption += 'A';
        }
        
        String[][] padded_input = new String [encryption.length()/4][4];
        int pointer = 0;

        for(int row = 0; row < padded_input.length; row++){
            for(int column = 0; column < padded_input[row].length; column++){
                
                char temp = encryption.charAt(pointer);

                padded_input[row][column] = Character.toString(temp);
                pointer++;
                System.out.print(padded_input[row][column]);
            }
            System.out.println();
            if(pointer%16 == 0){
                System.out.println();
            }
        }

        

        return padded_input;
    } 

}




