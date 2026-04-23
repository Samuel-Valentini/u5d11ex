package samuelvalentini.u5d11ex.utility;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import samuelvalentini.u5d11ex.entity.Employee;

@Component
public class EmailSender {
    private final String domainName;
    private final String apiKey;

    public EmailSender(@Value("${mailgun.domainName}") String domainName, @Value("${mailgun.apiKey}") String apiKey) {
        this.domainName = domainName;
        this.apiKey = apiKey;
    }

    public void sendRegistrationEmail(Employee recipient) {
        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.domainName + "/messages")
                .basicAuth("api", this.apiKey)
                .queryString("from", "samuel@github.com")
                .queryString("to", recipient.getEmail())
                .queryString("subject", "Benvenuto sulla nostra piattaforma!")
                .queryString("text", "Ciao " + recipient.getFirstName() + ", la tua registrazione è andata a buon fine!")
                .asJson();

        System.out.println(response.getBody());


    }
}
