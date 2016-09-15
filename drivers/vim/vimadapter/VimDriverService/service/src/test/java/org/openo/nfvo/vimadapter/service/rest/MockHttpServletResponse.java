/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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
package org.openo.nfvo.vimadapter.service.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class MockHttpServletResponse implements HttpServletResponse{

    @Override
    public void flushBuffer() throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getBufferSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getCharacterEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getContentType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isCommitted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetBuffer() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setBufferSize(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setCharacterEncoding(String arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setContentLength(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setContentType(String arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setLocale(Locale arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addCookie(Cookie arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addDateHeader(String arg0, long arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addHeader(String arg0, String arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addIntHeader(String arg0, int arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean containsHeader(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String encodeRedirectURL(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeRedirectUrl(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeURL(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeUrl(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendError(int arg0) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendError(int arg0, String arg1) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendRedirect(String arg0) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDateHeader(String arg0, long arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setHeader(String arg0, String arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setIntHeader(String arg0, int arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setStatus(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setStatus(int arg0, String arg1) {
        // TODO Auto-generated method stub
        
    }

}