package azcompany.final_project.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_items")
public class CartItemEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookEntity book;
    private Integer quantity;
}
