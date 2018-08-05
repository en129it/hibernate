package com.ddv.test.service.state;

/**
 * AbstractState is the generalization of classes that represents a state in the
 * synchronization process between the cache data and the data source of trust.
 */
public abstract class AbstractState {

	protected StateContext context;
	
	/**
	 * Create a new AbstractState instance.
	 * @param aContext Non-null context that gives access synchronization 
	 * context information.
	 */
	protected AbstractState(StateContext aContext) {
		context = aContext;
	}
	
	/**
	 * Get the unique identifier of this state and which can be exchanged in
	 * broadcasting messages with other services.
	 * @return Non-null string.
	 */
	public abstract String getStateCode();
	
	/**
	 * Get the minimum life time of this state in msec. This means that if the
	 * synchronization process arrives in this state, what is the minimum 
	 * guaranteed time during which the system will remain in this state. 
	 * @return A positive number of milliseconds.
	 */
	public abstract int getMinLifeTimeInMsec();
	
	/**
	 * Method invoked when the synchronization process enters this state.
	 * Typically classes that implements this method will notify all the other
	 * services that about the new state.
	 */
	public abstract void onStateEnter();
	
	/**
	 * Method invoked on reception of a message sent by another service to
	 * notify about its synchronization process status.
	 * @param anOtherServiceId The unique identifier of the service that sent
	 * the message.
	 * @param anOtherStateType Non-null synchronization status that the other 
	 * service entered in.
	 */
	public abstract void onMessage(long anOtherServiceId, Class<? extends AbstractState> anOtherStateType);

}
