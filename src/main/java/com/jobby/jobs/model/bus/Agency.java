package com.jobby.jobs.model.bus;

import com.jobby.jobs.model.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(indexes = @Index(
                name = "idx_agency_code",
                columnList = "code",
                unique = true
        )
)
@Data
@Accessors(chain = true)
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id")
    private User owner;

    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Bus> buses;
}
