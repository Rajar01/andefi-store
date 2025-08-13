package store.andefi;

import com.github.f4b6a3.uuid.UuidCreator;
import com.github.javafaker.Faker;
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
    Faker faker = new Faker(new Locale("in-ID"));
    // ================================================================= //

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
      media.setUrls(
          Map.of(
              "image",
              "https://storage.googleapis.com/kagglesdsdata/datasets/4858610/8201367/Furniture_Data/beds/Beach/24547beach-style-nightstands-and-bedside-tables.jpg?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=databundle-worker-v2%40kaggle-161607.iam.gserviceaccount.com%2F20250813%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20250813T013740Z&X-Goog-Expires=345600&X-Goog-SignedHeaders=host&X-Goog-Signature=54db95484353d4f530de3ae6c50122afcaf43f0ea2a04252e8526169b53673ef6814c4efd4a6e30c20b769c691417a915f33ae81d8ba27077ca5e764d6d0be5815a4f35623b0ea1fb419f3c895e4065a2022c7f2c647723458683c82782704bbbaa8277ad93b7bedf57c8fc9884e5c2524f30b0eb01f0bf78a2e9fad12814c6be7d98169cccf668ab3f74fc229c677eeb2f4c7528e0e0b45ee1bf49e2a95b6627f8b34a23eea71216cd04f24fb226e119cfe1a1aa836ee5dc1ecc9efe763515dc6fab0e4809aa9509988321a4dbae3b7a9df6940616f8cdc112cab0f81042e7b5db77e8a55f3de7f544954f0134fa0fab917d1ed571062c629d58b0fd365041a"));

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
      product.setDescription(faker.lorem().paragraph(5));
      product.setPrice(faker.number().numberBetween(100000, 300000));
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
      media.setUrls(
          Map.of(
              "image",
              "https://placehold.co/500/png|https://placehold.co/600/png|https://placehold.co/500/png|https://placehold.co/500/png|https://placehold.co/500/png|https://placehold.co/500/png|https://placehold.co/500/png"));

      mediaRepository.persist(media);

      ShippingAddress shippingAddress = new ShippingAddress();
      shippingAddress.setAddress(faker.address().fullAddress());

      Account account = new Account();
      account.setEmail(String.format("account%d@gmail.com", i));
      account.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));
      account.setUsername(faker.name().username());
      account.setFullName(faker.name().fullName());
      account.setPhoneNumber(String.format("Account %d phone number", i));
      account.setShippingAddress(shippingAddress);
      account.setRoles(Set.of(role));

      accountRepository.persist(account);

      int rating = random.nextInt(5) + 1;

      Review review = new Review();
      review.setId(UuidCreator.getTimeOrderedEpoch());
      review.setContent(faker.lorem().paragraph(faker.random().nextInt(1, 10)));
      review.setRating(rating);
      review.setProduct(products.getFirst());
      review.setMedia(media);
      review.setAccount(account);

      reviews.add(review);
    }

    for (int i = 21; i <= 31; i++) {
      Media media = new Media();
      media.setUrls(
          Map.of(
              "image",
              "https://placehold.co/500/png|https://placehold.co/600/png|https://placehold.co/500/png|https://placehold.co/500/png|https://placehold.co/500/png|https://placehold.co/500/png|https://placehold.co/500/png"));

      mediaRepository.persist(media);

      ShippingAddress shippingAddress = new ShippingAddress();
      shippingAddress.setAddress(faker.address().fullAddress());

      Account account = new Account();
      account.setEmail(String.format("account%d@gmail.com", i));
      account.setPassword(BCrypt.hashpw("12345678", BCrypt.gensalt()));
      account.setUsername(faker.name().username());
      account.setFullName(faker.name().fullName());
      account.setPhoneNumber(String.format("Account %d phone number", i));
      account.setShippingAddress(shippingAddress);
      account.setRoles(Set.of(role));

      accountRepository.persist(account);

      int rating = random.nextInt(5) + 1;

      Review review = new Review();
      review.setId(UuidCreator.getTimeOrderedEpoch());
      review.setContent(faker.lorem().paragraph(faker.random().nextInt(1, 10)));
      review.setRating(rating);
      review.setProduct(products.getFirst());
      review.setMedia(media);
      review.setAccount(account);

      reviews.add(review);
    }

    reviewRepository.persist(reviews);
  }
}
