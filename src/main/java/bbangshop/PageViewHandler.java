package bbangshop;

import bbangshop.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.core.Ordered;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageViewHandler {


    @Autowired
    private PageRepository pageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationAccepted_then_CREATE (@Payload ReservationAccepted reservationAccepted) {
        try {
            if (reservationAccepted.isMe()) {
                // view 객체 생성
                Page page = new Page();
                // view 객체에 이벤트의 Value 를 set 함
                page.setReservationId(reservationAccepted.getReservationId());
                page.setBakerName(reservationAccepted.getBakerName());
                page.setBreadId(reservationAccepted.getBreadId());
                page.setStatus(reservationAccepted.getStatus());
                // view 레파지 토리에 save
                pageRepository.save(page);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationCanceled_then_UPDATE (@Payload ReservationCanceled reservationCanceled) {
        try {
            if (reservationCanceled.isMe()) {
                // view 객체 조회
                List<Page> reservationList = pageRepository.findByReservationId(reservationCanceled.getReservationId());
                for(Page page : reservationList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    page.setStatus(reservationCanceled.getStatus());
                    // view 레파지 토리에 save
                    pageRepository.save(page);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}