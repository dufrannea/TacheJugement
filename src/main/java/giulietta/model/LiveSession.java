package giulietta.model;

import java.util.List;

public class LiveSession {

	private Item current;
	private Person person;
	private List<Item> items;
	
	public LiveSession(List<Item> items) {
		//TODO : assertion.precondition
		assert items != null;
		
	}
	
	/**
	 * @return the items
	 */
	public final List<Item> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public final void setItems(List<Item> items) {
		this.items = items;
	}

	/**
	 * @return the current
	 */
	public final Item getCurrent() {
		return current;
	}
	/**
	 * @param current the current to set
	 */
	public final void setCurrent(Item current) {
		this.current = current;
	}
	/**
	 * @return the person
	 */
	public final Person getPerson() {
		return person;
	}
	/**
	 * @param person the person to set
	 */
	public final void setPerson(Person person) {
		this.person = person;
	}
	

}
