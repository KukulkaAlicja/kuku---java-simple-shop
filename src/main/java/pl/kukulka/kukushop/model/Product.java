package pl.kukulka.kukushop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString(of = "user")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long productId;

    @NotEmpty
    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @NotNull
    @DecimalMin(value = "0.1")
    @Column(name = "price")
    private BigDecimal price;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "USER_ID")
    private User user;

    @Column(name = "available")
    private boolean available;

    @Column(name = "add_date")
    @Temporal(TemporalType.DATE)
    private Date date;


}



