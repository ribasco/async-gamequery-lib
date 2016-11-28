package com.ibasco.agql.examples.base;

import com.ibasco.agql.examples.*;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ExampleRunner {

    private static final Logger log = LoggerFactory.getLogger(ExampleRunner.class);

    private Map<String, BaseExample> examples = new HashMap<>();

    private Options options = new Options();

    private Option nameOption = Option.builder("e").hasArg(true).numberOfArgs(1).desc("The name of the example to run").required().build();

    private HelpFormatter formatter = new HelpFormatter();

    public ExampleRunner() {
        this.examples.put("source-query", new SourceServerQueryEx());
        this.examples.put("master-query", new MasterServerQueryEx());
        this.examples.put("source-rcon", new SourceRconQueryEx());
        this.examples.put("coc-webapi", new CocWebApiQueryEx());
        this.examples.put("csgo-webapi", new CsgoWebApiQueryEx());
        this.examples.put("steam-webapi", new SteamWebApiQueryEx());
        this.examples.put("steam-store-webapi", new SteamStoreWebApiQueryEx());
        this.examples.put("source-logger", new SourceLogMonitorEx());
        this.examples.put("steam-econ-webapi", new SteamEconItemsQueryEx());

        //Add options
        options.addOption(nameOption);
    }

    private void processArguments(String[] args) throws Exception {
        // create the parser
        CommandLineParser parser = new DefaultParser();

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);
            if (line.hasOption(nameOption.getOpt())) {
                runExample(line.getOptionValue(nameOption.getOpt()));
            } else {
                log.error("No example specified");
            }
        } catch (ParseException exp) {
            formatter.printHelp("programName", options, true);
        }
    }

    private void runExample(String exampleKey) throws Exception {
        if (this.examples.containsKey(exampleKey)) {
            BaseExample example = this.examples.get(exampleKey);
            try {
                log.info("Running Example : {}", exampleKey);
                example.run();
            } finally {
                example.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ExampleRunner runner = new ExampleRunner();
        runner.processArguments(args);
    }
}
