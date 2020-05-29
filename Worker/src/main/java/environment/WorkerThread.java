package main.java.environment;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import main.java.entity.Entity;
import main.java.interpreter.Interpreter;

public class WorkerThread extends Thread {

	private LinkedBlockingQueue<Entity> entityStepQueue;
	private LinkedBlockingQueue<Entity> checkSurroundingsQueue;
	private Interpreter interpreter;
	private Environment env;
	public boolean done;
	
	
	public WorkerThread(LinkedBlockingQueue<Entity> entityStepQueue, LinkedBlockingQueue<Entity> checkSurroundingsQueue, Interpreter interpreter, Environment env) {
		this.entityStepQueue = entityStepQueue;
		this.checkSurroundingsQueue = checkSurroundingsQueue;
		this.interpreter = interpreter;
		this.env = env;
		done = false;
	}
	
	@Override
	public void run() {
		Entity currentEntity;
		while (!done) {
			try {
				if (!entityStepQueue.isEmpty()) {
					currentEntity = entityStepQueue.poll(50, TimeUnit.MILLISECONDS);
					if (currentEntity != null && currentEntity.getIsVisible()) {
						currentEntity.onStep(interpreter, env);
					}
				} else {
					currentEntity = checkSurroundingsQueue.poll(50, TimeUnit.MILLISECONDS);
					if (currentEntity != null && currentEntity.getIsVisible()) {
						env.checkSurroundings(currentEntity);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
					
		}
	}

}
