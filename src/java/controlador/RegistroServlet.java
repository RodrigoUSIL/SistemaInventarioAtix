package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import modelo.Usuario;
import repository.UsuarioRepository;

// servlet que maneja el registro de nuevos usuarios
@WebServlet(name = "RegistroServlet", urlPatterns = {"/registro"})
public class RegistroServlet extends HttpServlet {

    // repositorio para operaciones de usuario en la bd
    private final UsuarioRepository repo = new UsuarioRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // obtiene los datos del formulario de registro
        String nomUsuario = request.getParameter("nomUsuario");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        String confirmar = request.getParameter("confirmar");

        // valida que los campos obligatorios no esten vacios
        // si falta alguno muestra un error y vuelve al formulario
        if (nomUsuario == null || nomUsuario.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()
                || confirmar == null || confirmar.trim().isEmpty()) {
            request.setAttribute("error", "complete todos los campos.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // verifica que ambas contrasenas sean iguales
        // si no coinciden muestra un error y vuelve al formulario
        if (!contrasena.equals(confirmar)) {
            request.setAttribute("error", "las contrasenas no coinciden.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // verifica que el nombre de usuario no este ya en uso
        // si ya existe muestra un error y vuelve al formulario
        if (repo.existeUsuario(nomUsuario.trim())) {
            request.setAttribute("error", "ese nombre de usuario ya existe.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // construye el objeto usuario con los datos del formulario
        Usuario u = new Usuario();
        u.setNomUsuario(nomUsuario.trim());
        // si no ingreso correo guarda cadena vacia
        u.setCorreoUsuario(correo != null ? correo.trim() : "");
        u.setContrasena(contrasena.trim());

        // intenta insertar el usuario en la bd
        // si se inserta correctamente redirige al login con mensaje de exito en la url
        if (repo.registrar(u)) {
            // redirige al login con mensaje de exito en la url
            response.sendRedirect("login.jsp?exito=cuenta+creada+exitosamente");
        } else { // si hay un error al insertar muestra un error y vuelve al formulario
            request.setAttribute("error", "error al registrar. intente nuevamente.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // si accede por get muestra el formulario de registro
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }
}
