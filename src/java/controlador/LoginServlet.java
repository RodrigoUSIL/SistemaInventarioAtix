package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import modelo.Usuario;
import repository.UsuarioRepository;

// servlet que maneja el inicio de sesion del sistema
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    // repositorio para consultar usuarios en la bd
    private final UsuarioRepository repo = new UsuarioRepository();

    // maximo de intentos permitidos antes de bloquear
    private static final int MAX_INTENTOS = 3;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // obtiene los datos enviados desde el formulario
        String nomUsuario = request.getParameter("nomUsuario");
        String contrasena = request.getParameter("contrasena");

        // obtiene o crea la sesion actual
        HttpSession session = request.getSession();

        // recupera el contador de intentos, si no existe lo inicializa en 0
        Integer intentos = (Integer) session.getAttribute("intentos");
        if (intentos == null) {
            intentos = 0;
        }

        // si ya supero el maximo de intentos bloquea el acceso
        if (intentos >= MAX_INTENTOS) {
            request.setAttribute("error", "cuenta bloqueada. demasiados intentos fallidos.");
            request.setAttribute("bloqueado", true);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // valida que los campos no esten vacios
        // si falta alguno muestra un error y vuelve al formulario indicando cuantos intentos quedan
        if (nomUsuario == null || nomUsuario.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "complete todos los campos.");
            request.setAttribute("intentosRestantes", MAX_INTENTOS - intentos);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // busca el usuario en la bd con las credenciales ingresadas
        Usuario usuario = repo.login(nomUsuario.trim(), contrasena.trim());

        // si el usuario es encontrado, inicia sesion y redirige al menu principal
        if (usuario != null) {
            // credenciales correctas, reinicia el contador de intentos
            session.setAttribute("intentos", 0);
            // guarda el usuario activo en la sesion
            session.setAttribute("usuarioActivo", usuario);
            // redirige al menu principal
            response.sendRedirect("index.jsp");
        } else { // si las credenciales son incorrectas, incrementa el contador y muestra error indicando cuantos intentos quedan
            intentos++;
            session.setAttribute("intentos", intentos);
            int restantes = MAX_INTENTOS - intentos;

            // si no quedan intentos, bloquea el acceso
            if (restantes <= 0) {
                request.setAttribute("error", "cuenta bloqueada. demasiados intentos fallidos.");
                request.setAttribute("bloqueado", true);
            } else { // si quedan intentos, muestra error indicando cuantos intentos quedan
                request.setAttribute("error", "usuario o contrasena incorrectos.");
                request.setAttribute("intentosRestantes", restantes);
            }
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // si accede por get muestra directamente el formulario
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
