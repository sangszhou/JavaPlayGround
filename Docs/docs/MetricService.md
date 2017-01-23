Recording the metrics of the service will help developers know the working condition of the system and bring 

Pros:

1. will help developers knows the working condition of the system
2. will let dev know the bottle neck of the system and do the optimization accordingly

Yet, metrics is still missing in most of the running project within iCam. The problem of missing lies in two fold, the first one is preparation,   
Since mario is a new projectx`


Content:

**What's is metrics data, why we need metric data, what do we capture in metric data.**

Metric data refers to the collection of data that changes frequently, such as memory, cpu usage and so on. It is used to reflect the working status of the whole system and help developers understand how their code works.

There are many types of metric data worth recording, the first one is system metrics such as cpu, memory, disk usage, if your system is running on JVM, you can also have jvm metrics recorded as well. This kind of metric data gives developer the overview of the working condition of the whole system, if the cpu or memory load is high, you might need more powerful hardware or make your node distributed. JVM metrics including garbage collector information, threads status, while there's a lot tools out there to debug a JVM program, metric data could give you a direct impression to your system. Those kind of metric data used to identifying problems. 

The second kind of metric data is more detail than the first one described above. It aims to investigate how your program works in the granularity of method call. How many times a method is called in time range, how long it takes to finish the method call, is there any method call throws an exception. Those kind of metric data help find the bottleneck of your program and optimize it accordingly. Those kind of data is used to investigating the problems.

Nowdays, most system doesn't running in standalone mode, instead they always interact with other systems. The connect point need to be carefully monitored to position the problem. And there's also a kind of data called trace data which is similar to metric data but focus on a group of systems instead of one. The trace data used to detect the bottleneck of the whole cluster and cannot missing in micro services.

**How to record metric data**

Metric data can be stored in two forms: the first one presented as how many 

Monitoring data comes in a variety of forms, the metrics data which are collected more or less continuously. and discrete infrequent occurrences that can provide crucial context for understanding what changed your system's behavior. System metric data should be collected in the first form while method call should adopt the second one since it only make sense when the thing happens. Besides, with discrete data, you can do the aggregation and plot figures just like the first type.


**what's mario metric service, how to use metric data**

Mario ships with a built in metric service which enable both backbone and adoption developers to record metrics without writing extra code or building the infrastructure to store and visualize the data.

Get started:

Write a simple fetcher in mario.

```java
public class DummyFetcher extends Fetcher {
    
    public static Random r = new Random();

    @InjectService
    LoggingService logger;

    @InjectService
    HttpClientService httpClientService;

    RestTemplate restTemplate;

    @Override
    public void init() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = 
                new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        restTemplate = httpClientService.createRestTemplate(clientHttpRequestFactory);
    }


    public void nextEvents(EventCollection ec, int limit) {

        logger.log("Generate 2 random events");

        Event ev1 = Event.create();

        ev1.setIntValue("primary", r.nextInt());
        ev1.setIntValue("secondary", r.nextInt());
        ec.putEvent(ev1);

        logger.log("First event is: " + ev1.toJson().get("primary") + " " + ev1.toJson().get("secondary"));

        Event ev2 = Event.create();

        ev2.setIntValue("primary", r.nextInt());
        ev2.setIntValue("secondary", r.nextInt());
        ec.putEvent(ev2);

        logger.log("Second event is: " + ev2.toJson().get("primary") + " " + ev2.toJson().get("secondary"));

        ResponseEntity<String> result = null;

        Random rand = new Random();

        try {
            int prob = rand.nextInt(100);
            if(prob > 70) {
                result = restTemplate.getForEntity("http://icam-dev-soa-01:2015/user/xinszhou", String.class);
            } else if(prob > 30){
                result = restTemplate.getForEntity("http://icam-dev-soa-01:2015/user/xinszh223ou2", String.class);
            } else {
                result = restTemplate.getForEntity("http://icam-dev-soa-01:2015/us3243er/xinszh223ou2", String.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

This could be the simplest implementation of fetcher. Every time `nextEvents` is called, it just emit two random integer and 
put them into EventCollection and send a http request to user api of soa. In this method, there is no 
single line of metric related code yet the most important metric data you may interest has already collected for you.

Now, you can go to kibana dashboard and visualize the metric data

![](Docs/docs/images/metric_data_visualization.png)

The top figures shows the dump and fetch event number aggregated by adoption name, it record the number of method call of `nextEvent`. The bottom right figure record the status code respond with user api. and bottom right shows the event number
 aggregated by metric name.
 
Those figures are just simple show cases of what you can do with those data, you can plot your metric data according to your 
need. The predefined metrics we are recording now is dump event, fetch event and http request.

```java
public class HttpMetric extends Span {
    String adoption;
    String path;
    String method;
    int statusCode;
    boolean success;
    String exception;
}

public class FetchDumpMetric extends Span {
    String adoption;
    int eventsCount;
}

public abstract class Span implements Metric {

    long start;
    long end;
    long timeConsumed;
    String type = "span";
    String name;
}

public abstract class Event implements Metric {
    long time;
    String type = "event";
    String name;
}
```

Span and Event are two most common usage of metric data. Span usually represent an action with begin and end timing, while
event represent a discrete action itself or we don't care about the begin or end time.

## How adoption/backbone developers use metric service
 
The `get started` part is good start for adoption developers. If you are the one who only need to call Restful API to get the 
data you want to do the computation within mario, then you don't need to do anything. 

If your requirement is little big complex and need more resource to do the fetch/dump task then you might need to do some extra
work to get your metric data. The first scenario is you want to record the metric of specific method cal. Suppose, you have a 
method called `parseFile` and you want to record the filename, file type and how long it takes to parse the file. With those data you can investigate why certain method call is suspicious long.

```java
// define the metric data
class ParseFileMetric extends Span {
    String fileName;
    int fileSize;
};

// add the metric info you interested to metricService
public void parseFile(String fileName) {
    ParseFileMetric metric = new ParseFileMetric();
    metric.setStart(System.currentTimeMillis());
    
    metric.setName("fetch-event");
    metric.setFileName(fileName);
    
    // parse the file
    
    metric.setEnd(System.currentTimeMillis());
    metricService.add(metric);
};
```

to use MetricService, you must have access to marioComponent where you can always get the marioContext from.

The second scenario is you want to access third party storage or services which can not take advantage of RestTemplate. 
We suppose this scenario is rare, so we didn't weave the metric code with those third party libraries as we do for RestTemplate. But if you do have such need, you can weave the metric code just as we talked about above or contact backbone
developer and let them do it for you.

When you need to access API outside mario, use RestTemplate created by HttpClientService, don't create you own one or use 
other http clients.

After the metric data has been recorded, go to kibana dashboard and plot the figure you are interested in.

### How to configure metric service

There are two configurable switches used to control the status of metric service which are all enabled by default. 
The first one is `metric.enable` defined in `mario.conf` and second one is  `metric.enableAdoptionMetric` defined in profile.conf. `metric.enable` is controlled by backbone developers, once set to false, no metric data will be recorded.
`metric.enableAdoptionMetric` is controlled by adoption developer, adoption developer can turned it on or off based on the their own requirements.

### The architecture of mario metric service

### FAQ

1. Understand the concept of container. 

Mario provides annotation based and dependency injection and handles the lifecycle of the injected service, fetcher and dumper.

2. How to archive old data?

The archive strategy has not been decided yet, for now we don't delete any metric data. We gonna see how many data is produced 
after mario go to production and choose archive strategy accordingly.

3. How to know the metric service is running good ?

todo

4. Can I use annotation based metric recording instead of programmatically do that?

No, you cannot do that for now. Annotation based metric recording is a easy way to record metrics but bring AOP to mario doesn't have a high priority so you cannot do that for now.




----
With mario metric service, all you need to do is to pick the information you want and put the metric data into metric service. Then you can visualize those data in the dashboard of kibana. Get started:


what does metric service do?

how it works, the architecture of metric service

[link](Monitoring 101: Collecting the right data)