package poiw.project.CAS;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "readers")
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reader_number", unique = true, nullable = false)
    @Size(min = 16, max = 16)
    private String readerNumber;

    @Column(name = "reader_name")
    private String readerName;

    @Column(name = "required_access_level")
    @Min(0)
    @Max(5)
    private Integer requiredAccessLevel = 0;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReaderNumber() { return readerNumber; }
    public void setReaderNumber(String readerNumber) { this.readerNumber = readerNumber; }
    public String getReaderName() { return readerName; }
    public void setReaderName(String readerName) { this.readerName = readerName; }
    public Integer getRequiredAccessLevel() { return requiredAccessLevel; }
    public void setRequiredAccessLevel(Integer requiredAccessLevel) { this.requiredAccessLevel = requiredAccessLevel; }
}
