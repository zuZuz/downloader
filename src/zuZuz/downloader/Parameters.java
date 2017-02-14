package zuZuz.downloader;

import org.apache.commons.cli.*;

class Parameters {
    private String[] arguments;
    private Options options;
    private CommandLine opts;

    private static final String programName = "downloader";

    Parameters(String[] arguments) {
        this.arguments = arguments;

        Option threads = new Option("n", true, "count of download threads");
        Option limit = new Option("l", true, "speed limit");
        Option input = new Option("f", true, "input file");
        Option output = new Option("o", true, "output dir");

        threads.setArgs(1);
        limit.setArgs(1);
        input.setArgs(1);
        output.setArgs(1);

        options = new Options();
        options.addOption(threads);
        options.addOption(limit);
        options.addOption(input);
        options.addOption(output);
    }

    boolean parse() {
        CommandLineParser parser = new PosixParser();

        try {
            opts = parser.parse(options, arguments);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(programName, options);
    }

    int getN() {
        if (opts.hasOption("n")) {
            return Integer.parseInt(opts.getOptionValue("n"));
        }

        return -1;
    }

    boolean isCorrect() {
        return  getN() > 0 &&
                getL() > 0 &&
                !getF().isEmpty() &&
                !getO().isEmpty();
    }

    int getL() {
        int mul = 1;

        if (opts.hasOption("l")) {
            if (opts.getOptionValue("l").indexOf('k') != -1) {
                mul = 1024;
            }
            if (opts.getOptionValue("l").indexOf('m') != -1)
                mul = 1024 * 1024;

            return mul * Integer.parseInt(opts.getOptionValue("l").replaceAll("[^0-9]", ""));
        }

        return -1;
    }

    String getF() {
        if (opts.hasOption("f")) {
            return opts.getOptionValue("f");
        }

        return "";
    }

    String getO() {
        if (opts.hasOption("o")) {
            return opts.getOptionValue("o");
        }

        return "";
    }
}