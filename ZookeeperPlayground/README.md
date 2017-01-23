### Zero edition, finish three components and let them communicate between each other through RPC

Done

### Zero point five edition, make jar and let any task handled in the three component.

Note:

1. upload jar to back the task
2. need classloader to parse the task

### First edition, master won't down, boss won't down, just need to consider the working status

1. integrate zookeeper to the system to deal with worker down situation. When worker is down, requeue the task for 
future worker request  
2. /dis-scheduler-framework/worker/0,1,2,3 -> record the living workers

### Second edition, worker may down

All zk path start with dis-scheduler-framework

1. /worker/0,1,2,3 -> record the living workers
2. /worker/worker-id/job-id -> job content and status

### Third edition, scheduler may down


### Fourth edition, boss should be able to manipulate the submitted task (cancel or set timeout)

1. How to cancel the task

### Fifth edition, Task should be in shared/exclusive mode, and have recursive tasks

**Note:**

1. one task can be splitted into multi sub tasks and execute in sequential
2. task have dependencies
3. task in transaction