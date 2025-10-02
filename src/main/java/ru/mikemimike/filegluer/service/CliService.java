package ru.mikemimike.filegluer.service;

import org.apache.commons.cli.*;
import org.apache.commons.cli.help.HelpFormatter;

import java.io.IOException;

import static ru.mikemimike.filegluer.util.SeparatorUtil.getDefaultSeparator;

public class CliService {
    private CommandLineParser commandLineParser;
    private Options options;
    private HelpFormatter helpFormatter;

    public CliService() {
        options = new Options();

        Option input = new Option("i", "input", true, "input directory for gluing files");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file path");
        output.setRequired(true);
        options.addOption(output);

        Option ignore = new Option("ig", "ignore", true, "ignore file like .gitignore");
        ignore.setRequired(false);
        options.addOption(ignore);

        Option help = new Option("h", "help", false, "show help");
        help.setRequired(false);
        options.addOption(help);

        Option separator = new Option("s", "separator", true,
                "file separator. Default separator is: " + getDefaultSeparator());
        separator.setRequired(false);
        options.addOption(separator);

        commandLineParser = new DefaultParser();
        helpFormatter = HelpFormatter.builder().get();
    }

    public Options getOptions() {
        return options;
    }

    public void printHelp(String cmdLineSyntax, String header, Options options, String footer, boolean autoUsage) throws IOException {
        helpFormatter.printHelp(cmdLineSyntax, header, options, footer, autoUsage);

    }

    public CommandLine parseOptions(String[] args) throws ParseException {
        return commandLineParser.parse(options, args);
    }
}
