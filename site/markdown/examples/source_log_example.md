## Source Log Listen Service

This module allows you to receive server log events in real-time

1. [Configuration](#configuration)
2. [Example](#example)

#### Configuration

To start receiving log messages, you need to make sure you register the `<ip:port>` on the game server.
Do this either by directly issuing the rcon command `logaddress_add <ip:port>` on the server or add it to your server configuration files (e.g. server.cfg).

#### Example

To start listening to log messages immediately, just pass a callback to receive the log events in real time. The returned future will only be marked as completed once the service has been closed.

~~~java
import java.net.InetSocketAddress;

public class SourceLogListenerExample extends BaseExample {

    private static final Logger log = LoggerFactory.getLogger(SourceLogListenerExample.class);

    public static void main(String[] args) throws Exception {
        InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
        SourceLogListenService logService = new SourceLogListenService(addres, SourceLogListenerExample::processLogData);
        CompletableFuture<Void> f = logService.listen();
        f.join();
        System.out.println("Log service has been closed");
    }

    private static void processLogData(SourceLogEntry message) {
        System.out.printf("\u001B[36m%s:\u001B[0m %s\n", message.getSourceAddress(), message.getMessage());
    }
}
~~~