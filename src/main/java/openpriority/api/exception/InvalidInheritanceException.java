package openpriority.api.exception;

public class InvalidInheritanceException extends RuntimeException
{
    private final Class<?> received, required, root;

    public InvalidInheritanceException(Class<?> received, Class<?> required, Class<?> root)
    {
        this.received = received;
        this.required = required;
        this.root = root;
    }

    @Override
    public String getMessage()
    {
        return String.format("Class %s does not extend/implement class %s required to extend/implement class %s", received.getName(), required.getName(), root.getName());
    }
}
