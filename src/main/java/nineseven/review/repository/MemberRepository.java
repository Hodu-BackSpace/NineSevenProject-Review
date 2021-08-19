package nineseven.review.repository;

import lombok.RequiredArgsConstructor;
import nineseven.review.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

}
