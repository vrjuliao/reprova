package br.ufmg.engsoft.reprova.model;

public class Answer {
	/**
	 * Unique id of the answer
	 */
	private String id;

	/**
	 * Description of the answer. Mustn't be null nor empty.
	 */
	private final String description;

	/**
	 * Attachment to the answer. May be null or empty.
	 */
	private final String attachment;
	
	/**
	 * Whether the question is private.
	 */
	private final boolean pvt;
	
	public String getId() {
		return this.id;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getAttachment() {
		return this.attachment;
	}
	
	public boolean getPvt() {
	    return this.pvt;
	}
	
	public Answer(String description, String attachment, boolean pvt) {
		this.description = description;
		this.attachment = attachment;
		this.pvt = pvt;
	}
}
