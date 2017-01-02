package hska.iwi.eShopMaster.model.database.dataobjects;

public class Search {
	
	String text;
	
	public Search() {
		
	}

	public Search(String text) {
		this.text = text;
	}
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


}
