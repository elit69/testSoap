package hello;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class Debug {
	public static void debugObjectV2(Object obj) {
		System.out.println("##############"+obj.getClass());
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("###############################################");
	}
	public static HashMap<String, Object> jsonToHash(String json){
		ObjectMapper mapper = new ObjectMapper();
		TypeFactory typeFactory = mapper.getTypeFactory();
		MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
		HashMap<String, Object> map = null;
		try {
			 map = mapper.readValue(json, mapType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}
