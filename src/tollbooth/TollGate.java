/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package tollbooth;

import tollbooth.gatecontroller.GateController;
import tollbooth.TollboothException;

/**
 * The TollGate contains everything about a tollgate in a tollbooth.
 * @version Feb 3, 2016
 */
public class TollGate
{
	private final GateController controller;
	private final SimpleLogger logger;
	private int openCount;
	private int closeCount;
	private final int maxFailures;
	private boolean unresponsiveMode;
	
	/**
	 * Constructor that takes the actual gate controller and the logger.
	 * @param controller the GateController object.
	 * @param logger the SimpleLogger object.
	 */
	public TollGate(GateController controller, SimpleLogger logger) {
		this.controller = controller;
		this.logger = logger;
		this.openCount = 0;
		this.closeCount = 0;
		this.maxFailures = 3;
		this.unresponsiveMode = false;
	}
	
	public enum Action{
		OPEN, CLOSE, RESET
	}
	
	/**
	 * Open the gate.
	 * @throws TollboothException
	 */
	public void open() throws TollboothException
	{				
		if(this.controller.isOpen()){
			return;
		}
		this.actionDispatch(this.controller, Action.OPEN);
	}
	
	/**
	 * Close the gate
	 * @throws TollboothException
	 */
	public void close() throws TollboothException
	{
		if(!this.controller.isOpen()){
			return;
		}
		this.actionDispatch(this.controller, Action.CLOSE);
	}
	
	/**
	 * Reset the gate to the state it was in when created with the exception of the
	 * statistics.
	 * @throws TollboothException
	 */
	public void reset() throws TollboothException
	{
		this.actionDispatch(this.controller, Action.RESET);
	}
	
	public void takeControllerAction(Action act) throws TollboothException{
			switch(act){
				case OPEN:
					this.controller.open();
					this.openCount++;
					break;
				case CLOSE:
					this.controller.close();
					this.closeCount++;
					break;
				case RESET:
					this.controller.reset();
					break;
			}
	}
	
	public void actionDispatch(GateController controller, Action act) throws TollboothException{
		String logMessage;
		String actionName = this.getActionName(act);
		
		//Otherwise, attempt to close until success or until maxFailures attempts
		for(int attempt = 1; attempt <= maxFailures; attempt++){
			//If the gate is not responding to log messages
			if(this.unresponsiveMode == true){
				logMessage = String.format("%s: will not respond", actionName);
				this.logger.accept(new LogMessage(logMessage));
				throw new TollboothException(logMessage);
			}
			
			try{
				this.takeControllerAction(act);
				logMessage = String.format("%s: successful", actionName);
				this.logger.accept(new LogMessage(logMessage));
				return;
			} catch(TollboothException e) {
				if(attempt == maxFailures){
					logMessage = String.format("%s: unrecoverable malfunction", actionName);
					logger.accept(new LogMessage(logMessage));
					this.unresponsiveMode = true;
				} else{
					logMessage = String.format("%s: malfunction", actionName);
					logger.accept(new LogMessage(logMessage));
				}					
			}
		}
	}
	
	public String getActionName(Action act){
		String actionName = "Unknown";
		switch(act){
			case OPEN:
				actionName = "open";
				break;
			case CLOSE:
				actionName = "close";
				break;
			case RESET:
				actionName = "reset";
				break;
		}
		return actionName;
	}
	
	/**
	 * @return true if the gate is open
	 * @throws TollboothException 
	 */
	public boolean isOpen() throws TollboothException
	{
			return controller.isOpen();
	}
	
	/**
	 * @return the number of times that the gate has been opened (that is, the
	 *  open method has successfully been executed) since the object was created.
	 */
	public int getNumberOfOpens()
	{
		return openCount;
	}
	
	/**
	 * @return the number of times that the gate has been closed (that is, the
	 *  close method has successfully been executed) since the object was created.
	 */
	public int getNumberOfCloses()
	{
		return this.closeCount;
	}
}
