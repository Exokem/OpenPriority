package openpriority.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface FactoryOperation
{
    OperationStage stage() default OperationStage.ADD;
}
