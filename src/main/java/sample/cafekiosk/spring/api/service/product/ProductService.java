package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.forDisplay;

/**
 * readOnly = true : 읽기전용
 * CRUD 에서 CUD 동작 x / only Read
 * JPA : CUD 스냅샷 저장 x, 변경 감지x (성능 향)
 * <p>
 * CQRS - Command / Query
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 동시성 이슈 - ExecutorService를 통해 스레드풀 생성 후 동시성 테스트 가능
    // 1. 3번 시도 - 중요도가 높지 않은 경우
    // 2. 유니크 인덱스를 부여해 정책을 uuid로 변경 - 중요도가 높은 경우
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return "%03d".formatted(nextProductNumberInt);
    }

    public List<ProductResponse> getSellingProducts() {
        // 판매중, 판매보류 상품이 판매 목록 상품이다.
        List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
