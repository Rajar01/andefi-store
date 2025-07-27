package store.andefi;

import com.github.f4b6a3.uuid.UuidCreator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.mindrot.jbcrypt.BCrypt;
import store.andefi.dto.TEIDto;
import store.andefi.entity.*;
import store.andefi.repository.*;
import store.andefi.service.TEIService;

@Path("/create")
public class GreetingResource {
  @Inject ProductRepository productRepository;
  @Inject DiscountRepository discountRepository;
  @Inject CategoryRepository categoryRepository;
  @Inject StockRepository stockRepository;
  @Inject MediaRepository mediaRepository;
  @Inject RoleRepository roleRepository;
  @Inject AccountRepository accountRepository;
  @Inject ReviewRepository reviewRepository;
  @RestClient TEIService teiService;

  @GET
  @Transactional
  public void create() {
    Role role = new Role();
    role.setName("customer");

    roleRepository.persist(role);

    // ================================================================= //
    String csvFilePath =
        "D:\\Workspace\\marketing_sample_for_amazon_com-ecommerce__20200101_20200131__10k_data.csv";
    List<String[]> records = new ArrayList<>();
    int maxRecords = 100; // The maximum number of records to read
    int recordsRead = 0;

    try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null && recordsRead < maxRecords) {
        records.add(nextLine);
        recordsRead++;
      }
    } catch (IOException | CsvException e) {
      e.printStackTrace();
    }

    records = records.subList(1, records.size());

    // ================================================================= //
    List<Product> products = new ArrayList<>();

    Category category1 = new Category();
    category1.setName("Category 1");

    Category category2 = new Category();
    category2.setName("Category 2");

    categoryRepository.persist(List.of(category1, category2));

    Discount discount = new Discount();
    discount.setDiscountPercentage(30);
    discount.setActive(true);
    discount.setStartDate(Instant.now());
    discount.setEndDate(Instant.now().plus(Duration.ofDays(2)));

    discountRepository.persist(discount);

    for (int i = 0; i < records.size(); i++) {
      Media media = new Media();
      media.setUrls(Map.of("image", "example.com"));

      mediaRepository.persist(media);

      Stock stock = new Stock();
      stock.setQuantityOnHand(100);
      stock.setAvailableQuantity(100);
      stock.setReservedQuantity(0);
      stock.setSoldQuantity(0);

      stockRepository.persist(stock);

      Product product = new Product();
      product.setId(UuidCreator.getTimeOrderedEpoch());
      product.setName(records.get(i)[1]);
      product.setDescription(records.get(i)[27]);
      product.setPrice(1000000);
      product.setAttributes(Map.of("Color", "Black", "Weight", "100kg"));
      product.setMedia(media);
      product.setStock(stock);
      product.setDiscount(discount);

      float[][] embedding =
          teiService.getEmbedding(
              new TEIDto(
                  String.format(
                      "Product name:%s, product description:%s",
                      product.getName(), product.getDescription())));

      product.setEmbedding(embedding[0]);

      if (i <= 20) product.setCategories(List.of(category1));
      if (i > 20 && i <= 40) product.setCategories(List.of(category2));
      if (i > 40) product.setCategories(List.of(category1, category2));

      products.add(product);
    }

    productRepository.persist(products);

    // ================================================================= //

    Random random = new Random();

    List<Review> reviews = new ArrayList<>();

    for (int i = 1; i <= 20; i++) {
      Media media = new Media();
      media.setUrls(Map.of("image", String.format("example%d.com", i)));

      mediaRepository.persist(media);

      ShippingAddress shippingAddress = new ShippingAddress();
      shippingAddress.setAddress(String.format("Address %d", i));

      Account account = new Account();
      account.setEmail(String.format("account%d@gmail.com", i));
      account.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));
      account.setUsername(String.format("Account %d username", i));
      account.setFullName(String.format("Account %d full name", i));
      account.setPhoneNumber(String.format("Account %d phone number", i));
      account.setShippingAddress(shippingAddress);
      account.setRoles(Set.of(role));

      accountRepository.persist(account);

      int rating = random.nextInt(5) + 1;

      Review review = new Review();
      review.setId(UuidCreator.getTimeOrderedEpoch());
      review.setContent(String.format("Content %d", i));
      review.setRating(rating);
      review.setProduct(products.getFirst());
      review.setMedia(media);
      review.setAccount(account);

      reviews.add(review);
    }

    for (int i = 21; i <= 31; i++) {
      Media media = new Media();
      media.setUrls(Map.of("image", String.format("example%d.com", i)));

      mediaRepository.persist(media);

      ShippingAddress shippingAddress = new ShippingAddress();
      shippingAddress.setAddress(String.format("Address %d", i));

      Account account = new Account();
      account.setEmail(String.format("account%d@gmail.com", i));
      account.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));
      account.setUsername(String.format("Account %d username", i));
      account.setFullName(String.format("Account %d full name", i));
      account.setPhoneNumber(String.format("Account %d phone number", i));
      account.setShippingAddress(shippingAddress);
      account.setRoles(Set.of(role));

      accountRepository.persist(account);

      int rating = random.nextInt(5) + 1;

      Review review = new Review();
      review.setId(UuidCreator.getTimeOrderedEpoch());
      review.setContent("");
      review.setRating(rating);
      review.setProduct(products.getFirst());
      review.setMedia(media);
      review.setAccount(account);

      reviews.add(review);
    }

    reviewRepository.persist(reviews);
  }
}
