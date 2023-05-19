package sample.cafekiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository repository) {
        this.productRepository = repository;
    }

    public void createProduct(ProductCreateRequest request) {

        // productNumber 부여 : 001, 002, 003, 004 형태
        // DB에서 마지막 저장된 Product의 상품 번호를 읽어와서 +1

        String latestProductNumber = productRepository.findLatestProduct();

        productRepository.save()

    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(
            ProductSellingStatus.forDisplay());

        return products.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

}
