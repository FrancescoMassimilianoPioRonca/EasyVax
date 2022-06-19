package com.easyvax.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocketMessageBroker


/***
 * WebSocket è un protocollo di comunicazione. Non definisce però alcune cose come ad esempio inviare un messaggio solo agli utenti che sono iscritti a un particolare topic o
 * come inviare un messaggio a un particolare utente. Abbiamo bisogno di STOMP per queste funzionalità.
 * STOMP sta per Simple Text Oriented Messaging Protocol. È un protocollo di messaggistica che definisce il formato e le regole per lo scambio di dati.
 *
 *
 @EnableWebSocketMessageBroker viene utilizzato per abilitare il nostro server WebSocket.
 Implementiamo l'interfaccia WebSocketMessageBrokerConfigurer e
 forniamo l'implementazione per alcuni dei suoi metodi per configurare la connessione websocket.
 Nel primo metodo, registriamo un endpoint websocket che i client utilizzeranno per connettersi al nostro server websocket.
 Nel secondo metodo, stiamo configurando un broker di messaggi che verrà utilizzato per instradare i messaggi da un client all'altro.

 La prima riga definisce che i messaggi la cui destinazione inizia con "/app" dovrebbero essere indirizzati ai metodi di gestione dei messaggi (definiremo questi metodi a breve).

 E, la seconda riga definisce che, i messaggi
 la cui destinazione inizia con "/topic" dovrebbero essere indirizzati al broker di messaggi.
 Il broker di messaggi trasmette messaggi a tutti i client connessi che sono iscritti al topic.
 */
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}
