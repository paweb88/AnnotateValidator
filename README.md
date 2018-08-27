# AnnotateValidator

Simple lib for validate DTO with Annotation rules

## Using

```Java
public class Car implements Parcelable {

    @Required
    private String plateNumber = "";

    @GreaterThan()
    private int mileage = 0;

    @GreaterThan()
    private int fuelTank = 0;
    /**
     code goes here
    */
}

Validator validator = new Validator(car);
if (validator.validate()) {
    //do something
} else {
    CarService.invalidField(validator.getErrors());
}
```
