package o.horbenko.feign;

public class FooDTO {
    private String someString;
    private FooDTO someInnerEntity;

    public FooDTO() {

    }

    public FooDTO(String someString) {
        this.someString = someString;
    }

    @Override
    public String toString() {
        return "FooDTO{" +
            "someString='" + someString + '\'' +
            ", someInnerEntity=" + someInnerEntity +
            '}';
    }

    public String getSomeString() {
        return someString;
    }

    public void setSomeString(String someString) {
        this.someString = someString;
    }

    public FooDTO getSomeInnerEntity() {
        return someInnerEntity;
    }

    public void setSomeInnerEntity(FooDTO someInnerEntity) {
        this.someInnerEntity = someInnerEntity;
    }
}
