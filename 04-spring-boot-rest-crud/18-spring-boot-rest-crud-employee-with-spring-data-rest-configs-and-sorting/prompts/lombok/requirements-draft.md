Introduce lombok in the project to reduce boilerplate code in the Entities.
Use Lombok annotations @Getter, @Setter, @Builder, etc., to automatically generate getters, setters, constructors, and other common methods in the Entity classes.
Please also configure the maven compiler plugin to enable annotation processing for Lombok (as per Spring Initializr with SpringBoot 3.4.0).
Ensure that the Lombok library is included in the project dependencies and that the IDE is configured to recognize Lombok annotations.
Ensure that the Entities are properly annotated with Lombok annotations to reduce boilerplate code.
The goal is to simplify the Entity classes while maintaining their functionality and readability.
Review the existing Entities and identify areas where Lombok can be applied effectively.