package poiw.project.CAS;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "readers")
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reader_number", unique = true, nullable = false)
    private String readerNumber;

    @Column(name = "required_access_level")
    @Min(0)
    @Max(5)
    private Integer requiredAccessLevel = 0;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReaderNumber() { return readerNumber; }
    public void setReaderNumber(String readerNumber) { this.readerNumber = readerNumber; }
    public Integer getRequiredAccessLevel() { return requiredAccessLevel; }
    public void setRequiredAccessLevel(Integer requiredAccessLevel) { this.requiredAccessLevel = requiredAccessLevel; }
}
