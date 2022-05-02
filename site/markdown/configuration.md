## Client Configuration

1. [Option Classes](#list-of-available-option-classes)
2. [Examples](#examples)
    1. [Mixing it all together](#mixing-it-all-together)

Every client optionally accepts a type of `Options` argument in it's constructor, allowing you to provide your own pre-defined configuration to the client. The `Options` interface is simply a container for `Option` instances.

The library provides an `OptionsBuilder` class to facilitate in defining custom configuration options in a type-safe manner for a specific module. This builder class can be instantiated by calling the static method `builder()` of an options container class (e.g. `SourceQueryOptions.builder()`)

#### List of available option classes

Options with scope `All` can be used on any module while those with restricted scopes can only be used within their associated containers. For example, you are not allowed to add `SourceRconOptions.USE_TERMINATOR_PACKET` option inside a container of type `SourceQueryOptions` while options with global scope (e.g. GeneralOptions, ConnectOptions) are allowed.

| Options Class       | Description                                                                | Scope        |
|---------------------|----------------------------------------------------------------------------|--------------|
| GeneralOptions      | Options that can be used by all modules.                                   | All          |
| ConnectOptions      | Failsafe options that applies to the process of acquiring a new connection | All          |
| FailsafeOptions     | Failsafe options that applies to module specific operations                | All          |
| SourceQueryOptions  | Options specific to the Source query module                                | Source Query |
| MasterServerOptions | Options specific to the Master server query module                         | Master Query |
| SourceRconOptions   | Options specific to the Source RCON query module                           | RCON Query   | 
| HttpOptions         | Options specific to the Web API module                                     | Web API      |

#### Examples

##### Mixing it all together

Here is an example of adjusting the configuration parameters of the `SourceQueryClient`. Combined with `GeneralOptions`, `ConnectOptions` and module specific `FailsafeOptions`.

```java
class ConfigExample {

    public static void main(String[] args) {
        //SourceRconOptions options = SourceRconOptions.builder()
        //MasterServerOptions options = MasterServerOptions.builder()
        //HttpOptions options = HttpOptions.builder();
        SourceQueryOptions options = SourceQueryOptions.builder()
                                                       .option(GeneralOptions.CONNECTION_POOLING, false) //disable connection pooling for source queries
                                                       .option(ConnectOptions.FAILSAFE_RETRY_DELAY, 5000L) //change retry duration to 5 seconds if connection to the game server fails
                                                       .option(FailsafeOptions.FAILSAFE_RATELIMIT_ENABLED, true) //turn on rate limiting for server queries
                                                       .option(FailsafeOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH) //change rate limit strategy for source queries
                                                       .option(GeneralOptions.THREAD_EXECUTOR_SERVICE, customExecutor) //provide a custom executor service
                                                       .option(GeneralOptions.READ_TIMEOUT, 5000) //change socket read timeout duration to 5 seconds
                                                       .build();
        SourceQueryClient client = new SourceQueryClient(options);
        //...    
    }
}
```