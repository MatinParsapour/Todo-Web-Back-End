package web.todo.ToDoWeb.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.todo.ToDoWeb.exception.NotFoundException;
import web.todo.ToDoWeb.model.Message;
import web.todo.ToDoWeb.model.User;
import web.todo.ToDoWeb.model.dto.MessageDTO;
import web.todo.ToDoWeb.repository.MessageRepository;
import web.todo.ToDoWeb.service.MessageService;
import web.todo.ToDoWeb.service.RequestService;
import web.todo.ToDoWeb.service.UserService;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.http.MediaType.*;
import static web.todo.ToDoWeb.constants.FileConstants.*;

@Service
public class MessageServiceImpl extends BaseServiceImpl<Message, String, MessageRepository> implements MessageService {

    private final MessageRepository messageRepository;
    private final RequestService requestService;
    private final UserService userService;

    public MessageServiceImpl(MessageRepository repository, MessageRepository messageRepository, RequestService requestService, UserService userService) {
        super(repository);
        this.messageRepository = messageRepository;
        this.requestService = requestService;
        this.userService = userService;
    }

    @Override
    public void sendMessage(String requestId, MessageDTO messageDTO) {
        Message message = initializeMessageByMessageDTO(messageDTO);
        Message newMessage = save(message);
        requestService.addMessageToRequest(requestId, newMessage);
    }

    @Override
    public void updateMessage(Message message) {
        if (messageRepository.existsByIdAndIsDeletedFalse(message.getId())) {
            save(message);
        } else {
            throw new NotFoundException("The message might be deleted");
        }
    }

    @Override
    public void deleteMessage(String messageId) {
        Message message = findById(messageId).orElseThrow(() -> new NotFoundException("The message might be deleted"));
        message.setIsDeleted(true);
        save(message);
    }

    @Override
    public void sendPicture(String requestId, MultipartFile picture, String userId) throws IOException {
        validImageTypeAssertion(picture);
        Path requestFolder = Paths.get(REQUEST_FOLDER + userId + FORWARD_SLASH + requestId).toAbsolutePath().normalize();
        if (!Files.exists(requestFolder)) {
            Files.createDirectories(requestFolder);
        }
        String fileName = validateFileName(picture, requestFolder);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage(setPictureImageUrl(userId, requestId, fileName));
        messageDTO.setUserId(userId);
        sendMessage(requestId, messageDTO);
    }

    private String validateFileName(MultipartFile picture, Path requestFolder) throws IOException {
        String fileName = picture.getOriginalFilename();
        try {
            Files.copy(picture.getInputStream(), requestFolder.resolve(fileName));
        } catch (FileAlreadyExistsException exception){
            fileName = UUID.randomUUID() + DOT + JPG_EXTENSION;
            Files.copy(picture.getInputStream(), requestFolder.resolve(fileName));
        }
        return fileName;
    }

    private String setPictureImageUrl(String userId, String requestId, String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(MESSAGE_IMAGE_PATH + FORWARD_SLASH + userId + FORWARD_SLASH + requestId + FORWARD_SLASH + fileName).toUriString();
    }

    private void validImageTypeAssertion(MultipartFile profileImage) {
        if (!Arrays.asList(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(profileImage.getContentType())) {
            throw new IllegalStateException(profileImage.getOriginalFilename() + " is not a suitable file please upload image");
        }
    }

    private Message initializeMessageByMessageDTO(MessageDTO messageDTO) {
        Message message = new Message();
        message.setMessage(messageDTO.getMessage());
        message.setUser(validateAndReturnUser(messageDTO.getUserId()));
        return message;
    }

    private User validateAndReturnUser(String userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new NotFoundException("No user found"));
    }
}
