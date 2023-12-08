package scheduling.policy;

import events.entries.Entry;

import scheduling.Scheduler;
import scheduling.Server;

public interface SchedulingPolicy
{
    Server selectServer(Entry entry, Scheduler scheduler);
}
