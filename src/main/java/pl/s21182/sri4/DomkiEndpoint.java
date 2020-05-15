package pl.s21182.sri4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.edu.pjwstk.s21882.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Optional;


@Endpoint
public class DomkiEndpoint {
	private static final String NAMESPACE_URI = "http://s21882.pjwstk.edu.pl";

	private DomkiRepository domkiRepository;

	@Autowired
	public DomkiEndpoint(DomkiRepository domkiRepository) {
		this.domkiRepository = domkiRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getListRequest")
	@ResponsePayload
	public GetListResponse getListResponse(@RequestPayload GetListRequest request) {
		GetListResponse response = new GetListResponse();
		for(DomekObject domek: domkiRepository.getDomki()) {
			boolean occupied=false;
			for(XMLGregorianCalendar gc:domek.getOccupied()){
				if(gc.getYear()==request.getDate().getYear()
				&& gc.getMonth()==request.getDate().getMonth()
				&& gc.getDay()==request.getDate().getDay())
					occupied=true;
			}
			if(!occupied)
				response.getDomki().add(domek);
		}
		return response;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "claimDomekRequest")
	@ResponsePayload
	public ClaimDomekResponse claimDomekResponse(@RequestPayload ClaimDomekRequest request) {
		ClaimDomekResponse response = new ClaimDomekResponse();
		Optional<DomekObject> domekObjectOptional=domkiRepository.getDomki().stream().filter(d->d.getName().equals(request.getDomek().getName())).findFirst();
		response.setResponse("zarezerwowano domek");
		response.setSuccess(true);
		if(domekObjectOptional.isPresent()){
			for(XMLGregorianCalendar gc:domekObjectOptional.get().getOccupied()){
				if(gc.getYear()==request.getDate().getYear()
						&& gc.getMonth()==request.getDate().getMonth()
						&& gc.getDay()==request.getDate().getDay())
					response.setSuccess(false);
					response.setResponse("data juz zajeta");
			}
			if(response.isSuccess())
				domekObjectOptional.get().getOccupied().add(request.getDate());
		} else {
			response.setSuccess(false);
			response.setResponse("domek nie istnieje/zostal wybrany bledny domek");
		}
		return response;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "complainRequest")
	@ResponsePayload
	public ComplainResponse complainResponse(@RequestPayload ComplainRequest request) {
		ComplainResponse response = new ComplainResponse();
		response.setSuccess(domkiRepository.zlozSkarge(request.getDomek().getName(), request.getSkarga(), request.getNazwisko()));
		if(response.isSuccess())
			response.setResponse("reklamacja zlozona");
		else
			response.setResponse("wystapil problem");
		return response;
	}
}
