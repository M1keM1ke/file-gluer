package ru.mikemimike.filegluer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ru.mikemimike.filegluer.service.CliService;
import ru.mikemimike.filegluer.service.GluerService;

import java.io.IOException;


public class FileGluerApplication {
    public static void main(String[] args) {
        CliService cliService = new CliService();
        CommandLine cmd = null;
        try {
            cmd = cliService.parseOptions(args);

            if (cmd.hasOption("h")) {
                cliService.printHelp("filegluer", "", cliService.getOptions(), "", false);
                return;
            }
        } catch (ParseException e) {
            try {
                cliService.printHelp("filegluer", "", cliService.getOptions(), "", false);
                return;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String inputPath = cmd.getOptionValue("input");
        String outputFilePath = cmd.getOptionValue("output");
        String ignoreOption = cmd.getOptionValue("ignore");
        String separator = cmd.getOptionValue("separator");

        GluerService gluerService = new GluerService();
        gluerService.concatenateFiles(inputPath, outputFilePath, ignoreOption, separator);
    }
}
