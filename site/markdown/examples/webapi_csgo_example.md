# Counter Strike : Global Offensive Web API

### Available Interfaces

| **Interface Name** | **Description**             |
|--------------------|-----------------------------|
| CsgoServers        | Game Server Status Info     |

**Get Game Server Status**

~~~java
class CsgoExample {

    public static void main(String[] args) throws Exception {
        try (CsgoWebApiClient apiClient = new CsgoWebApiClient("auth-token-here")) {
            CsgoServers servers = new CsgoServers(apiClient);
            CsgoGameServerStatus status = servers.getGameServerStatus().get();
            System.out.printf("Game Server Status : %s%n", status);
        }
    }
}
~~~

**Get Map Playtime Info**

~~~java
class CsgoExample {

    public static void main(String[] args) throws Exception {
        try (CsgoWebApiClient apiClient = new CsgoWebApiClient("auth-token-here")) {
            CsgoServers servers = new CsgoServers(apiClient);
            CsgoGameMapPlaytimeInfo playtimeInfo = servers.getMapPlaytimeInfo("day", "competitive", "operation").join();
            System.out.printf("Playtime Info: %s%n", playtimeInfo);
        }
    }
}
~~~