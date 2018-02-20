package solutions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9Java {

    private static Pattern bangMatch = Pattern.compile("^!.(.*)$");
    private static Pattern garbageStartMatch = Pattern.compile("^<(.*)$");
    private static Pattern garbageEndMatch = Pattern.compile("^>(.*)$");
    private static Pattern openBracketMatch = Pattern.compile("^\\{(.*)$");
    private static Pattern closeBracketMatch = Pattern.compile("^}(.*)$");
    private static Pattern anyMatch = Pattern.compile("^.(.*)");

    public static class CleaningResult {
        String cleaned;
        int totalGarbage;

        static CleaningResult of(String cleaned, int totalGarbage) {
            CleaningResult r = new CleaningResult();
            r.cleaned = cleaned;
            r.totalGarbage = totalGarbage;
            return r;
        }
    }

    private static CleaningResult deleteGarbageLoop(String input) {

        int totalGarbage = 0;

        while (!input.isEmpty()) {
            Matcher m = bangMatch.matcher(input);
            if (m.find()) {
                input = m.group(1);
                continue;
            }
            m = garbageEndMatch.matcher(input);
            if (m.find()) {
                return CleaningResult.of(m.group(1), totalGarbage);
            }
            m = anyMatch.matcher(input);
            if (m.find()) {
                input = m.group(1);
                totalGarbage++;
            }
        }
        return CleaningResult.of(input, totalGarbage);
    }

    static CleaningResult removeGarbage(String input) {
//        System.out.println("----" + input.length());

        StringBuilder resultBuilder = new StringBuilder();
        int totalGarbage = 0;

        while(!input.isEmpty()) {

            Matcher m = openBracketMatch.matcher(input);
            if (m.find()) {
                input = m.group(1);
                resultBuilder.append('{');
                continue;
            }
            m = closeBracketMatch.matcher(input);
            if (m.find()) {
                input = m.group(1);
                resultBuilder.append('}');
                continue;
            }
            m = bangMatch.matcher(input);
            if (m.find()) {
                input = m.group(1);
            }
            m = garbageStartMatch.matcher(input);
            if (m.find()) {
                CleaningResult cleanResult = deleteGarbageLoop(m.group(1));
                input = cleanResult.cleaned;
                totalGarbage += cleanResult.totalGarbage;
                continue;
            }
            input = input.substring(1);
        }
        return CleaningResult.of(resultBuilder.toString(), totalGarbage);
    }

    static int getValue(String input) {

        CleaningResult cleaningResult = removeGarbage(input);
        System.out.println("garbage total: " + cleaningResult.totalGarbage);

        String rest = cleaningResult.cleaned;
        int currentValue = 0;
        int total = 0;
        while(!rest.isEmpty()) {
            char next = rest.charAt(0);
            if (next == '{') {
                currentValue++;
            }
            if (next == '}') {
                total += currentValue;
                currentValue--;
            }
            rest = rest.substring(1);
        }
        return total;
    }
}
