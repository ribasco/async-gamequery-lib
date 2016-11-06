package org.ribasco.agql.core.transport.http.processors;

import org.ribasco.agql.core.exceptions.AsyncGameLibUncheckedException;
import org.ribasco.agql.core.transport.http.ContentTypeProcessor;

public class XmlContentTypeProcessor extends ContentTypeProcessor {
    @Override
    protected Object processContent(String body) {
        throw new AsyncGameLibUncheckedException("Not yet implemented");
    }
}
