package vikram.sample;

import javax.persistence.*;

@Entity
@Table(name="entity")
public class PersistentEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String data;

}
