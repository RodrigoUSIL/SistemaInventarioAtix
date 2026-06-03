package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import repository.UsuarioRepository;

// servlet que maneja el cambio de contrasena del usuario
@WebServlet(name = "CambiarContrasenaServlet", urlPatterns = {"/cambiarContrasena"})
public class CambiarContrasenaServlet extends HttpServlet {

    // repositorio para actualizar datos en la bd
    private final UsuarioRepository repo = new UsuarioRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // obtiene los datos del formulario
        String nomUsuario = request.getParameter("nomUsuario");
        String contrasenaNueva = request.getParameter("contrasenaNueva");
        String confirmar = request.getParameter("confirmar");

        // valida que todos los campos esten llenos
        // si falta alguno muestra un error y vuelve al formulario
        if (nomUsuario == null || nomUsuario.trim().isEmpty()
                || contrasenaNueva == null || contrasenaNueva.trim().isEmpty()
                || confirmar == null || confirmar.trim().isEmpty()) {
            request.setAttribute("error", "complete todos los campos.");
            request.getRequestDispatcher("cambiarContrasena.jsp").forward(request, response);
            return;
        }

        // verifica que las contrasenas nuevas coincidan
        // si no coinciden muestra un error y vuelve al formulario
        if (!contrasenaNueva.equals(confirmar)) {
            request.setAttribute("error", "las contrasenas no coinciden.");
            request.getRequestDispatcher("cambiarContrasena.jsp").forward(request, response);
            return;
        }

        // verifica que el usuario exista en la bd antes de actualizar
        // si no existe muestra un error y vuelve al formulario
        if (!repo.existeUsuario(nomUsuario.trim())) {
            request.setAttribute("error", "usuario no encontrado.");
            request.getRequestDispatcher("cambiarContrasena.jsp").forward(request, response);
            return;
        }

        // intenta actualizar la contrasena en la bd
        // si se actualiza correctamente redirige al login con mensaje de exito
        if (repo.cambiarContrasena(nomUsuario.trim(), contrasenaNueva.trim())) {
            // redirige al login con mensaje de exito
            response.sendRedirect("login.jsp?exito=contrasena+actualizada+correctamente");
        } else { // sino muestra un error y vuelve al formulario
            request.setAttribute("error", "error al actualizar. intente nuevamente.");
            request.getRequestDispatcher("cambiarContrasena.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // si accede por get muestra el formulario
        request.getRequestDispatcher("cambiarContrasena.jsp").forward(request, response);
    }
}
