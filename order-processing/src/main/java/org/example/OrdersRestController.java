package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/order-processing/orders")
public class OrdersRestController implements RowMapper<OrdersRestController.OrderItem> {

  private final RestTemplate restTemplate;

  private final JdbcTemplate jdbcTemplate;

  public OrdersRestController(RestTemplate restTemplate, JdbcTemplate jdbcTemplate) {
    this.restTemplate = restTemplate;
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping
  public List<OrderItem> getOrderItems() {
    return this.jdbcTemplate.query("select * from order_item", this);
  }

  @Override
  public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
    int productId = rs.getInt("id_product");
    Product product = this.restTemplate.getForObject(
        "http://catalogue/api/catalogue/products/" + productId, Product.class);
    return new OrderItem(rs.getInt("id"), rs.getInt("amount"),
        product);
  }

  record OrderItem(int id, int amount, Product product) {

  }

  record Product(int id, String title) {

  }
}