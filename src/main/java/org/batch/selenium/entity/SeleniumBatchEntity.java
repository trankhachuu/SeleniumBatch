package org.batch.selenium.entity;

public class SeleniumBatchEntity {

	private int id;
	
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SeleniumBatchEntity(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public SeleniumBatchEntity() {
		
	}
	
	@Override
    public String toString() {
        return "EntityId" + id + ", EntityName" + name;
    }
}
