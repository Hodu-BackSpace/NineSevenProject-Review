package nineseven.review.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.domain.InputHistory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InputHistoryRepository {

    private final EntityManager em;

    public void save(InputHistory inputHistory) {
        em.persist(inputHistory);
    }

    public List<InputHistory> findAll() {
        return em.createQuery("select h from InputHistory h", InputHistory.class)
                .getResultList();
    }

}
