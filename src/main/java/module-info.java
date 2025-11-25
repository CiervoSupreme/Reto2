module org.example.reto2 {
    // ===================================
    // Módulos de JavaFX
    // Módulos esenciales para la interfaz de usuario.
    requires javafx.fxml;
    requires javafx.controls;

    // ===================================
    // Módulos de Persistencia (Hibernate/JPA)
    // *CORRECCIÓN:* Usamos el nombre del módulo de Jakarta Persistence.
    requires jakarta.persistence;

    // Módulo principal de Hibernate.
    requires org.hibernate.orm.core;
    // Módulo de SQL y conexión JDBC.
    requires java.sql;

    // ===================================
    // Solución para el error 'javax.naming.Referenceable'
    requires java.naming;

    // Conector de MySQL.
    requires mysql.connector.j;
    // ===================================

    // Permite que Hibernate acceda y modifique las clases del modelo mediante reflexión
    opens org.example.reto2.Model to org.hibernate.orm.core;

    // Exporta el paquete base para que JavaFX pueda arrancar la aplicación (MainApp)
    exports org.example.reto2;

    // Abre el paquete de controladores para que el cargador FXML pueda inyectar la vista
    opens org.example.reto2.controller to javafx.fxml;
}