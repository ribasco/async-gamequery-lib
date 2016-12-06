/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.examples.base;

import com.ibasco.agql.examples.*;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExampleRunner {

    private static final Logger log = LoggerFactory.getLogger(ExampleRunner.class);

    private Map<String, BaseExample> examples = new HashMap<>();

    private Options options = new Options();

    private Option nameOption = Option.builder("e").hasArg(true).numberOfArgs(1).desc("The name of the example to run").required().build();

    private HelpFormatter formatter = new HelpFormatter();

    private BaseExample activeExample;

    public ExampleRunner() {
        this.examples.put("source-query", new SourceServerQueryEx());
        this.examples.put("master-query", new MasterServerQueryEx());
        this.examples.put("source-rcon", new SourceRconQueryEx());
        this.examples.put("coc-webapi", new CocWebApiQueryEx());
        this.examples.put("csgo-webapi", new CsgoWebApiQueryEx());
        this.examples.put("steam-webapi", new SteamWebApiQueryEx());
        this.examples.put("steam-store-webapi", new SteamStoreWebApiQueryEx());
        this.examples.put("source-log", new SourceLogMonitorEx());
        this.examples.put("steam-econ-webapi", new SteamEconItemsQueryEx());
        this.examples.put("mc-rcon", new McRconQueryEx());

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            try {
                log.info("Shutting down example");
                if (this.activeExample != null) {
                    this.activeExample.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ));

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
            formatter.printHelp("mvn exec:java -Dexec.args=\"-e <example-key>\"", options, true);
        }
    }

    private void runExample(String exampleKey) throws Exception {
        if (this.examples.containsKey(exampleKey)) {
            BaseExample example = this.examples.get(exampleKey);
            this.activeExample = example;
            try {
                log.info("Running Example : {}", exampleKey);
                example.run();
            } finally {
                log.info("Closing Example");
                example.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ExampleRunner runner = new ExampleRunner();
        runner.processArguments(args);
    }
}
