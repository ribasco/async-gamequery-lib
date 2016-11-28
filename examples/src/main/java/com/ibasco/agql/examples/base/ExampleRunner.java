package com.ibasco.agql.examples.base;

import com.ibasco.agql.examples.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ExampleRunner {

    private static final Logger log = LoggerFactory.getLogger(ExampleRunner.class);

    private Map<String, BaseExample> examples = new HashMap<>();

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
        log.info("Examples: {}", args);
        ExampleRunner runner = new ExampleRunner();
        for (String example : args) {
            runner.runExample(example);
        }
    }
}
