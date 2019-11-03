package spark.render;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonRender implements ISparkRender<Object> {
    private final Gson gson;
    
    public JsonRender() {
    	gson = new GsonBuilder().create();
    }
    
	@Override
	public Object render(Object o) {
		return gson.toJson(o);
	}

	@Override
	public Object parse(String json,Class<?> c) {
		return gson.fromJson(json, c);
	}
	
}
