package tollbooth;

import java.util.LinkedList;
import java.util.Queue;

public class TollboothLogger implements SimpleLogger {
	
	private Queue<LogMessage> logMessageQueue;
	
	public TollboothLogger(){
		this.logMessageQueue = new LinkedList<LogMessage>();
	}

	@Override
	public void accept(LogMessage message) {
		this.logMessageQueue.add(message);
	}

	@Override
	public LogMessage getNextMessage() {
		if(logMessageQueue.isEmpty()){
			return null;
		} else{
			return this.logMessageQueue.poll();
		}
	}
	
	/**
	 * Returns the size of the log message queue
	 * @return the number of LogMessage objects in the logger queue
	 */
	public int logSize(){
		return this.logMessageQueue.size();
	}
}
