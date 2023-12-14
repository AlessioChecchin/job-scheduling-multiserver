package scheduling.policy;

import events.entries.Entry;

import scheduling.Scheduler;
import scheduling.Server;

/**
 * Represents a scheduling policy.
 */
public interface SchedulingPolicy
{
    /**
     * Selects a server from a scheduler to handle an entry.
     * @param entry The entry to assign to a server.
     * @param scheduler The scheduler that handles entries and servers.
     * @return The selected server.
     */
    Server selectServer(Entry entry, Scheduler scheduler);
}
