package Lexer;
import java.util.Scanner;

public class Lexer {

    // what even is this shit
    enum Tokens {
        FUNC("proc", -1),
        INT64("int64", -2),
        NUMBER("unused", -3);

        // TODO all the others

        public String description;
        public int value;

		private Tokens(String description, int value) {
		    this.description = description;
			this.value = value;
		}

    }

    private String Identifier;
    private double NumVal;

    // holy mother of GabeN
    private String toString(int value) {
        return String.valueOf((char) value);
    }

    public int GetTok() {
        int LastChar = ' ';
        Scanner scanner = new Scanner(System.in);

        while (Character.isWhitespace(LastChar)) {
            LastChar = scanner.next().charAt(0);
        }

        // characters
        if (Character.isAlphabetic(LastChar)) {
           Identifier = toString(LastChar);

           while(Character.isDigit(LastChar = scanner.next().charAt(0))) {
               Identifier += toString(LastChar);
           }

           if (Identifier == Tokens.FUNC.description) {
               scanner.close(); // holy hell
               return Tokens.FUNC.value;
           }

           // TODO all the others
        }

        // numbers
        if (Character.isDigit(LastChar) || LastChar == '.') {
            String NumStr = toString(LastChar);
            LastChar = scanner.next().charAt(0);

            while (Character.isDigit(LastChar) || LastChar == '.') {
                NumStr += toString(LastChar);
                LastChar = scanner.next().charAt(0);
            }

            NumVal = Integer.parseInt(NumStr);
            scanner.close(); // holy hell
            return Tokens.NUMBER.value;
        }

        // comments
        if (LastChar == '#') { // i just picked one, you can change this
            LastChar = scanner.next().charAt(0);

            while(LastChar != '\n' && LastChar != '\r') { // EOF ????????
                LastChar = scanner.next().charAt(0);
            }

            scanner.close();
            return GetTok();
        }

        int Char = LastChar;
        LastChar = scanner.next().charAt(0);

        scanner.close();
        return Char;
    }


}
