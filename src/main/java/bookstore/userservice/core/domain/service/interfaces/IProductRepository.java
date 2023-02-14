package bookstore.userservice.core.domain.service.interfaces;

import bookstore.userservice.core.domain.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface IProductRepository extends CrudRepository<Product, UUID> {

    List<Product> findByTitle(String title);

    Product findByIsbn13(String isbn13);

    List<Product> findAll();

    //TODO: find a fix for findById

}
