package nineseven.review.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "ID_SEQUENCE_GENERATOR", sequenceName = "ID_SEQUENCE", initialValue = 1, allocationSize = 50)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InputHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQUENCE")
    @Column(name = "input_history_id")
    private Long id;

    private String inputTitle;
    private Integer inputSubscribe;

    @Builder
    public InputHistory(String inputTitle, Integer inputSubscribe) {
        this.inputTitle = inputTitle;
        this.inputSubscribe = inputSubscribe;
    }

}
