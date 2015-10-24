package schema.tf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.schematf.core.strategy.DerivativeDataLoader;
import uk.co.solong.schematf.model.MetaData;
import uk.co.solong.schematf.web.controllers.api.QualitiesController;
import uk.co.solong.schematf.web.controllers.api.SchemaController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class TestQualityInsertion {

    @Test
    public void test() throws ExecutionException {
        SchemaDao schemaDao = new SchemaDao() {
            
            @Override
            public void persist(JsonNode schema, MetaData metadata) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void persist(JsonNode schema) {
                // TODO Auto-generated method stub
                
            }
            
            
            
            @Override
            public boolean exists(String hashCode) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public JsonNode getFromDataCache(DerivativeDataLoader<JsonNode> ITEM_KEY) throws ExecutionException {
                ObjectMapper o = new ObjectMapper();
                try {
                    return o.readTree("{\"0\":{\"id\":0,\"name\":\"Normal\"},\"1\":{\"id\":1,\"name\":\"Genuine\"},\"2\":{\"id\":2,\"name\":\"rarity2\"},\"3\":{\"id\":3,\"name\":\"Vintage\"},\"4\":{\"id\":4,\"name\":\"rarity3\"},\"5\":{\"id\":5,\"name\":\"Unusual\"},\"6\":{\"id\":6,\"name\":\"Unique\"},\"7\":{\"id\":7,\"name\":\"Community\"},\"8\":{\"id\":8,\"name\":\"Valve\"},\"9\":{\"id\":9,\"name\":\"Self-Made\"},\"10\":{\"id\":10,\"name\":\"Customized\"},\"11\":{\"id\":11,\"name\":\"Strange\"},\"12\":{\"id\":12,\"name\":\"Completed\"},\"13\":{\"id\":13,\"name\":\"Haunted\"},\"14\":{\"id\":14,\"name\":\"Collector's\"},\"15\":{\"id\":15,\"name\":\"Decorated Weapon\"}}");
                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }
        };
        QualitiesController t = new QualitiesController(schemaDao );
        JsonNode p = t.getAllQualitiesSimple(new HttpServletResponse() {
            
            @Override
            public void setLocale(Locale loc) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setContentType(String type) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setContentLength(int len) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setCharacterEncoding(String charset) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setBufferSize(int size) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void resetBuffer() {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void reset() {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public boolean isCommitted() {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public PrintWriter getWriter() throws IOException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getContentType() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getCharacterEncoding() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getBufferSize() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public void flushBuffer() throws IOException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setStatus(int sc, String sm) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setStatus(int sc) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setIntHeader(String name, int value) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setHeader(String name, String value) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setDateHeader(String name, long date) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void sendRedirect(String location) throws IOException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void sendError(int sc, String msg) throws IOException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void sendError(int sc) throws IOException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public int getStatus() {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public Collection<String> getHeaders(String name) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Collection<String> getHeaderNames() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String encodeUrl(String url) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String encodeURL(String url) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String encodeRedirectUrl(String url) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String encodeRedirectURL(String url) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public boolean containsHeader(String name) {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public void addIntHeader(String name, int value) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void addHeader(String name, String value) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void addDateHeader(String name, long date) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void addCookie(Cookie cookie) {
                // TODO Auto-generated method stub
                
            }
        });
    }

}
