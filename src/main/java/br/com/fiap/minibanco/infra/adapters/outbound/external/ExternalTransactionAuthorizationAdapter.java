package br.com.fiap.minibanco.infra.adapters.outbound.external;

import br.com.fiap.minibanco.application.DTO.AuthTransactionDTO;
import br.com.fiap.minibanco.domain.ports.outbound.ExternalTransactionAuthorizationPort;
import br.com.fiap.minibanco.infra.restClient.ApiUrls;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ExternalTransactionAuthorizationAdapter implements ExternalTransactionAuthorizationPort {

    @Override
    public boolean isTransactionAuthorized() {
        try {
            RestClient restClient = RestClient.builder()
                    .baseUrl(ApiUrls.BASE_URL_AUTH_TRANSACTIONS)
                    .build();
            AuthTransactionDTO response = restClient.get()
                    .uri(ApiUrls.URL_AUTH_TRANSACTIONS)
                    .retrieve()
                    .body(AuthTransactionDTO.class);
            return response != null && response.getData() != null && response.getData().isAuthorization();
        } catch (Exception e) {
            return false;
        }
    }
}
