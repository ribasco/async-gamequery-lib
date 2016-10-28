package org.ribasco.asyncgamequerylib.core.transport.http.processors;

import org.ribasco.asyncgamequerylib.core.exceptions.AsyncGameLibUncheckedException;
import org.ribasco.asyncgamequerylib.core.transport.http.ContentTypeProcessor;

public class XmlContentTypeProcessor extends ContentTypeProcessor {
    @Override
    protected Object processContent(String body) {
        throw new AsyncGameLibUncheckedException("Not yet implemented");
    }
}
