import java.io.*;
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

        String padded_Input[][][] = padding(encryption);
        String shift_Row[][][] = shiftRows(padded_Input);
        String parity_Bit[][][] = parityBit(shift_Row);
        shiftColumn(parity_Bit);
        RGF_mul("d7", 2);
    }

    /**
     * Reads desired file based on param and converts its content to a string and returns it.
     * @param fileName
     * @return
     */
    private static String readFile(String fileName){
        
        String data = "";
       
        // Takes the pathname of the file finds it and scans through it and returns the data
        try {
            File pathname = new File(fileName);
            Scanner read = new Scanner(pathname);
            while (read.hasNextLine()){
                data += read.nextLine();
            }
        } catch (Exception e) {
            System.out.println("no input file found");
            e.printStackTrace();
        }
        
        return data;
    }

    /**
     *  writes the desired string to a file named "output.txt"
     * @param input
     * @param append
     * @return
     * @throws IOException
     */
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

    /**
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

    /**
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

    /**
     * Takes the encrypted message and pads A till the length is divisible by 16 
     * and add its to a 2d array.
     * @param input
     * @return
     * @throws IOException
     */
    private static String[][][] padding(String input) throws IOException{
        
        // padds the string with 'A' to have a length divisible by 16
        int padding = 16 - (input.length() %16);
        for(int i = 0; i < padding; i++){
            input += 'A';
        }
        
        // creates a 3D array wiith 4 columns 4 rows and dynamicly creates boxs
        String[][][] padded_input = new String [input.length()/16][4][4];
        int pointer = 0;

        writeFile("Padding:" + "\n", true);

        // loops through the 3d array (rows, right to left; then down one column)
        for(int box = 0; box < padded_input.length; box++){
            for(int row = 0; row < padded_input[box].length; row++){
                for(int column = 0; column < padded_input[box][row].length; column++){
                    // adds character in the string at the pointer location to the array
                    char temp = input.charAt(pointer);

                    padded_input[box][row][column] = Character.toString(temp);
                    pointer++;
                    writeFile(padded_input[box][row][column], true);
                }
                writeFile("\n", true);
            }
            writeFile("\n", true);            
        }
        return padded_input;
    } 
    
    /**
     * Shift all the rows in the array depending on their row number 
     * @param input
     * @return an array of string
     * @throws IOException
     */
    private static String[][][] shiftRows(String[][][] input) throws IOException{
        
        writeFile("ShiftRows:" + "\n", true);

        // Creates a temporary copy of the 3d array
        String[][][] temp = new String[input.length][4][4];

        //loops through each box and does proper shift for each row
        for(int box = 0; box < input.length; box++){
            
            // Row 0 ; no shift
            temp[box][0][0] = input[box][0][0];
            temp[box][0][1] = input[box][0][1];
            temp[box][0][2] = input[box][0][2];
            temp[box][0][3] = input[box][0][3];

            // Row 1; 1 left
            temp[box][1][0] = input[box][1][1];
            temp[box][1][1] = input[box][1][2];
            temp[box][1][2] = input[box][1][3];
            temp[box][1][3] = input[box][1][0];

            // Row 2; 2 left
            temp[box][2][0] = input[box][2][2];
            temp[box][2][1] = input[box][2][3];
            temp[box][2][2] = input[box][2][0];
            temp[box][2][3] = input[box][2][1];

            // Row 3; 3 left
            temp[box][3][0] = input[box][3][3];
            temp[box][3][1] = input[box][3][0];
            temp[box][3][2] = input[box][3][1];
            temp[box][3][3] = input[box][3][2];
        }

        // loops through the 3d array (rows, right to left; then down one column)
        for(int box = 0; box < input.length; box++){
            for(int row = 0; row < input[box].length; row++){
                for(int column = 0; column < input[box][row].length; column++){

                    writeFile(temp[box][row][column], true);
                }
                writeFile("\n", true);
            }
            writeFile("\n", true);
        }
        return temp;
    }
    

    /**
     * Determines if a binary number will have even or odd number of ones
     * @param n
     * @return (true == odd # of 1s) (false == even # of ones)
     */
    private static boolean parityBitFunction(int n){
       
        boolean paritybit = false;
        while(n != 0){
            n = n & (n-1);
            paritybit = !paritybit;
        }

        return paritybit;
    }

    /**
     * loops through a 3d array and if the strings value converted to a binary nummber 
     * has an odd number of 1s adds a parity bit
     * @param input
     * @return 3d array of hexidecimal numbers as strings
     * @throws IOException
     */
    private static String[][][] parityBit(String[][][] input) throws IOException{
        writeFile("Parity: " + "\n", true);        

           // loops through the 2d array (rows, right to left; then down one column)
        for(int box = 0; box < input.length; box++){
            for(int row = 0; row < input[box].length; row++){
                for(int column = 0; column < input[box][row].length; column++){
                    String hex = null;
                
                    // Converts the current value to its decimal representation
                    char c = input[box][row][column].charAt(0);
                    int n = (int) c;
                    
                    // if odd number of 1s add parity bit and convert to hex and add back to array
                    if(parityBitFunction(n) == true){
                        
                        // converst char to binary string
                        hex = String.format("%x", n);
                        int num = Integer.parseInt(hex, 16);
                        String binary = Integer.toString(num, 2);
                        
                        // append 1 to the front of the string
                        binary = 1 + binary;

                        // converts binary string to hex
                        int decimal = Integer.parseInt(binary, 2);
                        hex = String.format("%x", decimal);
                        
                        input[box][row][column] = hex;

                    // if even convert back to hex and input in array
                    }else{
                        hex = String.format("%x", (int) n);
                        input[box][row][column] = hex;
                    } 
                    writeFile(input[box][row][column] + " ", true);
                }  
                writeFile("\n", true); 
            }    
            writeFile("\n", true);
        }
        return input;
    }

    private static String RGF_mul(String x, int y){
       
        int hex = Integer.parseInt(x, 16);
        String binary = Integer.toString(hex, 2);
        

       

        return null;
    }

    private static String[][][] shiftColumn(String[][][] input) throws IOException{
        writeFile("Shift Columns: \n", true);

        for(int box = 0; box < input.length; box++){
            for(int row = 0; row < input[box].length; row++){
                for(int column = 0; column < input[box][row].length; column++){
                
                }
            }
        }


        return null;
    }
}