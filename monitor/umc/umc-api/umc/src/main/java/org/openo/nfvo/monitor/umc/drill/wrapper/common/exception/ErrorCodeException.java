/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.nfvo.monitor.umc.drill.wrapper.common.exception;

/**
 *
 *       extend the Exception,save the error code info
 */
public class ErrorCodeException extends Exception {
    private static final long serialVersionUID = 3220072444842529499L;
    private int categoryCode = 0;
    private int errorCode = 1;
    private String[] arguments = null;

    private static String defaultText = null;

    public static void setDefaultText(String text) {
        defaultText = text;
    }

    public static String getDefaultText() {
        return defaultText;
    }

    public ErrorCodeException(int code, String debugMessage) {
        this(code, debugMessage, null);
    }

    public ErrorCodeException(int code, String debugMessage, String[] arguments) {
        super(debugMessage);

        this.errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeException(Throwable source, int code) {
        this(source, code, (String[]) null);
    }

    public ErrorCodeException(Throwable source, int code, String[] arguments) {
        super(source);

        this.errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeException(Throwable source, int code, String debugMessage) {
        this(source, code, debugMessage, null);
    }

    public ErrorCodeException(Throwable source, int code, String debugMessage, String[] arguments) {
        super(debugMessage, source);

        this.errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeException(int category, int code, String debugMessage) {
        this(category, code, debugMessage, null);
    }

    public ErrorCodeException(int category, int code, String debugMessage, String[] arguments) {
        super(debugMessage);

        this.categoryCode = category;
        this.errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeException(Throwable source, int category, int code) {
        this(source, category, code, (String[]) null);
    }

    public ErrorCodeException(Throwable source, int category, int code, String[] arguments) {
        super(source);

        this.categoryCode = category;
        this.errorCode = code;
        this.arguments = arguments;
    }

    public ErrorCodeException(Throwable source, int category, int code, String debugMessage) {
        this(source, category, code, debugMessage, null);
    }

    public ErrorCodeException(Throwable source, int category, int code, String debugMessage,
            String[] arguments) {
        super(debugMessage, source);

        this.categoryCode = category;
        this.errorCode = code;
        this.arguments = arguments;
    }

    public int getCategory() {
        return this.categoryCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String[] getArguments() {
        return this.arguments;
    }
}
