package pl.s21182.sri4;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.edu.pjwstk.s21882.DomekObject;

import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@Component
public class DomkiRepository {
    public static final List<DomekObject> domki = new LinkedList<>();
    public static final HashMap<String, List<String>> skargi = new HashMap<>();

    @PostConstruct
    public void initData() {
        DomekObject domek1 = new DomekObject();
        domek1.setName("domek nad jeziorem");
        domek1.setPrice(200.00);
        domek1.setCapacity(4);
        domek1.getOccupied().add(createXMLGC(new Date()));

        DomekObject domek2 = new DomekObject();
        domek2.setName("szopa");
        domek2.setPrice(3.50);
        domek2.setCapacity(1);

        DomekObject domek3 = new DomekObject();
        domek3.setName("willa");
        domek3.setPrice(499.99);
        domek3.setCapacity(24);

        DomekObject domek4 = new DomekObject();
        domek4.setName("hamak");
        domek4.setPrice(0);
        domek4.setCapacity(1);

        domki.add(domek1);
        domki.add(domek2);
        domki.add(domek3);
        domki.add(domek4);
        for (DomekObject domekObject : domki) {
            skargi.put(domekObject.getName(), new LinkedList<String>());
        }
    }

    @SneakyThrows
    private XMLGregorianCalendar createXMLGC(Date date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }

    public List<DomekObject> getDomki() {
        return domki;
    }

    public boolean zlozSkarge(String domek, String skarga, String nazwisko) {
        skargi.get(domek).add(nazwisko + " : " + skarga);
        return true;
    }
}
