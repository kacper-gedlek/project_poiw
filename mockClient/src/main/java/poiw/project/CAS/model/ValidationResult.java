package poiw.project.CAS.model;

/**
 * Walidacja odpowiedzi serwera.
 */
public class ValidationResult {
    private String cardOwner;
    private String readerName;
    private String validation;

    public ValidationResult() {
    }

    public ValidationResult(String cardOwner, String readerName, String validation) {
        this.cardOwner = cardOwner;
        this.readerName = readerName;
        this.validation = validation;
    }

    public String getCardOwner() {
        return cardOwner;
    }

    public void setCardOwner(String cardOwner) {
        this.cardOwner = cardOwner;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public boolean isAccessGranted() {
        return "ACCESS_GRANTED".equals(validation);
    }

    public boolean isAccessDenied() {
        return "ACCESS_DENIED".equals(validation);
    }

    public boolean isError() {
        return "ERROR".equals(validation);
    }

    @Override
    public String toString() {
        return "ValidationResult{cardOwner='" + cardOwner + "', readerName='" + readerName
                + "', validation='" + validation + "'}";
    }
}
