import sistema.SistemaGestionAyudantes;
import vistas.VistaLogin;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Inicializar el sistema
        SistemaGestionAyudantes sistema = SistemaGestionAyudantes.getInstancia();
        sistema.inicializarSistema();
        
        // Iniciar la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            VistaLogin vistaLogin = new VistaLogin();
            vistaLogin.mostrar();
        });
        
        System.out.println("===========================================");
        System.out.println("Sistema de Gestión de Ayudantes - FIS EPN");
        System.out.println("===========================================");
    }
}
