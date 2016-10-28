package org.ribasco.asyncgamequerylib.core.transport.http.processors;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.ribasco.asyncgamequerylib.core.transport.http.ContentTypeProcessor;

public class JsonContentTypeProcessor extends ContentTypeProcessor<JsonElement> {
    private JsonParser parser = new JsonParser();

    @Override
    protected JsonElement processContent(String body) {
        return parser.parse(body);
    }
}
