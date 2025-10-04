package uk.co.nikodem.dFProxyPlugin.Player.Platform;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ParsedPlatformInformationAdapter implements JsonSerializer<ParsedPlatformInformation>, JsonDeserializer<ParsedPlatformInformation> {
    private static final String CLASSNAME = "name";
    private static final String DATA = "data";

    public Class<?> getObjectClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

    @Override
    public JsonElement serialize(ParsedPlatformInformation parsedPlatformInformation, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty(CLASSNAME, parsedPlatformInformation.getClass().getName());
        obj.add(DATA, jsonSerializationContext.serialize(parsedPlatformInformation));
        return obj;
    }

    @Override
    public ParsedPlatformInformation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) obj.get(CLASSNAME);
        String className = prim.getAsString();
        return jsonDeserializationContext.deserialize(obj.get(DATA), getObjectClass(className));
    }
}
