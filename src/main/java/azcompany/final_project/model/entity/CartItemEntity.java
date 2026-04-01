package azcompany.final_project.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
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
