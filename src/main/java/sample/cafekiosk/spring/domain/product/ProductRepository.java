package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * select *
     * from product
     * where selling_status in ('SELLING', 'HOLD')
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

    /**
     * select *
     * from product
     * where product_number in ("001", "002")
     */
    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    @Query("select p.productNumber from Product p order by p.id desc limit 1")
    String findLatestProductNumber();
}
