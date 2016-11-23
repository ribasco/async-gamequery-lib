# Counter Strike : Global Offensive Web API

## Available Interfaces

| **Interface Name** | **Description**             |
|--------------------|-----------------------------|
| CsgoServers        | Game Server Status Info     |

**Get Game Server Status**

~~~
CsgoWebApiClient apiClient = new CsgoWebApiClient("auth-token-here");

CsgoServers servers = new CsgoServers(apiClient);

try {
    CsgoGameServerStatus status = servers.getGameServerStatus().get();
    log.info("Game Server Status : {}", status);
} finally {
    apiClient.close();
}
~~~