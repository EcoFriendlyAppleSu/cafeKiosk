package sample.cafekiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository repository) {
        this.productRepository = repository;
    }

    // 여러 사용자가 접근할 수 있다면 동시성 이슈가 발생할 수 있습니다.
    // 해결 1. DB pk에 Unique 조건을 등록해 해결하는 방법
    // 해결 2. UUID를 사용해 상품 생성
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {

        /*
         productNumber 부여 : 001, 002, 003, 004 형태
         DB에서 마지막 저장된 Product의 상품 번호를 읽어와서 +1
         Make nextProductNumber
        */
        String nextProductNumber = nextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(
            ProductSellingStatus.forDisplay());

        return products.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    private String nextProductNumber(){
        String latestProductNumber = productRepository.findLatestProduct();
        if(latestProductNumber == null){
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }

}
