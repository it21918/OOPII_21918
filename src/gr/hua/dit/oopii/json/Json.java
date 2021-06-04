package gr.hua.dit.oopii.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hua.dit.oopii.it21918.Traveller;

public class Json {
	public void writeJSON(ArrayList<Traveller> in_arraylist)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		AllTravellers data = new AllTravellers();
		data.setCollectionAllTravellers(in_arraylist);
		mapper.writeValue(new File("arraylist.json"), data);
	}

	public ArrayList<Traveller> readJSON() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.enableDefaultTyping();
		AllTravellers data = mapper.readValue(new File("arraylist.json"), AllTravellers.class);		
		return data.getCollectionAllTravellers();
	}
}

class AllTravellers {
	private ArrayList<Traveller> collectionAllTravellers;

	public ArrayList<Traveller> getCollectionAllTravellers() {
		return collectionAllTravellers;
	}

	public void setCollectionAllTravellers(ArrayList<Traveller> collectionAllTravellers) {
		this.collectionAllTravellers = collectionAllTravellers;
	}
}