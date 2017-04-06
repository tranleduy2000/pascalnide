package net.mabboud.android_tone_player;

/**
 * A buzzer that will continue playing until stop() is called.
 */
public class ContinuousBuzzer extends TonePlayer {
    protected double pausePeriodSeconds = 5;
    protected int pauseTimeInMs = 1;

    /**
     * The time the buzzer should pause for every cycle in milliseconds.
     */
    public int getPauseTimeInMs() {
        return pauseTimeInMs;
    }

    /**
     * The time the buzzer should pause for every cycle in milliseconds.
     */
    public void setPauseTimeInMs(int pauseTimeInMs) {
        this.pauseTimeInMs = pauseTimeInMs;
    }

    /**
     * The time period between when a buzzer pause should occur in seconds.
     */
    public double getPausePeriodSeconds() {
        return pausePeriodSeconds;
    }

    /**
     * The time period between when a buzzer pause should occur in seconds.
     * IE pause the buzzer every X/pausePeriod seconds.
     */
    public void setPausePeriodSeconds(double pausePeriodSeconds) {
        this.pausePeriodSeconds = pausePeriodSeconds;
    }

    protected void asyncPlayTrack() {
        playerWorker = new Thread(new Runnable() {
            public void run() {
                while (isPlaying) {
                    // will pause every x seconds useful for determining when a certain amount
                    // of time has passed while whatever the buzzer is signaling is active
                    // (if pause time not increased then continuous tone)
                    playTone(pausePeriodSeconds, (pauseTimeInMs <= 1));
                    try {
                        Thread.sleep(pauseTimeInMs);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });

        playerWorker.start();
    }
}
