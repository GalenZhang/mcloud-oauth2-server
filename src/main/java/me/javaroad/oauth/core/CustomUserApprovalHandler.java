package me.javaroad.oauth.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;

import java.util.Collection;

@Slf4j
public class CustomUserApprovalHandler extends ApprovalStoreUserApprovalHandler {

    private boolean useApprovalStore = true;

    private ClientDetailsService clientDetailsService;

    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
        super.setClientDetailsService(clientDetailsService);
    }

    public void setUseApprovalStore(boolean useApprovalStore) {
        this.useApprovalStore = useApprovalStore;
    }

    @Override
    public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest,
        Authentication userAuthentication) {

        boolean approved = false;
        // If we are allowed to check existing approvals this will short circuit the decision
        if (useApprovalStore) {
            authorizationRequest = super.checkForPreApproval(authorizationRequest, userAuthentication);
            approved = authorizationRequest.isApproved();
        } else {
            if (clientDetailsService != null) {
                Collection<String> requestedScopes = authorizationRequest.getScope();
                try {
                    ClientDetails client = clientDetailsService
                        .loadClientByClientId(authorizationRequest.getClientId());
                    for (String scope : requestedScopes) {
                        if (client.isAutoApprove(scope)) {
                            approved = true;
                            break;
                        }
                    }
                } catch (ClientRegistrationException e) {
                    log.info(e.getMessage(), e);
                }
            }
        }
        authorizationRequest.setApproved(approved);

        return authorizationRequest;

    }

}