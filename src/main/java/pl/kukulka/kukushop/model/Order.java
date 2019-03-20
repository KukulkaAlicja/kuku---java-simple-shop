package pl.kukulka.kukushop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString(of = {"user", "products"})

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long orderId;

    @NotEmpty
    @Column(name = "address")
    private String address;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "USER_ID")
    private User user;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany
    @JsonIgnore
    @JoinTable(name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "ORDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "PRODUCT_ID"))
    private List<Product> products = new ArrayList<>();


}
