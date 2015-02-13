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
                // cut out surrounding quotation marks
                String input = args[1];
                
                String[] output = crypto.encrypt(input);
                
                System.out.println("CIPHERTEXT: \"" + output[0] + "\"");
                System.out.println("KEY: \"" + output[1] + "\"");
                
                System.exit(0);
                
            } else if (cryptToggle.equals("decrypt")) {
                
                // attempt to decrypt the ciphertext using key
                String ciphertext = args[1];
                String key = args[2];
                String output = crypto.decrypt(ciphertext, key);
                System.out.println("DECRYPTED: \"" + output + "\"");
                System.exit(0);
                
            } else {
                System.out.println("Invalid arguments.");
                System.out.println(crypto.standardError());
            }
        }
    }
    
    private String[] encrypt(String input) {
        
        byte[] S = new byte[256];   // state vector
        byte[] T = new byte[256];   // temporary vector
        int keyLength = 16;         // always use a generated 16 character key
        
        byte[] K = new byte[16];    // generate the 16 character key
        for (int a = 0; a < 16; a++) {
            K[a] = (byte)(Math.random() * 16);
        }
        
        int padLength = K[ K[1] ] + K[ K[3] ];    // create a random amount of padding
        
        for (int i = 0; i < 256; i++) {
            S[i] = (byte) i;
            T[i] = K[i % keyLength];
        }
        
        int j = 0;
        
        for (int i = 0; i < 256; i++) {
            j = (j + S[i] + T[i]) & 0xFF;
            byte temp = S[j];   // swap values of i and j
            S[j] = S[i];
            S[i] = temp;
        }
        
        // begin encryption process using fully initalized state vector
        
        byte[] ciphertext = new byte[input.length() + padLength * 2];
        
        for (int b = 0; b < padLength; b++) {   // fill left pad
            ciphertext[b] = (byte) (Math.random() * 255);
        }
        
        for (int b = padLength + input.length(); b < ciphertext.length; b++) {  // fill right pad
            ciphertext[b] = (byte) (Math.random() * 255);
        }
        
        int i = 0, k = 0, t = 0;
        j = 0;
        
        for (int pos = 0; pos < input.length(); pos++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            byte temp = S[j];   // swap values of i and j
            S[j] = S[i];
            S[i] = temp;
            
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            ciphertext[pos + padLength] = (byte) (input.charAt(pos) ^ k);
        }
        
        String[] result = new String[2];
        result[0] = byteArrayToHexString(ciphertext);
        result[1] = byteArrayToHexString(K);
        
        return result;
    }
    
    private String decrypt(String ciphertext, String key) {
        byte[] K = hexStringToByteArray(key);
        
        int padLength = K[ K[1] ] + K[ K[3] ];
        
        byte[] paddedInput = hexStringToByteArray(ciphertext);
        
        byte[] byteInput = new byte[paddedInput.length - padLength * 2];
        for (int u = 0; u < byteInput.length; u++) {
            byteInput[u] = paddedInput[padLength + u];  // take away the generated pad
        }
        
        
        byte[] S = new byte[256];   // state vector
        byte[] T = new byte[256];   // temporary vector
        int keyLength = 16;         // always use a generated 16 character key
        
        for (int i = 0; i < 256; i++) {
            S[i] = (byte) i;
            T[i] = K[i % keyLength];
        }
        
        int j = 0;
        
        for (int i = 0; i < 256; i++) {
            j = (j + S[i] + T[i]) & 0xFF;
            byte temp = S[j];   // swap values of i and j
            S[j] = S[i];
            S[i] = temp;
        }
        
        
        // begin decryption process using fully initalized state vector
        
        byte[] cleartext = new byte[byteInput.length];
        
        int i = 0, k = 0, t = 0;
        j = 0;
        
        for (int pos = 0; pos < cleartext.length; pos++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            byte temp = S[j];   // swap values of i and j
            S[j] = S[i];
            S[i] = temp;
            
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            cleartext[pos] = (byte) (byteInput[pos] ^ k);
        }
        
        
        return hexToASCII(byteArrayToHexString(cleartext));
    }
    
    private String standardError() {
        return "Usage: [encrypt/decrypt]" + " [\"input\"] [key if decrypt]";
    }
    
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    
    private static String byteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    private static String hexToASCII(String hexValue)
    {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2)
        {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }
}
