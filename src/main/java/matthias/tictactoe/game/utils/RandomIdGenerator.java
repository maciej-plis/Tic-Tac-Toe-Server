package matthias.tictactoe.game.utils;

import java.util.Random;

public class RandomIdGenerator {

    private static char[] BASE_62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private static Random random = new Random();

    public static String getBase62Id(int length) {
        StringBuilder sb = new StringBuilder(length);

        for(int i=0; i<length; i++) {
            sb.append(BASE_62_CHARS[random.nextInt(BASE_62_CHARS.length)]);
        }

        return sb.toString();
    }

}
