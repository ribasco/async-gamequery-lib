/*
 * MIT License
 *
 * Copyright (c) 2016 Asynchronous Game Query Library
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ibasco.agql.core.enums;

/**
 * Identifies the stages of the request flow
 */
public enum RequestStatus {
    /**
     * Unprocessed request and is currently in-queue
     */
    NEW,
    /**
     * Request has been removed from the queue and is currently being processed
     */
    ACCEPTED,
    /**
     * Request could not be sent at this time, if marked for RETRY,
     * it will be placed on the bottom of the queue and scheduled for re-processing.
     */
    RETRY,
    /**
     * Indicates that the request is now in preparation stage and will be sent through the transport
     */
    REGISTERED,
    /**
     * A request has been issued to the underlying transport but is currently awaiting for completion
     */
    AWAIT,
    /**
     * Request has been sent through the underlying transport and is awaiting for response
     */
    SENT,
    /**
     * Response from the logger has been received
     */
    DONE
}
