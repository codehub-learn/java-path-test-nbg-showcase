package gr.codelearn.rentbnb.service;

public interface MailService {
	boolean send(String subject, String body, String receiver);
}
