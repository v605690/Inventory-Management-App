package com.crus.Inventory_Management_System.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.proxy.HibernateProxy;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "vendor")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    @Column(name = "contactName", nullable = false)
    private String contactName;

    @Column(name = "address", nullable = false)
    private String address;


    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "emailAddress", nullable = false)
    private String emailAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "vendor_products",
            joinColumns = @JoinColumn(name = "vendor_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    public void addProduct(Product product) {
        if (this.products == null) {
            this.products = new HashSet<>();
        }
        this.products.add(product);
        if (product.getVendors() == null) {
            product.setVendors(new HashSet<>());
        }
        product.getVendors().add(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.getVendors().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vendor)) return false;
        return id != null && id.equals(((Vendor) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
