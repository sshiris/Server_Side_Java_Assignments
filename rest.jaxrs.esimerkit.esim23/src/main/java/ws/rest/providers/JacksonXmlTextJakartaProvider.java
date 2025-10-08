package ws.rest.providers;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
@Provider
@Produces(MediaType.TEXT_XML)
@Consumes(MediaType.TEXT_XML)
public class JacksonXmlTextJakartaProvider<T>
        implements MessageBodyReader<T>, MessageBodyWriter<T> {
    private final XmlMapper mapper = new XmlMapper();
    @Override
    public boolean isReadable(Class<?> type, Type genericType,
                              Annotation[] annotations, MediaType mediaType) {
        return mediaType.isCompatible(MediaType.TEXT_XML_TYPE);
    }
    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations,
                      MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                      InputStream entityStream) throws IOException {
        return mapper.readValue(entityStream, type);
    }
    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return mediaType.isCompatible(MediaType.TEXT_XML_TYPE);
    }
    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException {
        mapper.writeValue(entityStream, t);
    }
}