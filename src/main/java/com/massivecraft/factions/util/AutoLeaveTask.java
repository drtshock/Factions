package com.massivecraft.factions.util;

import com.massivecraft.factions.P;

public class AutoLeaveTask implements Runnable {

    private static AutoLeaveProcessTask task;
    double rate;

    public AutoLeaveTask() {
        this.rate = P.p.conf().factions().getAutoLeaveRoutineRunsEveryXMinutes();
    }

    public synchronized void run() {
        if (task != null && !task.isFinished()) {
            return;
        }

        task = new AutoLeaveProcessTask();
        task.runTaskTimer(P.p, 1, 1);

        // maybe setting has been changed? if so, restart this task at new rate
        if (this.rate != P.p.conf().factions().getAutoLeaveRoutineRunsEveryXMinutes()) {
            P.p.startAutoLeaveTask(true);
        }
    }
}
