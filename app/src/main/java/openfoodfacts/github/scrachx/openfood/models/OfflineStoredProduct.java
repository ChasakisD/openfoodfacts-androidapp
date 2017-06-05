package openfoodfacts.github.scrachx.openfood.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity(indexes = {
        @Index(value = "code", unique = true)
})
public class OfflineStoredProduct {
    @Id private long id;
    private String code;
    private String product_name;
    private String generic_name;
    private String quantity;
    private String brands;
    private String ingredients_text;
    private String allergens;
    private String traces;
    private String nutrition_grade_fr;

    public OfflineStoredProduct(String code, String product_name, String generic_name, String quantity, String brands,
                          String ingredients_text, String allergens, String traces, String nutrition_grade_fr) {
        this.code = code;
        this.product_name = product_name;
        this.generic_name = generic_name;
        this.quantity = quantity;
        this.brands = brands;
        this.ingredients_text = ingredients_text;
        this.allergens = allergens;
        this.traces = traces;
        this.nutrition_grade_fr = nutrition_grade_fr;
    }

    @Generated(hash = 340864301)
    public OfflineStoredProduct(long id, String code, String product_name, String generic_name, String quantity,
            String brands, String ingredients_text, String allergens, String traces, String nutrition_grade_fr) {
        this.id = id;
        this.code = code;
        this.product_name = product_name;
        this.generic_name = generic_name;
        this.quantity = quantity;
        this.brands = brands;
        this.ingredients_text = ingredients_text;
        this.allergens = allergens;
        this.traces = traces;
        this.nutrition_grade_fr = nutrition_grade_fr;
    }

    @Generated(hash = 1397379995)
    public OfflineStoredProduct() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getGeneric_name() {
        return generic_name;
    }

    public void setGeneric_name(String generic_name) {
        this.generic_name = generic_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getIngredients_text() {
        return ingredients_text;
    }

    public void setIngredients_text(String ingredients_text) { this.ingredients_text = ingredients_text; }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public String getTraces() {
        return traces;
    }

    public void setTraces(String traces) {
        this.traces = traces;
    }

    public String getNutrition_grade_fr() {
        return nutrition_grade_fr;
    }

    public void setNutrition_grade_fr(String nutrition_grade_fr) { this.nutrition_grade_fr = nutrition_grade_fr; }
}