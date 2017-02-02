package aquarium.items;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SillyFish extends Fish {

	public SillyFish(AquariumContent content) {
		super(content);
		createScheduler();
	}

	private void createScheduler() {
		ScheduledExecutorService e = Executors.newScheduledThreadPool(1);
		e.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				setNewTarget(null);
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

}