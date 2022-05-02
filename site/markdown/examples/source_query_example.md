## Source Server Queries

1. [Supported Queries](#supported-queries)
2. [Supported Failsafe Policies](#supported-failsafe-policies)
3. [Examples](#examples)
   1. [Non-Blocking Queries](#non-blocking-example)
   2. [Blocking Queries](#blocking-example)
   3. [Manually obtaining a challenge number](#obtaining-a-challenge-number-manually)
   4. [Failsafe - Rate limited queries](#rate-limited-queries)

#### Supported Queries

Below are the list of supported server queries

| Name      | Description                                                                         | Protocol                                                                                                             | Requires Challenge |
|-----------|-------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------|:-------------------|
| Info      | General information about the source server (Now supports challenge based requests) | [A2S_INFO](https://developer.valvesoftware.com/wiki/Server_queries#A2S_INFO)                                         | No                 |
| Players   | Information about the players currently present in the source server                | [A2S_PLAYER](https://developer.valvesoftware.com/wiki/Server_queries#A2S_PLAYER)                                     | Yes                |
| Rules     | A list of rules specified by the server                                             | [A2S_RULES](https://developer.valvesoftware.com/wiki/Server_queries#A2S_RULES)                                       | Yes                |
| Challenge | Request a challenge number to be used for challenge based requests.                 | [A2S_SERVERQUERY_GETCHALLENGE](https://developer.valvesoftware.com/wiki/Server_queries#A2S_SERVERQUERY_GETCHALLENGE) | No                 |

#### Supported Failsafe Policies

Below are the list of supported failsafe features available for this module

| Policy       | Options Class   | Enabled by Default | Description                                                                               |
|--------------|-----------------|--------------------|-------------------------------------------------------------------------------------------|
| Retry        | FailsafeOptions | Yes                | A request is re-attempted when a TimeoutException is thrown                               |
| Rate Limiter | FailsafeOptions | No                 | Limit the frequency of requests. Useful if the servers you are querying are rate-limited. |

#### Examples

Note: Every query returns a response of type `SourceQueryResponse`. Below are the following information made available to you by the response object:

| Name    | Description                              | Type              | Method       |
|---------|------------------------------------------|-------------------|--------------|
| Address | The address/port of the server           | InetSocketAddress | getAddress() |
| Request | The originating request                  | AbstractRequest   | getRequest() |
| Result  | The underlying result sent by the server | Object            | getResult()  |

##### Non-Blocking Example

- We use a simple synchronization barrier (CountDownLatch) to allow us to wait unti a response has been received from the server

~~~java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

class NonBlockingQueryExample {

    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        //You can use the try with block to automatically close the client after use.
        try (SourceQueryClient client = new SourceQueryClient()) {
            InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
            CompletableFuture<SourceQueryInfoResponse> infoFuture = client.getInfo(address);
            if (infoFuture.isDone()) {
                SourceQueryInfoResponse response = infoFuture.getNow(null);
                printResponse(response);
                latch.countDown();
            } else {
                infoFuture.whenComplete((info, error) -> {
                    try {
                        //Check if we received an error
                        if (error != null) {
                            error.printStackTrace(System.err);
                            return;
                        }
                        //Display result
                        printResponse(info);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            //wait till we have received a response
            latch.await();
        }
    }

    private static void printResponse(SourceQueryResponse response) {
        System.out.printf("Received response from server (Address: %s, Request: %s, Result: %s)%n", response.getAddress(), response.getRequest(), response.getResult());
    }
}
~~~

##### Blocking Example

~~~java
class BlockingQueryExample {

    public static void main(String[] args) throws Exception {
        //You can use the try with block to automatically close the client after use.
        try (SourceQueryClient client = new SourceQueryClient()) {
            InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
            SourceQueryInfoResponse info = client.getInfo(address).join();
            printResponse(response);
        }
    }

    private static void printResponse(SourceQueryResponse response) {
        System.out.printf("Received response from server (Address: %s, Request: %s, Result: %s)%n", response.getAddress(), response.getRequest(), response.getResult());
    }
}

~~~

##### Obtaining a challenge number manually

Queries such as `getPlayers()` and `getRules()` requires a valid challenge number to be included in your request. Since version **1.0.0**, the library is now configured to automatically obtain a new challenge number if the server requires it. You can still manually obtain a challenge number by calling `SourceQueryClient.getChallenge()` then passing its result to one of the overloaded methods provided by this library:

Overloaded methods accepting a non-null challenge integer.

| Query   | Method                                  |
|---------|-----------------------------------------|
| Info    | getInfo(InetSocketAddress, Integer)     |
| Players | getPlayers(InetSocketAddress, Integer)  |
| Rules   | getRules(InetSocketAddress, Integer)    |

**Notes**

- As of 12/8/2020, valve has [updated](https://steamcommunity.com/discussions/forum/14/2974028351344359625/) A2S_INFO protocol to optionally support challenge based requests. Because of this, some servers might be configured to respond with a challenge request before sending the actual response. The library supports this new change.
- In this example, we obtain challenge number with type `SourceChallengeType.INFO`
- Passing a non-null challenge integer (regardless if its valid or not) will implicitly disable auto-update. This means that the library will not automatically request for a new challenge number. Instead, it will attempt to send the query, but if the server requires a valid challenge number then a `SourceChallengeException` will be thrown.
- You can obtain the updated challenge number via `SourceChallengeException.getChallenge()`.

~~~java
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

class ManualChallengeNumber {

    public static void main(String[] args) throws Exception {

        try (SourceQueryClient client = new SourceQueryClient()) {
            //First we obtain a challenge number from the server
            InetSocketAddress address = new InetSocketAddress("192.168.1.10", 27015);
            CompletableFuture<SourceQueryInfoResponse> infoFuture = queryClient.getChallenge(address, SourceChallengeType.INFO)
                                                                               .thenCompose(challengeResponse -> queryClient.getInfo(challengeResponse.getAddress(), challengeResponse.getResult()));
            if (infoFuture.isDone()) {
                try {
                    SourceQueryInfoResponse response = infoFuture.get();
                    if (response != null) {
                        System.out.printf("[INFO] Response from '%s' = %s%n", response.getAddress(), response.getResult());
                    }
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (cause != null) {
                        if (cause instanceof SourceChallengeException) {
                            Integer newChallengeNumber = ((SourceChallengeException) cause).getChallenge();
                            System.err.printf("Received new challenge number: %d%n", newChallengeNumber);
                            //TODO: resend request with updated challenge number from 'newChallengeNumber'

                        } else {
                            throw cause;
                        }
                    } else {
                        throw e;
                    }
                }
            } else {
                infoFuture.whenComplete((response, error) -> {
                    if (error != null) {
                        //check if we receive SourceChallengeException
                        error.printStackTrace(System.err);
                        return;
                    }
                    System.out.printf("[INFO] Response from '%s' = %s%n", response.getAddress(), response.getResult());
                });
            }
        }

    }
}
~~~

##### Rate limited queries

Server queries are not rate-limited by default. You will have to explicitly enable it via configuration. You can adjust the rate limit strategy to either [BURST](https://failsafe.dev/rate-limiter/#bursty-rate-limiter) or [SMOOTH](https://failsafe.dev/rate-limiter/#smooth-rate-limiter).

```java
import com.ibasco.agql.core.enums.RateLimitType;
import com.ibasco.agql.core.util.FailsafeOptions;
import com.ibasco.agql.protocols.valve.source.query.SourceQueryClient;
import com.ibasco.agql.protocols.valve.source.query.SourceQueryOptions;
import com.ibasco.agql.protocols.valve.source.query.info.SourceQueryInfoResponse;
import com.ibasco.agql.protocols.valve.source.query.info.SourceServer;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class QueryTest {

   private final CountDownLatch latch = new CountDownLatch(50);

   private final AtomicInteger counter = new AtomicInteger();

   public static void main(String[] args) throws Exception {
      new QueryTest().run();
   }

   private void run() throws Exception {
      //sv_max_queries_sec_global 5           // Maximum queries per second to respond to from anywhere. (Default: 60)
      //sv_max_queries_sec 5                  // Maximum queries per second to respond to from a single IP address. (Default 3.0)
      //sv_max_queries_window 200             // Window over which to average queries per second averages. (Default: 30)
      SourceQueryOptions options = SourceQueryOptions.builder()
                                                     //enable rate limiting
                                                     .option(FailsafeOptions.FAILSAFE_RATELIMIT_ENABLED, true)
                                                     //Select rate limit strategy (BURST or SMOOTH)
                                                     .option(FailsafeOptions.FAILSAFE_RATELIMIT_TYPE, RateLimitType.SMOOTH)
                                                     .option(FailsafeOptions.FAILSAFE_RATELIMIT_PERIOD, 5000L)
                                                     .option(FailsafeOptions.FAILSAFE_RATELIMIT_MAX_EXEC, 10L)
                                                     .option(FailsafeOptions.FAILSAFE_RATELIMIT_MAX_WAIT_TIME, 10000L)
                                                     .option(FailsafeOptions.FAILSAFE_RETRY_ENABLED, false)
                                                     .option(FailsafeOptions.FAILSAFE_RETRY_BACKOFF_ENABLED, false)
                                                     .build();
      InetSocketAddress address = new InetSocketAddress("192.168.50.6", 27016);

      //Send 50 requests asynchronously
      try (SourceQueryClient client = new SourceQueryClient(options)) {
         for (int i = 0; i < latch.getCount(); i++) {
            client.getInfo(address).whenComplete(this::printResponse);
         }
         //wait until we have received everything
         System.out.println("Waiting for all futures to complete");
         latch.await();
         System.out.println("Done");
      }
   }

   private void printResponse(SourceQueryInfoResponse response, Throwable error) {
      try {
         if (error != null) {
            error.printStackTrace(System.err);
            return;
         }
         assert response.getAddress() == response.getResult().getAddress();
         SourceServer info = response.getResult();
         System.out.printf("[%s] %03d Got response from '%s' = %s (%d/%d)%n", Thread.currentThread().getName(), counter.incrementAndGet(), response.getAddress(), info.getName(), info.getNumOfPlayers(), info.getMaxPlayers());
      } finally {
         latch.countDown();
      }
   }
}
```

**Output:**

Note: Example program run with with verbose mode enabled `-Dagql.verbose=true`

```bash
Waiting for all futures to complete
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=JJCR7ACAKBY] (Max Rate: 500ms)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=1XTSSSO57ES] (Max Rate: 500ms)
[agql-el-1-4] 001 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=TBVXMV1GDYY] (Max Rate: 500ms)
[agql-el-1-6] 002 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=VRVECM2ZESA] (Max Rate: 500ms)
[agql-el-1-2] 003 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=ESFDUVNH64] (Max Rate: 500ms)
[agql-el-1-8] 004 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=AXBN4ZETEPO] (Max Rate: 500ms)
[agql-el-1-7] 005 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=1O9ALEVB4SA] (Max Rate: 500ms)
[agql-el-1-5] 006 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=96GLQXS8PVM] (Max Rate: 500ms)
[agql-el-1-9] 007 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=VEWKU75GUWY] (Max Rate: 500ms)
[agql-el-1-1] 008 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=XXMDJU820G] (Max Rate: 500ms)
[agql-el-1-3] 009 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=39MRZEXPS] (Max Rate: 500ms)
[agql-el-1-2] 010 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=PID3KZ3K7I] (Max Rate: 500ms)
[agql-el-1-9] 011 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=DICZIFDPNEM] (Max Rate: 500ms)
[agql-el-1-1] 012 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=2WYFR3L1QAK] (Max Rate: 500ms)
[agql-el-1-7] 013 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=RBL1FAVLOEE] (Max Rate: 500ms)
[agql-el-1-6] 014 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=W7J7NKMGDVI] (Max Rate: 500ms)
[agql-el-1-2] 015 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=UXCBZKCSU] (Max Rate: 500ms)
[agql-el-1-8] 016 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=ZOOHXCROHYG] (Max Rate: 500ms)
[agql-el-1-3] 017 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=TIWTCXYJXJW] (Max Rate: 500ms)
[agql-el-1-1] 018 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=0CQCRMMC70] (Max Rate: 500ms)
[agql-el-1-7] 019 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=SH1OAYGII6O] (Max Rate: 500ms)
[agql-el-1-8] 020 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=YGLKXUVIC0] (Max Rate: 500ms)
[agql-el-1-3] 021 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=2XW4WQMVLI] (Max Rate: 500ms)
[agql-el-1-7] 022 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=WN2UFTWQ9CU] (Max Rate: 500ms)
[agql-el-1-3] 023 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=HLVTI2MSWI0] (Max Rate: 500ms)
[agql-el-1-1] 024 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=HSXSNIHOF3A] (Max Rate: 500ms)
[agql-el-1-5] 025 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=ALEU0HRZMX8] (Max Rate: 500ms)
[agql-el-1-5] 026 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=DREFOEDHHZI] (Max Rate: 500ms)
[agql-el-1-9] 027 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=HRBGSQBRBV0] (Max Rate: 500ms)
[agql-el-1-7] 028 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=HJ4DNVEIE] (Max Rate: 500ms)
[agql-el-1-4] 029 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=TO5LGE9DQGO] (Max Rate: 500ms)
[agql-el-1-5] 030 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=XWRZBZYYGBW] (Max Rate: 500ms)
[agql-el-1-1] 031 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=OULEBW5HJO] (Max Rate: 500ms)
[agql-el-1-2] 032 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=G95DOY15ZS] (Max Rate: 500ms)
[agql-el-1-4] 033 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=SEZS0RA0HLY] (Max Rate: 500ms)
[agql-el-1-2] 034 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=FM52D91TCDQ] (Max Rate: 500ms)
[agql-el-1-3] 035 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=GB4FZFIDIDY] (Max Rate: 500ms)
[agql-el-1-9] 036 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=L0JM3UF03QS] (Max Rate: 500ms)
[agql-el-1-5] 037 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=7ODSFHGL5T0] (Max Rate: 500ms)
[agql-el-1-7] 038 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=FAQUB6AJJGI] (Max Rate: 500ms)
[agql-el-1-9] 039 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=YLTOTQDICEW] (Max Rate: 500ms)
[agql-el-1-6] 040 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=8SJVPUELHOS] (Max Rate: 500ms)
[agql-el-1-2] 041 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=ABREN5XZOVW] (Max Rate: 500ms)
[agql-el-1-8] 042 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=ASM0WPZKYEY] (Max Rate: 500ms)
[agql-el-1-4] 043 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=U9GHFY9OXK] (Max Rate: 500ms)
[agql-el-1-9] 044 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=MTIVSFWHFFI] (Max Rate: 500ms)
[agql-el-1-8] 045 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=LRIEY0SGKUI] (Max Rate: 500ms)
[agql-el-1-6] 046 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=ZC5ENB1VQTU] (Max Rate: 500ms)
[agql-el-1-8] 047 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=DXLSTRP9ITA] (Max Rate: 500ms)
[agql-el-1-6] 048 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[rate-limiter-3-1] Acquiring send permit for SourceQueryInfoRequest[id=IRSUCUSSGI0] (Max Rate: 500ms)
[agql-el-1-5] 049 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
[agql-el-1-6] 050 Got response from '/192.168.50.6:27016' = Left 4 Dead 2 Dedicated Test Server #00 (0/8)
Done
[main       ] Requested to release resource 'com.ibasco.agql.core.util.AgqlManagedExecutorService@49fc609f' (Subtrahend: 1, Old Ref Count: 1, New Ref Count: 0)
[main       ] Reference count of resource 'com.ibasco.agql.core.util.AgqlManagedExecutorService@49fc609f' has reached 0. Attempting to close
[main       ] Shutting down executor service 'com.ibasco.agql.core.util.AgqlManagedExecutorService@49fc609f' (Reference count: 0)

Process finished with exit code 0
```