package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks // Mock 만들고 DI해줍니다.
    private MailService mailService;

    @DisplayName("메일 전송 테스트 - Unit Test")
    @Test
    public void sendMailTest() throws Exception {
        // given
         /* Mock, InjectMocks 처리
         MailSendClient mailSendClient = mock(MailSendClient.class);
         MailSendHistoryRepository mailSendHistoryRepository = mock(MailSendHistoryRepository.class);
         MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);
         */

        // Given에 when이 있는 것이 어색합니다. 따라서 나온 것이 BDDMockito를 사용합니다.
        /*
        when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class),
            any(String.class)))
            .thenReturn(true);
        */

        BDDMockito.given(
            mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class),
                any(String.class))).willReturn(true);

        // when
        boolean result = mailService.sendMail("", "", "", "");

        // then
        assertThat(result).isTrue();
        // mock 객체를 만들면 기본적으로 Default value가 들어갑니다. 그러나 명확하게 보려면 행위의 호출 횟수를 보는 것이 좋습니다.
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }

}
