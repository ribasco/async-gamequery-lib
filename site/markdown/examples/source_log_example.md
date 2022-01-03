## Source Log Listen Service

### Configuration

To start receiving log messages, you need to make sure you register the `<ip:port>` on the game server. 
Do this either by directly issuing the rcon command `logaddress_add <ip:port>` on the server or add it to your server configuration files (e.g. server.cfg).

### Usage

To start listening to log messages immediately, just pass a callback that will process the log statements. Its as simple as that.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
static void main(String[] args) {
    SourceLogListenService logListenService = new SourceLogListenService();
    logListenService.setLogEventCallback(SourceLogMonitorEx::processLogData)
    logListenService.listen();
}

static void processLogData(SourceLogEntry message) {
    log.info("Got Data : {}", message);
}
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If you use the default constructor, the service will bind itself to a random local port number and will listen to any ip address. If you need to bind to a specific ip/port, then just pass an `InetSocketAddress` to the constructor.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
SourceLogListenService logListenService = new SourceLogListenService(new InetSocketAddress("192.168.1.12", 43813);
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
