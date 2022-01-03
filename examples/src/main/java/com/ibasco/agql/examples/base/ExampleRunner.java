/*
 * Copyright 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    private final Map<String, BaseExample> examples = new HashMap<>();

    private final Options options = new Options();

    private final Option nameOption = Option.builder("e").hasArg(true).numberOfArgs(1).desc("The name of the example to run").required().build();

    private final HelpFormatter formatter = new HelpFormatter();

    private BaseExample activeExample;

    public ExampleRunner() {
        this.examples.put("source-query", new SourceQueryExample());
        this.examples.put("master-query", new MasterQueryExample());
        this.examples.put("source-rcon", new SourceRconExample());
        this.examples.put("coc-webapi", new CocWebApiExample());
        this.examples.put("csgo-webapi", new CsgoWebApiExample());
        this.examples.put("steam-webapi", new SteamWebApiExample());
        this.examples.put("steam-store-webapi", new SteamStoreWebApiExample());
        this.examples.put("source-log", new SourceLogListenerExample());
        this.examples.put("steam-econ-webapi", new SteamEconItemsExample());
        this.examples.put("mc-rcon", new MinecraftRconExample());
        this.examples.put("dota2-webapi", new Dota2WebApiExample());

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
                runExample(args, line.getOptionValue(nameOption.getOpt()));
            } else {
                log.error("No example specified");
            }
        } catch (ParseException exp) {
            formatter.printHelp("mvn exec:java -Dexec.args=\"-e <example-key>\"", options, true);
        }
    }

    private void runExample(String[] args, String exampleKey) throws Exception {
        if (this.examples.containsKey(exampleKey)) {
            BaseExample example = this.examples.get(exampleKey);
            this.activeExample = example;

            log.info("Running Example : {}", exampleKey);
            example.run(args);
            log.info("Closing Example");
        }
    }

    public static void main(String[] args) throws Exception {
        ExampleRunner runner = new ExampleRunner();
        runner.processArguments(args);
    }
}
