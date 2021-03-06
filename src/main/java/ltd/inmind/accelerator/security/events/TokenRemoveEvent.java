package ltd.inmind.accelerator.security.events;

import org.springframework.context.ApplicationEvent;

public class TokenRemoveEvent extends ApplicationEvent {

    public TokenRemoveEvent(String token) {
        super(token);
    }

    @Override
    public String getSource() {
        return (String) super.getSource();
    }
}
