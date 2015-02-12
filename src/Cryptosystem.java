
public class Cryptosystem {
    
    public static void main(String[] args) {
        
        Cryptosystem crypto = new Cryptosystem();
        
        if (args.length <= 1) {
            System.out.println("Too few arguments.");
            System.out.println(crypto.standardError());
            
        } else if (args.length >= 4) {
            System.out.println("Too many arguments.");
            System.out.println(crypto.standardError());
        } else {
            // determine if the user wants to encrypt or decrypt
            String cryptToggle = args[0];
            
            if (cryptToggle.equals("encrypt")) {
                
                // attempt to encrypt the user input
                String rawInput = args[1];
                if (rawInput.charAt(0) != '\"' || 
                        rawInput.charAt(rawInput.length()) != '\"') {
                    System.out.println("Input string formatted incorrectly.");
                    System.out.println(crypto.standardError());
                }
                // cut out surrounding quotation marks
                String input = rawInput.substring(1, rawInput.length() - 1);
                
                String[] output = crypto.encrypt(input);
                
                System.out.println("CIPHERTEXT: \"" + output[0] + "\"");
                System.out.println("KEY: \"" + output[1] + "\"");
                
            } else if (cryptToggle.equals("decrypt")) {
                
                // attempt to decrypt the ciphertext using key
                String rawInput = args[1];
                if (rawInput.charAt(0) != '\"' || 
                        rawInput.charAt(rawInput.length()) != '\"') {
                    System.out.println("Input string formatted incorrectly.");
                    System.out.println(crypto.standardError());
                }
                // cut out surrounding quotation marks
                String ciphertext = rawInput.substring(1, rawInput.length() - 1);
                
                String key = args[2];
                
                String output = crypto.decrypt(ciphertext, key);
                
                System.out.println("DECRYPTED: \"" + output + "\"");
                
            } else {
                System.out.println("Invalid arguments.");
                System.out.println(crypto.standardError());
            }
        }
    }
    
    private String[] encrypt(String input) {
        return null;
    }
    
    private String decrypt(String ciphertext, String key) {
        return "";
    }
    
    private String standardError() {
        return "Usage: [encrypt/decrypt]" + " [\"input\"] [key if decrypt]";
    }
}
