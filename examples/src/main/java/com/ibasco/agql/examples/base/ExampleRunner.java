/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

    private volatile boolean closed;

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
                                                                BaseExample.printLine();
                                                                System.out.printf("(\033[0;31mSHUTDOWN\033[0m) \033[0;33mShutdown requested. Attempting to shutdown example \033[0;36m'%s'\033[0m\n", activeExample);
                                                                BaseExample.printLine();
                                                                if (this.activeExample != null && !closed) {
                                                                    this.activeExample.close();
                                                                }
                                                            } catch (IOException e) {
                                                                e.printStackTrace(System.err);
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
            try (BaseExample example = this.examples.get(exampleKey)) {
                this.activeExample = example;
                clearConsole();
                BaseExample.printHeader("Running example program '%s' (key: %s)", example.getClass().getSimpleName(), exampleKey);
                System.out.println();
                example.run(args);
            } finally {
                System.out.println("Example closed");
                closed = true;
            }
        }
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws Exception {
        new ExampleRunner().processArguments(args);
    }
}
