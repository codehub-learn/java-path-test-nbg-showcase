package gr.codelearn.eshop.service;

public interface MailService {
	boolean send(String subject, String body, String receiver);
}
