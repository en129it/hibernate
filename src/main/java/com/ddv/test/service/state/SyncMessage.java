package com.ddv.test.service.state;

/**
 * SyncMessage is the message exchanged between the different application 
 * instances to inform each other about synchronization state changes. One of 
 * these state is a request to start a synchronization of the cache with the 
 * source of trust. The instance that enters that state informs the other 
 * instances which can then reply to abort that operation or abort themselves
 * such operation. If all the instances have the same weight, no synchronization
 * decision would ever be made. Therefore each instance is associated with a 
 * service unique identifier. That value will define a precedence between the
 * instances.
 */
public class SyncMessage {

	/** The unique identifier of the application instance that issues this message. */
	private long serviceId;
	/** The code of the new synchronization status that the application instance entered into. */
	private String statusCode;
	
	/**
	 * Create a new SyncMessage.
	 */
	public SyncMessage() {
		// default constructor
	}
	
	/**
	 * Create a new SyncMessage and initialize it.
	 * @param aServiceId Unique identifier of the application instance that 
	 * issues this message.
	 * @param aStatusCode Non-null code of the new synchronization status that 
	 * the application instance entered into.
	 */
	public SyncMessage(long aServiceId, String aStatusCode) {
		serviceId = aServiceId;
		statusCode = aStatusCode;
	}
	
	public long getServiceId() {
		return serviceId;
	}
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	
	
}
