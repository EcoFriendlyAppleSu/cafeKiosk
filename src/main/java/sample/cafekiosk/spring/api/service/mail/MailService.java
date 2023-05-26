package sample.cafekiosk.spring.api.service.mail;

import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@Service
public class MailService {

    private final MailSendClient mailSendClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public MailService(MailSendClient mailSendClient,
        MailSendHistoryRepository mailSendHistoryRepository) {
        this.mailSendClient = mailSendClient;
        this.mailSendHistoryRepository = mailSendHistoryRepository;
    }

    // 기록을 남아야하기에 기록 Table이 필요합니다.
    public boolean sendMail(String fromEmail, String toEmail, String subject, String contents) {

        boolean result = mailSendClient.sendEmail(fromEmail, toEmail, subject, contents);

        if (result) {
            mailSendHistoryRepository.save(MailSendHistory.builder()
                .fromEmail(fromEmail)
                .toEmail(toEmail)
                .subject(subject)
                .contents(contents)
                .build()
            );
            return true;
        }
        return false;
    }
}
