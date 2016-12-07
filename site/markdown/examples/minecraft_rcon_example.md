## Minecraft RCON Example

Similar to the Source Rcon Query example, the only difference is that you need to disable the packet terminator flag. 

### Disable the terminator flag

Simply pass `false` to the SourceRconClient constructor.

~~~
SourceRconClient minecraftRconClient = new SourceRconClient(false);
~~~

