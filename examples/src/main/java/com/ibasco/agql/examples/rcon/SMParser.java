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

package com.ibasco.agql.examples.rcon;

import com.ibasco.agql.core.util.Console;
import com.ibasco.agql.core.util.Strings;
import com.ibasco.agql.protocols.valve.source.query.rcon.SourceRconClient;
import com.ibasco.agql.protocols.valve.source.query.rcon.message.SourceRconCmdResponse;
import org.apache.commons.lang3.StringUtils;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMParser {

    private static final Pattern smPluginFieldValue = Pattern.compile("(?<field>\\w+):\\s(?<value>.+)");

    private static final Pattern smPluginInfo = Pattern.compile("(?<id>\\d*)\\s\"(?<name>.+)\"\\s(\\((?<version>.+)\\))?\\s?by\\s(?<author>.+)");

    private static final Pattern smCvar = Pattern.compile("\\s*(?<name>[\\w_]*)\\s+(?<value>.+)\\R*");

    private static final Pattern smCvarHelp = Pattern.compile("\"(?<name>.+)\"\\s=\\s\"(?<value>.+)?\"\\s?(?:min\\.\\s(?<min>\\S+))?\\s?(?:max\\.\\s(?<max>\\S+))?\\R\\s*(?<misc>.+\\R)?\\s-\\s(?<description>(?s:.)+)");

    private static final Pattern smCmdInfo = Pattern.compile("\\s*(?<name>\\S+)\\s*(?<type>\\S+)\\s+(?<description>.+)\\R?");

    private final SourceRconClient client;

    public SMParser(final SourceRconClient client) {
        this.client = client;
    }

    public CompletableFuture<List<SMPlugin>> getPluginList(InetSocketAddress address) {
        return client.execute(address, "sm plugins list")
                     .thenApply(this::parsePluginList)
                     .thenApplyAsync(list -> extractPluginInfo(address, list)) //NOTE: The following extract methods blocks the EventLoop, thus we need to call this asynchronously
                     .thenApplyAsync(plugins -> extractCvarList(address, plugins))
                     .thenApplyAsync(plugins -> extractCommandList(address, plugins));
    }

    private List<SMPlugin> parsePluginList(SourceRconCmdResponse response) {
        List<SMPlugin> plugins = new ArrayList<>();
        String pluginList = response.getResult();
        Matcher matcher = smPluginInfo.matcher(pluginList);
        while (matcher.find()) {
            String id = matcher.group("id");
            String name = matcher.group("name");
            String version = matcher.group("version");
            String author = matcher.group("author");
            plugins.add(new SMPlugin(id, name, version, author));
        }
        return plugins;
    }

    private List<SMPlugin> extractPluginInfo(InetSocketAddress address, List<SMPlugin> pluginList) {
        try {
            CountDownLatch latch = new CountDownLatch(pluginList.size());
            for (SMPlugin plugin : pluginList) {
                client.execute(address, String.format("sm plugins info %s", plugin.getId()))
                      .thenCombine(CompletableFuture.completedFuture(plugin), this::parsePluginInfo)
                      .whenComplete((sourcemodPlugin, throwable) -> latch.countDown());
            }
            latch.await();
        } catch (InterruptedException e) {
            throw new CompletionException(e);
        }
        return pluginList;
    }

    private List<SMPlugin> extractCvarList(InetSocketAddress address, List<SMPlugin> plugins) {
        try {
            CountDownLatch latch = new CountDownLatch(plugins.size());
            for (SMPlugin plugin : plugins) {
                client.execute(address, String.format("sm cvars %s", plugin.getId()))
                      .thenCombine(CompletableFuture.completedFuture(plugin), this::parseCvarList)
                      .thenApplyAsync(p -> extractConVarHelp(address, p))
                      .whenComplete((sourcemodPlugin, throwable) -> latch.countDown());
            }
            latch.await();
        } catch (InterruptedException e) {
            throw new CompletionException(e);
        }
        return plugins;
    }

    private List<SMPlugin> extractCommandList(InetSocketAddress address, List<SMPlugin> plugins) {
        try {
            CountDownLatch cmdLatch = new CountDownLatch(plugins.size());
            for (SMPlugin plugin : plugins) {
                client.execute(address, String.format("sm cmds %s", plugin.getId()))
                      .thenCombine(CompletableFuture.completedFuture(plugin), this::parseCmdInfo)
                      .whenComplete((sourcemodPlugin, throwable) -> cmdLatch.countDown());
            }
            cmdLatch.await();
        } catch (InterruptedException e) {
            throw new CompletionException(e);
        }
        return plugins;
    }

    private SMPlugin parsePluginInfo(SourceRconCmdResponse response, SMPlugin plugin) {
        Matcher matcher = smPluginFieldValue.matcher(response.getResult());
        while (matcher.find()) {
            String field = matcher.group("field");
            String value = matcher.group("value");

            switch (field.toLowerCase()) {
                case "filename": {
                    plugin.setFilename(value);
                    break;
                }
                case "title": {
                    plugin.setTitle(value);
                    break;
                }
                case "author": {
                    plugin.setAuthor(value);
                    break;
                }
                case "version": {
                    plugin.setVersion(value);
                    break;
                }
                case "timestamp": {
                    plugin.setTimestamp(value);
                    break;
                }
                case "url": {
                    plugin.setUrl(value);
                    break;
                }
                case "hash": {
                    plugin.setHash(value);
                    break;
                }
            }
        }
        return plugin;
    }

    private SMPlugin parseCvarList(SourceRconCmdResponse response, SMPlugin plugin) {
        String cvarString = response.getResult();
        if (Strings.isBlank(cvarString) || cvarString.contains("[SM] No convars found for")) {
            return plugin;
        }
        //Strip headers
        cvarString = cvarString.replaceAll("\\[SM]\\sListing\\s\\d*\\sconvars.+\\R*", "");
        cvarString = cvarString.replaceAll("\\s+\\[Name]\\s+\\[Value]\\R*", "");
        Matcher matcher = smCvar.matcher(cvarString);
        while (matcher.find()) {
            String name = matcher.group("name");
            String value = matcher.group("value");
            plugin.getConVars().add(new SMConVar(name, value));
        }
        return plugin;
    }

    private SMPlugin extractConVarHelp(InetSocketAddress address, SMPlugin plugin) {
        if (!plugin.getConVars().isEmpty()) {
            try {
                CountDownLatch helpLatch = new CountDownLatch(plugin.getConVars().size());
                for (SMConVar conVar : plugin.getConVars()) {
                    client.execute(address, String.format("help %s", conVar.getName()))
                          .thenCombine(CompletableFuture.completedFuture(conVar), this::parseConVarHelp)
                          .whenComplete((cvar, error) -> helpLatch.countDown());
                }
                helpLatch.await();
            } catch (InterruptedException e) {
                throw new CompletionException(e);
            }
        }
        return plugin;
    }

    private SMPlugin parseCmdInfo(SourceRconCmdResponse response, SMPlugin plugin) {
        String cmdString = response.getResult();
        if (cmdString.contains("No commands found for"))
            return plugin;
        //strip headers
        cmdString = cmdString.replaceAll("\\[SM]\\sListing\\scommands\\sfor:.+\\R\\s*\\[Name]\\s*\\[Type]\\s*\\[Help]\\R", "");
        Matcher matcher = smCmdInfo.matcher(cmdString);
        while (matcher.find()) {
            String name = matcher.group("name");
            String type = matcher.group("type");
            String description = matcher.group("description");
            plugin.getCommands().add(new SMCommand(name, type, description));
        }
        return plugin;
    }

    private SMConVar parseConVarHelp(SourceRconCmdResponse response, SMConVar conVar) {
        if (Strings.isBlank(response.getResult()) || response.getResult().contains("no cvar or command named"))
            return conVar;
        Matcher matcher = smCvarHelp.matcher(response.getResult());
        while (matcher.find()) {
            String name = matcher.group("name");
            String value = matcher.group("value");
            String minValue = matcher.group("min");
            String maxValue = matcher.group("max");
            String description = matcher.group("description");
            if (Strings.isBlank(conVar.getName()))
                conVar.setName(name);
            if (Strings.isBlank(conVar.getValue()))
                conVar.setValue(value);
            conVar.setMinValue(minValue);
            conVar.setMaxValue(maxValue);
            conVar.setDescription(description);
        }
        return conVar;
    }

    public String prettyFormat(List<SMPlugin> plugins) {
        StringBuilder output = new StringBuilder();
        for (SMPlugin plugin : plugins) {
            String data = Console.colorize(true)
                                 .green("Id: ").white("%-3s ", plugin.getId())
                                 .green("Name: ").white("%-40s ", plugin.getFilename())
                                 .green("Url: ").white("%-40s ", StringUtils.abbreviate(plugin.getUrl(), 39))
                                 .green("Hash: ").white("%-32s ", plugin.getHash())
                                 .green("Total Cvars: ").white("%02d ", plugin.getConVars().size())
                                 .green("Total Commands: ").white("%02d", plugin.getCommands().size())
                                 .toString();
            output.append(data);

            //cvars
            if (!plugin.getConVars().isEmpty()) {
                int cvarCount = 1;
                output.append("\n");
                output.append(Console.colorize(true).yellow("\t[ConVars]\n").toString());
                String header = Console.colorize(true)
                                       .blue("%-4s", "ID").reset()
                                       .blue("%-40s", "Name").reset()
                                       .blue("%-30s", "Value").reset()
                                       .blue("%-30s", "Description").reset()
                                       .blue("%-30s", "Min").reset()
                                       .blue("%-30s", "Max").reset()
                                       .blue("%-30s", "Default").reset()
                                       .toString();
                output.append("\t").append(header).append("\n");
                for (SMConVar conVar : plugin.getConVars()) {
                    output.append("\t");
                    String cvarOutput = Console.colorize(true)
                                               .purple("%02d) ", cvarCount++)
                                               .white("%-40s", conVar.getName())
                                               .white("%-30s", StringUtils.abbreviate(conVar.getValue(), 29))
                                               .white("%-30s", StringUtils.abbreviate(StringUtils.strip(conVar.getDescription(), "\r\n"), 29))
                                               .white("%-30s", conVar.getMinValue())
                                               .white("%-30s", conVar.getMaxValue())
                                               .white("%-30s", conVar.getDefaultValue())
                                               .reset()
                                               .toString();
                    output.append(cvarOutput).append("\n");
                }
            }
            //commands
            if (!plugin.getCommands().isEmpty()) {
                int cmdCount = 1;
                output.append("\n");
                output.append(Console.colorize(true).yellow("\t[Commands]\n").toString());
                String header = Console.colorize(true)
                                       .blue("%-4s", "ID").reset()
                                       .blue("%-40s", "Name").reset()
                                       .blue("%-30s", "Description").reset()
                                       .blue("%-30s", "Type").reset()
                                       .toString();
                output.append("\t").append(header).append("\n");
                for (SMCommand command : plugin.getCommands()) {
                    output.append("\t");
                    String cmdOutput = Console.colorize(true)
                                              .purple("%02d) ", cmdCount++)
                                              .white("%-40s", command.getName())
                                              .white("%-30s", StringUtils.abbreviate(StringUtils.strip(command.getDescription(), "\r\n"), 29))
                                              .white("%-30s", command.getType())
                                              .toString();
                    output.append(cmdOutput).append("\n");
                }
            }
            output.append("\n");
        }
        return output.toString();
    }
}