package poiw.project.CAS;

public class ValidationResponse {
    private String cardOwner;
    private String readerName;
    private String validation;

    public ValidationResponse(String cardOwner, String readerName, String validation) {
        this.cardOwner = cardOwner;
        this.readerName = readerName;
        this.validation = validation;
    }

    public String getCardOwner() {
        return cardOwner;
    }

    public String getReaderName() {
        return readerName;
    }

    public String getValidation() {
        return validation;
    }
}