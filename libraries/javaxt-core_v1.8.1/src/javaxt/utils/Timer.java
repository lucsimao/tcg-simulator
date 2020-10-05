package javaxt.utils;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//******************************************************************************
//**  Timer Class
//******************************************************************************
/**
 *   Used to schedule tasks for future execution in a background thread. Tasks
 *   may be scheduled for one-time execution, or for repeated execution at
 *   regular intervals.
 *
 *   Unlike the java.util.Timer class, this implementation does not silently
 *   cancel tasks if a task encounters an exception.
 *
 ******************************************************************************/

public class Timer {

    private Scheduler scheduler;


    public Timer(){
        scheduler = new Scheduler();
    }


  //**************************************************************************
  //** schedule
  //**************************************************************************
  /** Schedules the specified task for execution after the specified delay.
   *
   * @param task  task to be scheduled.
   * @param delay delay in milliseconds before task is to be executed.
   */
    public void schedule(Runnable task, long delay) {
        scheduleAtFixedRate(task, delay, 0);
    }


  //**************************************************************************
  //** schedule
  //**************************************************************************
  /** Schedules the specified task for execution at the specified time. If the
   *  time is in the past, the task is scheduled for immediate execution.
   *
   * @param task task to be scheduled.
   * @param time time at which task is to be executed.
   */
    public void schedule(Runnable task, java.util.Date time) {
        long delay = time.getTime() - System.currentTimeMillis();
        scheduleAtFixedRate(task, delay, 0);
    }


  //**************************************************************************
  //** scheduleAtFixedRate
  //**************************************************************************
  /** Schedules the specified task for repeated fixed-rate execution, 
   *  beginning at the specified time. Subsequent executions take place at
   *  regular intervals, separated by the specified period.
   *
   * @param task Task to be scheduled.
   * @param firstTime First time at which task is to be executed.
   * @param period Time in milliseconds between successive task executions.
   */
    public void scheduleAtFixedRate(Runnable task, java.util.Date firstTime, long period){
        long delay = firstTime.getTime() - System.currentTimeMillis();
        scheduleAtFixedRate(task, delay, period);
    }


  //**************************************************************************
  //** scheduleAtFixedRate
  //**************************************************************************
  /** Schedules the specified task for repeated fixed-rate execution,
   *  beginning at the specified time. Subsequent executions take place at
   *  regular intervals, separated by the specified period.
   *
   * @param task Task to be scheduled.
   * @param delay delay in milliseconds before task is to be executed.
   * @param period Time in milliseconds between successive task executions.
   */
    public void scheduleAtFixedRate(Runnable task, long delay, long period){
        if (delay<0) delay=0;
        if (period<=0) scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
        else scheduler.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
    }


    public void cancel(){
        scheduler.shutdown();
    }

    public boolean initialized(){
        return scheduler.task!=null;
    }

  //**************************************************************************
  //** Scheduler Class
  //**************************************************************************
  /** Simple wrapper for the ScheduledThreadPoolExecutor service. This class
   *  provides a mechanism to trap runtime errors thrown by an individual
   *  task. Code adapted from:
   *  http://code.nomad-labs.com/2011/12/09/mother-fk-the-scheduledexecutorservice/
   */
    private class Scheduler extends ScheduledThreadPoolExecutor {

        private String task;
        private Runnable command;
        private long initialDelay;
        private long period;
        private TimeUnit unit;

        public Scheduler() {
            super(1);
        }


        @Override
        public ScheduledFuture schedule(Runnable command, long initialDelay, TimeUnit unit) {
            this.task = "schedule";
            this.command = command;
            this.initialDelay = initialDelay;
            this.unit = unit;

            return init();
        }

        @Override
        public ScheduledFuture scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
            this.task = "scheduleAtFixedRate";
            this.command = command;
            this.initialDelay = initialDelay;
            this.period = period;
            this.unit = unit;

            return init();
        }

        
        @Override
        public ScheduledFuture scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {

            this.task = "scheduleWithFixedDelay";
            this.command = command;
            this.initialDelay = initialDelay;
            this.period = delay; //<-- hijacking the period variable...
            this.unit = unit;

            return init();
        }
        

        private ScheduledFuture init(){
            if (task.equals("schedule")){
                return super.schedule(wrapRunnable(command), initialDelay, unit);
            }
            else if (task.equals("scheduleAtFixedRate")){
                return super.scheduleAtFixedRate(wrapRunnable(command), initialDelay, period, unit);
            }
            else if (task.equals("scheduleWithFixedDelay")){
                return super.scheduleWithFixedDelay(wrapRunnable(command), initialDelay, period, unit);
            }
            return null;
        }

        private Runnable wrapRunnable(Runnable command) {
            return new LogOnExceptionRunnable(command);
        }

        private class LogOnExceptionRunnable implements Runnable {
            private Runnable theRunnable;

            public LogOnExceptionRunnable(Runnable theRunnable) {
                super();
                this.theRunnable = theRunnable;
            }

            @Override
            public void run() {
                try {
                    theRunnable.run();
                }
                catch(Exception e) {
                  //Restart the scheduled task!
                    init();
                }
            }
        }
    }
}
