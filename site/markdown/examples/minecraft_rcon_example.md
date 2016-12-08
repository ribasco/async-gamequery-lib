## Minecraft RCON Example

Similar to the Source Rcon Query example, the only difference is that you need to set the send packet terminator flag to `false`. 

### Disabling packet terminators

Simply pass the argument to the SourceRconClient constructor.

~~~
SourceRconClient minecraftRconClient = new SourceRconClient(false);

//authenticate...

//send commands...
~~~

