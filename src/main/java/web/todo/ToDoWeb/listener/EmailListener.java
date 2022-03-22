package web.todo.ToDoWeb.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import web.todo.ToDoWeb.model.Email;

@Component
public class EmailListener extends AbstractMongoEventListener<Email> {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailListener.class);

    @Override
    public void onAfterSave(AfterSaveEvent<Email> event) {
        LOGGER.info(event.getDocument().get("origin").toString());
        LOGGER.info(event.getDocument().get("destination").toString());
        LOGGER.info(event.getDocument().get("sentDate").toString());
        LOGGER.info(event.getDocument().get("isDeleted").toString());
        LOGGER.info(event.getDocument().get("message").toString());
    }
}
