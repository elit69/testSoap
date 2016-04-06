
package hello;

import java.io.StringWriter;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import hello.wsdl.Forecast;
import hello.wsdl.ForecastReturn;
import hello.wsdl.GetCityForecastByZIP;
import hello.wsdl.GetCityForecastByZIPResponse;
import hello.wsdl.Temp;

public class WeatherClient extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(WeatherClient.class);

	public GetCityForecastByZIPResponse getCityForecastByZip(String zipCode) throws JAXBException {
		System.out.println("sdfsdfsdfsdfsdfsd\t"+zipCode);
		GetCityForecastByZIP request = new GetCityForecastByZIP();
		request.setZIP(zipCode);

		log.info("Requesting forecast for " + zipCode);
		
		JAXBContext context = JAXBContext.newInstance(GetCityForecastByZIP.class);
        Marshaller m = context.createMarshaller();
		StringWriter sw = new StringWriter();
		m.marshal(request, sw);
		String xmlString = sw.toString();
		System.out.println(xmlString);
		

		GetCityForecastByZIPResponse response = (GetCityForecastByZIPResponse) getWebServiceTemplate()
				.marshalSendAndReceive(
						"http://wsf.cdyne.com/WeatherWS/Weather.asmx",
						request,
						new SoapActionCallback("http://ws.cdyne.com/WeatherWS/GetCityForecastByZIP"));

		Debug.debugObjectV2(response);
		return response;
	}

	public void printResponse(GetCityForecastByZIPResponse response) {

		ForecastReturn forecastReturn = response.getGetCityForecastByZIPResult();

		if (forecastReturn.isSuccess()) {
			log.info("Forecast for " + forecastReturn.getCity() + ", " + forecastReturn.getState());

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			for (Forecast forecast : forecastReturn.getForecastResult().getForecast()) {

				Temp temperature = forecast.getTemperatures();

				log.info(String.format("%s %s %s°-%s°", format.format(forecast.getDate().toGregorianCalendar().getTime()),
						forecast.getDesciption(), temperature.getMorningLow(), temperature.getDaytimeHigh()));
				log.info("");
			}
		} else {
			log.info("No forecast received");
		}
	}

}
