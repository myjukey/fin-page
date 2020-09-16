package bbangshop;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PageRepository extends CrudRepository<Page, Long> {
    List<Page> findByReservationId(Long reservationId);
}