package poiw.project.CAS;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", unique = true, nullable = false)
    @Size(min = 16, max = 16)
    private String cardNumber;

    @Column(name = "card_owner")
    private String cardOwner;

    @Column(name = "access_level")
    @Min(0)
    @Max(5)
    private Integer accessLevel = 0;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardOwner() { return cardOwner; }
    public void setCardOwner(String cardOwner) { this.cardOwner = cardOwner; }
    public Integer getAccessLevel() { return accessLevel; }
    public void setAccessLevel(Integer accessLevel) { this.accessLevel = accessLevel; }
}
