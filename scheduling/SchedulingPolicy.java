package scheduling;

import entries.Entry;

public interface SchedulingPolicy
{
    Server selectServer(Entry entry, Scheduler scheduler);
}
