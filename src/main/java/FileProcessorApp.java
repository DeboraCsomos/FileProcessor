import utils.FilePartReader;

import static java.lang.Integer.parseInt;

public class FileProcessorApp {
    private static FilePartReader filePartReader;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Mandatory parameter [filePath] is missing. \n" +
                    "Please provide a valid path to an existing text file!");
            System.exit(1);
        }

        else {
            String filePath = args[0];
            if (args.length == 1) {
                filePartReader = new FilePartReader(filePath);
            }

            else {
                int toLine = parseSecondParameter(args);
                if (args.length == 2) {
                    filePartReader = new FilePartReader(filePath, toLine);
                } else if (args.length == 3) {
                    String language;
                    if (!args[2].equals("hu") && !args[2].equals("en")) {
                        language = "hu";
                    } else {
                        language= args[2];
                    }
                    filePartReader = new FilePartReader(filePath, toLine, language);
                } else {
                    System.out.println("Too much parameter!");
                    System.exit(1);
                }
            }
        }
        System.out.println(filePartReader);
    }

    private static int parseSecondParameter(String[] args) {
        int toLine = 0;
        try {
            toLine = parseInt(args[1]);
        } catch (NumberFormatException exp) {
            System.out.println("Second parameter must be a number!");
            System.exit(1);
        }
        return toLine;
    }
}
